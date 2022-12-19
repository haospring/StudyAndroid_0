package com.hcs.testviewbasic;

import android.app.Application;

import com.hcs.testviewbasic.utils.LogUtils;
import com.hcs.testviewbasic.utils.ProcessUtils;

import timber.log.Timber;

public class ViewApplication extends Application {
    private static final String TAG = ViewApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.init(true, this, new Timber.DebugTree());
        LogUtils.logD(TAG, "Application initialized processName is %s", ProcessUtils.getProcessName(this));
    }
}
