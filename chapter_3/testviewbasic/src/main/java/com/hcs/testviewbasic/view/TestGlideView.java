package com.hcs.testviewbasic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.hcs.testviewbasic.utils.LogUtils;

public class TestGlideView extends ImageView {
    private int mLastX;
    private int mLastY;

    public TestGlideView(Context context) {
        this(context, null);
    }

    public TestGlideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestGlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TestGlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                setTranslationX(x - mLastX + getTranslationX());
                setTranslationY(y - mLastY + getTranslationY());
                break;
            default:
        }
        mLastX = x;
        mLastY = y;

        return super.onTouchEvent(event);
    }
}
