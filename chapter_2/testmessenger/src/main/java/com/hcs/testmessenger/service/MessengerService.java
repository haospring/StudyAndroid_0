package com.hcs.testmessenger.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hcs.testmessenger.model.Book;
import com.hcs.testmessenger.model.User;
import com.hcs.testmessenger.utils.Constant;

public class MessengerService extends Service {
    private static final String TAG = MessengerService.class.getSimpleName();
    private static final String THREAD_NAME = "messenger thread";
    private HandlerThread mHandlerThread = new HandlerThread(THREAD_NAME);
    private Messenger mService;
    private Handler mServiceHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(THREAD_NAME);
        mHandlerThread.start();
        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());
        mService = new Messenger(mServiceHandler);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mService.getBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandlerThread != null) {
            mHandlerThread.quitSafely();
        }
        mServiceHandler.removeCallbacksAndMessages(null);
    }

    private static class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == Constant.MSG_WHAT_CLIENT) {
                Bundle data = msg.getData();
                data.setClassLoader(User.class.getClassLoader());
                Log.d(TAG, "handleMessage: " + data.getString(Constant.MSG_TITLE_CLIENT));
                Log.d(TAG, "handleMessage: " + data.getParcelable(Constant.MSG_TITLE_USER));
                Messenger clientMessenger = msg.replyTo;
                Bundle serviceData = new Bundle();
                serviceData.putString(Constant.MSG_TITLE_SERVICE, Constant.MSG_CONTENT_SERVICE);
                serviceData.putParcelable(Constant.MSG_TITLE_BOOK, new Book("978-7-121-26939-4", "Android 开发艺术探索"));
                Message serviceMsg = Message.obtain();
                serviceMsg.what = Constant.MSG_WHAT_SERVICE;
                serviceMsg.setData(serviceData);
                try {
                    clientMessenger.send(serviceMsg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                super.handleMessage(msg);
            }
        }
    }
}