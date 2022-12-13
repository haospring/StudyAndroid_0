package com.hcs.testsocket.socket.client;

import android.os.Handler;
import android.os.HandlerThread;

import com.hcs.testsocket.socket.OnMessageArrivedListener;
import com.hcs.testsocket.socket.OnSocketStateListener;
import com.hcs.testsocket.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TcpClient {
    private static final String TAG = TcpClient.class.getSimpleName();
    private static final String THREAD_NAME_HEART = "thread_heart";
    private static final String THREAD_NAME_WORK = "thread_work";

    private static final int RECEIVE_BUFFER_SIZE = 1024 * 1024;
    private static final int CONNECT_TIME_OUT = 1000;
    private static final int RETRY_RATE = 500;
    private static final int MAX_RECONNECT_COUNT = 50;
    private static final int HEART_BEAT_MSG = 0XFF;
    private static final int HEART_BEAT_RATE = 3000;

    private int mRetryConnectCount;

    private volatile boolean mConnection = false;

    private OnSocketStateListener mOnSocketStateListener;
    private OnMessageArrivedListener mOnMessageArrivedListener;
    private HandlerThread mHeartThread;
    private HandlerThread mWorkThread;
    private Handler mHeartHandler;
    private Handler mWorkHandler;
    private Socket mSocket;
    private InetSocketAddress mInetSocketAddress;
    private final Object mLock = new Object();
    private InputStream mInputStream;
    private OutputStream mOutputStream;

    private final Runnable mHeartRunnable = new Runnable() {
        @Override
        public void run() {
            if (!sendHeartBeat2Server() && mSocket != null) {
                try {
                    mSocket.close();
                    mSocket = null;
                    mConnection = false;
                    mWorkHandler.post(mConnectRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }

            mHeartHandler.postDelayed(mHeartRunnable, HEART_BEAT_RATE);
        }
    };

    private final Runnable mConnectRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (mLock) {
                try {
                    mSocket = createSocket();
                    mSocket.connect(mInetSocketAddress, CONNECT_TIME_OUT);
                    onSocketConnected();
                } catch (IOException e) {
                    e.printStackTrace();
                    mConnection = false;
                    if (++mRetryConnectCount == MAX_RECONNECT_COUNT) {
                        mRetryConnectCount = 0;
                        if (mOnSocketStateListener != null) {
                            mOnSocketStateListener.onClientException(new ConnectException());
                        }
                    }

                    mWorkHandler.postDelayed(this, RETRY_RATE);
                }
            }
        }
    };

    public TcpClient() {
        initHandler();
    }

    public void initHandler() {
        mHeartThread = new HandlerThread(THREAD_NAME_HEART);
        mHeartThread.start();
        mWorkThread = new HandlerThread(THREAD_NAME_WORK);
        mWorkThread.start();
        mHeartHandler = new Handler(mHeartThread.getLooper());
        mWorkHandler = new Handler(mWorkThread.getLooper());
    }

    public void setOnSocketStateListener(OnSocketStateListener listener) {
        mOnSocketStateListener = listener;
    }

    public void setOnMessageArrivedListener(OnMessageArrivedListener listener) {
        mOnMessageArrivedListener = listener;
    }

    public void start(String host, int port) {
        if (isStarted()) {
            return;
        }

        mInetSocketAddress = new InetSocketAddress(host, port);
        mWorkHandler.post(mConnectRunnable);
    }

    public void sendToServer(String msg) {
        LogUtils.logD(TAG, "msg send to server is: " + msg);
        if (mOutputStream != null) {
            try {
                mOutputStream.write(msg.getBytes());
                mOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Socket createSocket() throws SocketException {
        Socket socket = new Socket();
        socket.setKeepAlive(true);
        socket.setReuseAddress(true);
        socket.setTcpNoDelay(true);
        socket.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
        return socket;
    }

    private void onSocketConnected() throws IOException {
        mInputStream = mSocket.getInputStream();
        mOutputStream = mSocket.getOutputStream();

        mHeartHandler.post(mHeartRunnable);

        if (mOnSocketStateListener != null) {
            mOnSocketStateListener.onClientStart();
        }
        mConnection = true;

        BufferedReader br = new BufferedReader(new InputStreamReader(mInputStream));
        String line;
        while (isStarted() && isConnected()) {
            line = br.readLine();
            LogUtils.logD(TAG, "message from server is: %s", line);
            if (mOnMessageArrivedListener != null) {
                mOnMessageArrivedListener.onMessageArrived(line.getBytes());
            }
        }
    }

    private boolean sendHeartBeat2Server() {
        if (mSocket == null || mSocket.isClosed() || mSocket.isOutputShutdown()) {
            return false;
        }

        final byte[] buffer = {(byte) HEART_BEAT_MSG};
        try {
            mOutputStream.write(buffer);
            mOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isStarted() {
        return mSocket != null && !mSocket.isClosed();
    }

    public boolean isConnected() {
        return mSocket != null && mSocket.isConnected() && mConnection;
    }

    public void close() {
        if (mWorkThread != null) {
            mWorkThread.quitSafely();
        }

        if (mHeartThread != null) {
            mHeartThread.quitSafely();
        }

        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
        }

        if (mHeartHandler != null) {
            mHeartHandler.removeCallbacksAndMessages(null);
        }

        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mOnSocketStateListener != null) {
            mOnSocketStateListener.onClientClose();
        }
    }
}
