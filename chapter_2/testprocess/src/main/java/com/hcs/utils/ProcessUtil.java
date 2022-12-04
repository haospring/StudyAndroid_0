package com.hcs.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProcessUtil {
    public static String getProcessName(Context context) {
        String processName;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            processName = Application.getProcessName();
        } else {
            processName = getProcessNameByAt(context);
        }
        return processName;
    }

    private static String getProcessNameByAt(Context context) {
        String processName;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Method currentProcessName = activityThread.getDeclaredMethod("currentProcessName");
            currentProcessName.setAccessible(true);
            processName = (String) currentProcessName.invoke(null);
        } catch (NoSuchMethodException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
            processName = getProcessNameByAm(context);
        }
        return processName;
    }

    private static String getProcessNameByAm(Context context) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : runningAppProcesses) {
            if (info.pid == android.os.Process.myPid()) {
                processName = info.processName;
            }
        }
        return processName;
    }
}
