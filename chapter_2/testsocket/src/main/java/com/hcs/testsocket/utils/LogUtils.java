package com.hcs.testsocket.utils;

import android.app.Application;
import android.text.TextUtils;

import com.hcs.testsocket.R;

import timber.log.Timber;
import timber.log.Timber.Tree;

public class LogUtils {
    private static final String TAG = "TestSocket-";
    private static boolean mIsShowLog;
    private static final int STACK_TRACE_LOG_POSITION = 4;
    private static Application mApplication;

    public static void init(Tree tree, Application application, boolean isShowLog) {
        Timber.plant(tree);
        LogUtils.mIsShowLog = isShowLog;
        LogUtils.mApplication = application;
    }

    public static boolean isShowLog() {
        return mIsShowLog;
    }

    public static void setShowLog(boolean mIsShowLog) {
        LogUtils.mIsShowLog = mIsShowLog;
    }

    public static void logV(Object tag, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).v(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).v(message, args);
        }
    }

    public static void logD(Object tag, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).d(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).d(message, args);
        }
    }

    public static void logI(Object tag, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).i(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).i(message, args);
        }
    }

    public static void logW(Object tag, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).w(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).w(message, args);
        }
    }

    public static void logE(Object tag, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).e(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).e(message, args);
        }
    }

    public static void logE(Object tag, Throwable throwable, String message, Object... args) {
        if (!isShowLog()) {
            return;
        }

        if (TextUtils.isEmpty(message) && mApplication != null) {
            message = mApplication.getResources().getString(R.string.log_output_info_is_null);
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).e(message);
        } else {
            Timber.tag(TAG + getTag(tag) + getTargetStackTraceElement()).e(throwable, message, args);
        }
    }

    private static String getTag(Object obj) {
        String tag;
        if (obj instanceof String) {
            tag = (String) obj;
        } else if (obj instanceof Class) {
            tag = ((Class<?>) obj).getSimpleName();
        } else {
            tag = obj.getClass().getSimpleName();
        }
        return tag;
    }

    private static String getTargetStackTraceElement() {
        StackTraceElement targetStackTraceElement = null;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        if (stackTraceElements.length > 0) {
            targetStackTraceElement = stackTraceElements[STACK_TRACE_LOG_POSITION];
        }

        String stackTraceName = "";
        if (targetStackTraceElement != null) {
            stackTraceName = "(" + targetStackTraceElement.getFileName() + ":"
                    + targetStackTraceElement.getLineNumber() + ")";
        }
        return stackTraceName;
    }
}
