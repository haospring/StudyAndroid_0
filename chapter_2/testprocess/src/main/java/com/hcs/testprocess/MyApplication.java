package com.hcs.testprocess;

import android.app.Application;
import android.util.Log;

import com.hcs.utils.ProcessUtil;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate processName = " + ProcessUtil.getProcessName(this));
    }
}
