package com.hcs.testviewbasic.utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hcs.testviewbasic.R;

import timber.log.Timber;

public class LogUtils {
    private static final String TAG = "TestViewBasic-";
    private static final int STACK_TRACE_LOG_POSITION = 5;

    private static Application mApplication;
    private static boolean mIsShowLog;

    private static final int LOG_V = Log.VERBOSE;
    private static final int LOG_D = Log.DEBUG;
    private static final int LOG_I = Log.INFO;
    private static final int LOG_W = Log.WARN;
    private static final int LOG_E = Log.ERROR;


    public static void init(boolean isShowLog, Context context, Timber.Tree tree) {
        mIsShowLog = isShowLog;
        mApplication = (Application) context.getApplicationContext();
        Timber.plant(tree);
    }

    public static void logV(Object tag, String message, Object... args) {
        outPutLog(LOG_V, tag, null, message, args);
    }

    public static void logD(Object tag, String message, Object... args) {
        outPutLog(LOG_D, tag, null, message, args);
    }

    public static void logI(Object tag, String message, Object... args) {
        outPutLog(LOG_I, tag, null, message, args);
    }

    public static void logW(Object tag, String message, Object... args) {
        outPutLog(LOG_W, tag, null, message, args);
    }

    public static void logE(Object tag, String message, Object... args) {
        outPutLog(LOG_E, tag, null, message, args);
    }

    public static void logE(Object tag, String message, Throwable throwable, Object... args) {
        outPutLog(LOG_E, tag, throwable, message, args);
    }

    private static String getTag(Object obj) {
        String tag;
        if (obj instanceof String) {
            tag = (String) obj;
        } else if (obj instanceof Class<?>) {
            tag = ((Class<?>) obj).getSimpleName();
        } else {
            tag = obj.getClass().getSimpleName();
        }
        return tag;
    }

    private static String getTargetElementName() {
        StackTraceElement targetElement = null;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length > 0) {
            targetElement = stackTraceElements[STACK_TRACE_LOG_POSITION];
        }

        String targetElementName = null;
        if (targetElement != null) {
            targetElementName = targetElement.getFileName();
            targetElementName = "(" + targetElementName + ":" + targetElement.getLineNumber() + ")";
        }
        return targetElementName;
    }

    private static void outPutLog(int priority, Object obj, Throwable throwable, String message, Object... args) {
        if (!mIsShowLog) {
            return;
        }

        if (TextUtils.isEmpty(message)) {
            message = mApplication.getResources().getString(R.string.warn_log_empty);
        }

        String tag = TAG + getTag(obj) + getTargetElementName();
        switch (priority) {
            case LOG_V:
                Timber.tag(tag).v(message, args);
                break;
            case LOG_D:
                Timber.tag(tag).d(message, args);
                break;
            case LOG_I:
                Timber.tag(tag).w(message, args);
                break;
            case LOG_W:
                Timber.tag(tag).i(message, args);
                break;
            case LOG_E:
                if (throwable == null) {
                    Timber.tag(tag).e(message, args);
                } else {
                    Timber.tag(tag).e(throwable, message, args);
                }
                break;
            default:
        }
    }
}
