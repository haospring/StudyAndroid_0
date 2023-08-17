package com.hcs.testwindow;

import android.app.Application;
import android.util.Log;

import com.hcs.testwindow.utils.ProcessUtils;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = ProcessUtils.getProcessName(getApplicationContext());
        Log.d(TAG, "onCreate: " + processName);
    }
}
