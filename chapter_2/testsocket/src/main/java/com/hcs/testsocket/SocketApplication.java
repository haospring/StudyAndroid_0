package com.hcs.testsocket;

import android.app.Application;

import com.hcs.testsocket.utils.LogUtils;

import timber.log.Timber;


public class SocketApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.init(new Timber.DebugTree(), this, true);
    }
}
