package com.hcs.testmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.hcs.testmessenger.databinding.ActivityMainBinding;
import com.hcs.testmessenger.model.Book;
import com.hcs.testmessenger.model.User;
import com.hcs.testmessenger.service.MessengerService;
import com.hcs.testmessenger.utils.Constant;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private Messenger mClient;
    private Messenger mService;
    private User user;

    private final Handler mClientHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.MSG_WHAT_SERVICE) {
                Bundle data = msg.getData();
                data.setClassLoader(Book.class.getClassLoader());
                Book book = data.getParcelable(Constant.MSG_TITLE_BOOK);
                Log.d(TAG, "handleMessage: " + data.getString(Constant.MSG_TITLE_SERVICE));
                Log.d(TAG, "handleMessage: " + book);
                user.setBook(book);
            } else {
                super.handleMessage(msg);
            }
        }
    };

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mService = new Messenger(service);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        mClient = new Messenger(mClientHandler);
        user = new User(0, "任玉刚", true, new Book());

        mBinding.btnMain.setOnClickListener(this);
        mBinding.btnMainSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.btnMain.getId()) {
            Intent intent = new Intent(this, MessengerService.class);
            bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
        } else if (v.getId() == mBinding.btnMainSend.getId()) {
            if (mService != null) {
                Bundle data = new Bundle();
                data.putString(Constant.MSG_TITLE_CLIENT, Constant.MSG_CONTENT_CLIENT);
                data.putParcelable(Constant.MSG_TITLE_USER, user);
                Message msg = Message.obtain();
                msg.replyTo = mClient;
                msg.what = Constant.MSG_WHAT_CLIENT;
                msg.setData(data);
                try {
                    mService.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mClientHandler.removeCallbacksAndMessages(null);
    }
}