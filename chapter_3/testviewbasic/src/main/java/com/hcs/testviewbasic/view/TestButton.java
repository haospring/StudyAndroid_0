package com.hcs.testviewbasic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import com.hcs.testviewbasic.utils.LogUtils;

public class TestButton extends Button {
    private static final String TAG = "TestButton";

    public TestButton(Context context) {
        super(context);
    }

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.logD(TAG, "onTouchEvent action = %d", event.getAction());
        return super.onTouchEvent(event);
    }
}
