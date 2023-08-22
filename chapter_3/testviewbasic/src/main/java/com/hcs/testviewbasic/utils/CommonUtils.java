package com.hcs.testviewbasic.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class CommonUtils {
    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics;
        if (context instanceof Activity) {
            displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        } else {
            displayMetrics = context.getResources().getDisplayMetrics();
        }
        return displayMetrics;
    }
}
