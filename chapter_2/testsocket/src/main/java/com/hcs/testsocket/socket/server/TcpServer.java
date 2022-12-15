package com.hcs.testsocket.socket.server;


import com.hcs.testsocket.socket.ServerCallback;
import com.hcs.testsocket.utils.CommonUtils;
import com.hcs.testsocket.utils.Constants;
import com.hcs.testsocket.utils.LogUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {
    private static final String TAG = TcpServer.class.getSimpleName();
    private static final int TCP_MAX_CONNECT_COUNT = 10;
    private static final int READ_BLOCK_SIZE = 1024 * 1024;

    private ServerSocket mServerSocket;
    private List<Socket> mClientList;
    private ServerCallback mServerCallback;
    private ExecutorService mExecutorService;
    private Socket mSocket;

    private boolean mResult = true;

    private TcpServer() {
        mExecutorService = Executors.newCachedThreadPool();
    }

    private static class TcpServerHolder {
        private static final TcpServer INSTANCE = new TcpServer();
    }

    public static TcpServer getInstance() {
        return TcpServerHolder.INSTANCE;
    }


    private class ReadRunnable implements Runnable {
        private final Socket socket;

        public ReadRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            LogUtils.logD(TAG, "===============");
            try (InputStream inputStream = socket.getInputStream()) {
                try (OutputStream outputStream = socket.getOutputStream()) {
                    byte[] buffer = new byte[READ_BLOCK_SIZE];
                    int len;
                    if (inputStream.available() == 0) {
                        return;
                    }
                    String receiveStr;
                    String receiveBytes;
                    while ((len = inputStream.read(buffer)) != -1) {
                        receiveBytes = CommonUtils.bytesToHexString(buffer, 0, len);
                        LogUtils.logD(TAG, "message bytes from client is: %s", receiveBytes);
                        if (Constants.HEART_MSG.equalsIgnoreCase(receiveBytes)) {
                            continue;
                        }
                        receiveStr = new String(buffer, 0, len, StandardCharsets.UTF_8);
                        mServerCallback.receiveClientMsg(true, receiveStr);
                        outputStream.write(buffer, 0, len);
                        outputStream.flush();
                    }
                    mClientList.remove(socket);
                    closeClient(socket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean startServer(int port, ServerCallback callback) {
        mServerCallback = callback;
        mClientList = new ArrayList<>();

        mExecutorService.execute(() -> {
            try {
                mServerSocket = new ServerSocket(port);
                while (mResult) {
                    LogUtils.logD(TAG, "+++++++++++++++");
                    mSocket = mServerSocket.accept();
                    mClientList.add(mSocket);
                    LogUtils.logD(TAG, "---------------: " + mServerCallback);
                    if (mServerCallback != null) {
                        String host = mSocket.getInetAddress().getHostAddress();
                        mServerCallback.otherMsg(host, host + " connected success");
                    }
                    mExecutorService.execute(new ReadRunnable(mSocket));
                }
            } catch (IOException e) {
                e.printStackTrace();
                mResult = false;
            }
        });
        return mResult;
    }

    public synchronized boolean sendToClient(String msg) {
        boolean result = false;
        try {
            for (Socket socket : mClientList) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(msg);
                bw.flush();
            }
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void close() {
        try {
            if (mClientList != null) {
                for (Socket socket : mClientList) {
                    closeClient(socket);
                }
            }

            if (mServerSocket != null) {
                mServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeClient(Socket socket) throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
        }
    }
}