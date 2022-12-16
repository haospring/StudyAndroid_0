package com.hcs.testviewbasic.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProcessUtils {
    public static String getProcessName(Context context) {
        String processName = getProcessNameByApplication(context);
        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByClazz(context);
        }
        if (TextUtils.isEmpty(processName)) {
            processName = getProcessNameByAm(context);
        }
        return processName;
    }

    private static String getProcessNameByApplication(Context context) {
        String processName;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            processName = Application.getProcessName();
        } else {
            processName = getProcessNameByClazz(context);
        }
        return processName;
    }

    private static String getProcessNameByClazz(Context context) {
        String processName = null;
        try {
            Class<?> activityThread = Class.forName("android.app.ActivityThread", false, context.getClassLoader());
            Method currentProcessName = activityThread.getDeclaredMethod("currentProcessName");
            Object obj = currentProcessName.invoke(null);
            if (obj instanceof String) {
                processName = (String) obj;
            }
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
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
                break;
            }
        }
        return processName;
    }
}
