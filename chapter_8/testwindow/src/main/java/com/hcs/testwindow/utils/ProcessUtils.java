package com.hcs.testwindow.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProcessUtils {
    private static final String TAG = ProcessUtils.class.getSimpleName();

    public static String getProcessName(@NonNull Context context) {
        String processName = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            processName = Application.getProcessName();
            Log.d(TAG, "getProcessName: " + processName);
        }

        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByClazz();
            Log.d(TAG, "getProcessName: " + processName);
        }

        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByActivityManager(context);
            Log.d(TAG, "getProcessName: " + processName);
        }

        return processName;
    }

    private static String getProcessNameByClazz() {
        String processName = null;
        try {
            @SuppressLint("PrivateApi") Class<?> activityThread = Class.forName("android.app.ActivityThread");
            @SuppressLint("DiscouragedPrivateApi") Method currentProcessName = activityThread.getDeclaredMethod("currentProcessName");
            currentProcessName.setAccessible(true);
            Object invoke = currentProcessName.invoke(activityThread);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return processName;
    }

    private static String getProcessNameByActivityManager(Context context) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            if (Process.myPid() == info.pid) {
                processName = info.processName;
            }
        }
        return processName;
    }
}
