package com.hcs.testviewbasic.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.hcs.testviewbasic.utils.LogUtils;

public class HorizontalScrollViewEx extends LinearLayout {
    private static final String TAG = HorizontalScrollViewEx.class.getSimpleName();

    private final Scroller mScroller;
    private final VelocityTracker mVelocityTracker;

    private int mLastX;
    private int mLastY;
    private int mLastXIntercept;
    private int mLastYIntercept;

    private int mChildWidth = getResources().getDisplayMetrics().widthPixels;
    private int mChildSize = 2;
    private int mChildIndex = 0;

    private final int mScaledTouchSlop;

    public HorizontalScrollViewEx(Context context) {
        this(context, null);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizontalScrollViewEx(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 3;
        LogUtils.logD(TAG, "mScaledTouchSlop = " + mScaledTouchSlop);
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    // 外部拦截法
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtils.logD(TAG, "onInterceptTouchEvent action = " + ev.getAction());
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
        }

        mLastX = x;
        mLastY = y;
        mLastXIntercept = x;
        mLastYIntercept = y;
        return intercepted;
    }


// 内部拦截法
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            return false;
//        } else {
//            return true;
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtils.logD(TAG, "action = " + event.getAction());

        mVelocityTracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                if (deltaX > mScaledTouchSlop) {
                    scrollBy(-deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity) >= 50) {
                    mChildIndex = xVelocity > 0 ? mChildIndex - 1 : mChildIndex + 1;
                } else {
                    mChildIndex = (getScrollX() + mChildWidth / 2) / mChildWidth;
                }
                mChildIndex = Math.max(0, Math.min(mChildIndex, mChildSize - 1));
                int dx = mChildIndex * mChildWidth - getScrollX();
                smoothScrollBy(dx, 0);
                mVelocityTracker.clear();
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(getScrollX(), 0, dx, dy);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
