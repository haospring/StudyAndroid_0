package com.hcs.testsocket.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hcs.testsocket.R;
import com.hcs.testsocket.databinding.ActivityMainBinding;
import com.hcs.testsocket.socket.OnMessageArrivedListener;
import com.hcs.testsocket.socket.OnSocketStateListener;
import com.hcs.testsocket.socket.ServerCallback;
import com.hcs.testsocket.socket.client.TcpClient;
import com.hcs.testsocket.socket.server.TcpServer;
import com.hcs.testsocket.utils.CommonUtils;
import com.hcs.testsocket.utils.LogUtils;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ServerCallback, OnMessageArrivedListener, OnSocketStateListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;

    private TcpServer mTcpServer;
    private TcpClient mTcpClient;

    private final ExecutorService mExecutorService = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initData();
        initView();

        mBinding.btnStartService.setOnClickListener(this);
        mBinding.btnConnectService.setOnClickListener(this);
        mBinding.btnSendMsg.setOnClickListener(this);
    }

    private void initData() {
        mTcpServer = TcpServer.getInstance();
        mTcpClient = new TcpClient();
    }

    private void initView() {
        mBinding.rbServer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.layServer.setVisibility(View.VISIBLE);
            } else {
                mBinding.layServer.setVisibility(View.GONE);
            }
        });

        mBinding.rbClient.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mBinding.layClient.setVisibility(View.VISIBLE);
            } else {
                mBinding.layClient.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == mBinding.btnStartService.getId()) {
            mTcpServer.startServer(40000, this);
        } else if (viewId == mBinding.btnConnectService.getId()) {
            // 10.0.2.16
            mTcpClient.setOnSocketStateListener(this);
            mTcpClient.setOnMessageArrivedListener(this);
            mTcpClient.start(Objects.requireNonNull(mBinding.etIpAddress.getText()).toString(), 40000);
        } else if (viewId == mBinding.btnSendMsg.getId()) {
            mExecutorService.execute(()->{
                mTcpClient.sendToServer(Objects.requireNonNull(mBinding.etMsg.getText()).toString());
            });
        }
    }

    @Override
    public void receiveClientMsg(boolean success, String msg) {
        LogUtils.logD(TAG, "message from client: %s", msg);
    }

    @Override
    public void otherMsg(String host, String msg) {
        String ipText = getResources().getText(R.string.ip_address) + host;
        mBinding.tvIpAddress.setText(ipText);
        LogUtils.logD(TAG, msg);
    }

    @Override
    public void onMessageArrived(byte[] message) {
        LogUtils.logD(TAG, "message from server: %s", Arrays.toString(message));
    }

    @Override
    public void onClientStart() {
        LogUtils.logD(TAG, "client started");
    }

    @Override
    public void onClientClose() {
        LogUtils.logD(TAG, "client closed");
    }

    @Override
    public void onClientException(Exception e) {
        LogUtils.logE(TAG, e, "exception occurred");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTcpClient != null) {
            mTcpClient.close();
        }

        if (mTcpServer != null) {
            mTcpServer.close();
        }
    }
}