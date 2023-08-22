package com.hcs.testviewbasic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewEx extends ListView {
    private int mLastX;
    private int mLastY;

    public ListViewEx(Context context) {
        this(context, null);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastX;
                int dy = y - mLastY;
                if (Math.abs(dx) > Math.abs(dy)) {
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
        }

        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }
}
