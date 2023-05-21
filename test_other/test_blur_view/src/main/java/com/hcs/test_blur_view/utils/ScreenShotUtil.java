package com.hcs.test_blur_view.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Picture;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScreenShotUtil {
    private static final String TAG = "ScreenShotUtil";

    /**
     * 获取截屏图片
     * @param context
     * @return
     */
    public static Bitmap takeScreenShot(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
         ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRealMetrics(displayMetrics);
        Rect rect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        int width = rect.width();
        int height = rect.height();

        Bitmap bitmap = null;
        try {
            Class<?> surfaceControl = Class.forName("android.view.SurfaceControl");
            Method screenshot = surfaceControl.getDeclaredMethod("screenshot", Rect.class, int.class, int.class, int.class);
            bitmap = (Bitmap) screenshot.invoke(surfaceControl, rect, width, height, Surface.ROTATION_0);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            Log.d(TAG, "takeScreenShot bitmap is null");
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), new Matrix(), false);
//        bitmap.recycle();
        return newBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static Bitmap convertHardWareBitmap(Bitmap src) {
        if (src.getConfig() != Bitmap.Config.HARDWARE) {
            //return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight());
            return src;
        }
        final int w = src.getWidth();
        final int h = src.getHeight();
        // For hardware bitmaps, use the Picture API to directly create a software bitmap
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(w, h);
        canvas.drawBitmap(src, 0, 0, null);
        picture.endRecording();
        return Bitmap.createBitmap(picture, w, h, Bitmap.Config.ARGB_8888);
    }
}