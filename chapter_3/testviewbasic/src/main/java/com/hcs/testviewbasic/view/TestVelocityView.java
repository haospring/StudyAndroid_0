package com.hcs.testviewbasic.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hcs.testviewbasic.utils.LogUtils;

public class TestVelocityView extends TextView implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private static final String TAG = TestVelocityView.class.getSimpleName();
    private final GestureDetector mGestureDetector;
    private final Scroller mScroller;
    private int mScrollX;
    private int mScrollY;
    private int mLastX;
    private int mLastY;

    public TestVelocityView(Context context) {
        this(context, null);
    }

    public TestVelocityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestVelocityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TestVelocityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mGestureDetector = new GestureDetector(getContext(), this);
        mGestureDetector.setOnDoubleTapListener(this);
//        mGestureDetector.setIsLongpressEnabled(false);

        mScroller = new Scroller(getContext());
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        velocityTracker.addMovement(event);
        velocityTracker.computeCurrentVelocity(100);
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();
        LogUtils.logD(TAG, "xVelocity = %f", xVelocity);
        LogUtils.logD(TAG, "yVelocity = %f", yVelocity);
        velocityTracker.clear();
        velocityTracker.recycle();

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                setTranslationX(getTranslationX() + deltaX);
                setTranslationY(getTranslationY() + deltaY);
                break;
            default:
        }

        mLastX = x;
        mLastY = y;

        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void smoothScrollTo(int destX, int destY) {
        int startX = getScrollX();
        int startY = getScrollY();
        int deltaX = destX - startX;
        int deltaY = destY - startY;
        LogUtils.logD(TAG, "startX = " + startX);
        LogUtils.logD(TAG, "startY = " + startY);
        LogUtils.logD(TAG, "deltaX = " + deltaX);
        LogUtils.logD(TAG, "deltaY = " + deltaY);
        mScroller.startScroll(startX, startY, deltaX, deltaY, 1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            super.computeScroll();
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        LogUtils.logD(TAG, "onDown");
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        LogUtils.logD(TAG, "onShowPress");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LogUtils.logD(TAG, "onSingleTapUp");
        return performClick();
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        LogUtils.logD(TAG, "onScroll");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        LogUtils.logD(TAG, "onLongPress");
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LogUtils.logD(TAG, "onFling");
        return true;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mScrollX = (e.getX() >= (float) getWidth() / 2) ? -(int) (e.getX() - getWidth() / 2) : (int) (getWidth() / 2 - e.getX());
        mScrollY = (e.getY() >= (float) getHeight() / 2) ? -(int) (e.getY() - getHeight() / 2) : (int) (getHeight() / 2 - e.getY());
        LogUtils.logD(TAG, "e.getX() = " + e.getX());
        LogUtils.logD(TAG, "e.getY() = " + e.getY());
        LogUtils.logD(TAG, "e.getWidth() = " + getWidth());
        LogUtils.logD(TAG, "e.getHeight() = " + getHeight());
        LogUtils.logD(TAG, "mScrollX = " + mScrollX);
        LogUtils.logD(TAG, "mScrollY = " + mScrollY);

        smoothScrollTo(mScrollX, mScrollY);
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        LogUtils.logD(TAG, "onDoubleTap");
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        LogUtils.logD(TAG, "onDoubleTapEvent");
        return true;
    }
}
