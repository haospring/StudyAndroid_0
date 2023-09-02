package com.hcs.testwindow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.hcs.testwindow.R;
import com.hcs.testwindow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mPanelView;
    private int mStatusBarHeight;
    private int mActionBarHeight;
    private int mScreenHeight;

    private VelocityTracker mVelocityTracker;
    private ValueAnimator mValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initView();
        mVelocityTracker = VelocityTracker.obtain();

        mStatusBarHeight = getStatusBarHeight();
        mActionBarHeight = getActionBarHeight();
        mScreenHeight = getScreenHeight();
        Log.d(TAG, "onCreate: mStatusBarHeight = " + mStatusBarHeight
                + ", mActionBarHeight = " + mActionBarHeight
                + ", mScreenHeight = " + mScreenHeight);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mPanelView = LayoutInflater.from(this).inflate(R.layout.layout_panel, mBinding.getRoot(), false);
        mBinding.btnJump.setOnClickListener(this);
        mPanelView.setOnTouchListener(new View.OnTouchListener() {
            int lastX = 0;
            int lastY = 0;
            int dx = 0;
            int dy = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mVelocityTracker.addMovement(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        mVelocityTracker.computeCurrentVelocity(500);
                        int velocityY = (int) mVelocityTracker.getYVelocity();

                        if (Math.abs(velocityY) > 100) {
                            playAnimation(dy > 0);
                        }

                        mVelocityTracker.clear();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        updateParams(dx, dy);
                        break;
                    default:
                }
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                return true;
            }
        });
    }

    private void playAnimation(boolean direcDown) {
        if (mValueAnimator != null) {
            mValueAnimator.removeAllUpdateListeners();
            mValueAnimator.cancel();
        }
        if (direcDown) {
            mValueAnimator = ValueAnimator.ofInt(mParams.y, 0);
        } else {
            mValueAnimator = ValueAnimator.ofInt(mParams.y, -(mScreenHeight - mStatusBarHeight - mActionBarHeight));
        }
        mValueAnimator.addUpdateListener(animation -> {
            int currY = (int) animation.getAnimatedValue();
            updateParams(currY);
        });
        mValueAnimator.start();
    }

    private void updateParams(int dx, int dy) {
        Log.d(TAG, "updateParams dx = " + dx + ", dy = " + dy);

        int currentY = mParams.y + dy;
        if (currentY > 0 || currentY < -(mScreenHeight - mStatusBarHeight - mActionBarHeight)) {
            return;
        }
        // mParams.x = mParams.x + dx;
        mParams.y = mParams.y + dy;
        mWindowManager.updateViewLayout(mPanelView, mParams);
    }

    private void updateParams(int currY) {
        Log.d(TAG, "updateParams currY = " + currY);
        if (currY > 0 || currY < -(mScreenHeight - mStatusBarHeight - mActionBarHeight)) {
            return;
        }
        mParams.y = currY;
        mWindowManager.updateViewLayout(mPanelView, mParams);
    }

    private void initWindow() {
        mWindowManager = getWindowManager();
        mParams = new LayoutParams();
        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.MATCH_PARENT;
        mParams.type = LayoutParams.TYPE_APPLICATION;
        mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | LayoutParams.FLAG_LAYOUT_NO_LIMITS | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.x = 0;
        mParams.y = -(mScreenHeight - mStatusBarHeight - mActionBarHeight);
    }

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度
     */
    @SuppressLint("DiscouragedApi")
    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    /**
     * 获取ActionBar高度
     *
     * @return ActionBar高度
     */
    private int getActionBarHeight() {
        TypedValue typedValue = new TypedValue();
        boolean hasAttribute = getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true);
        if (hasAttribute) {
            return TypedValue.complexToDimensionPixelSize(typedValue.data, getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    private int getScreenHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.btnJump) {
            if (mWindowManager == null) {
                initWindow();
            }
            if (mPanelView != null) {
                if (mPanelView.isAttachedToWindow()) {
                    mWindowManager.removeView(mPanelView);
                }
                mWindowManager.addView(mPanelView, mParams);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWindowManager != null && mPanelView != null) {
            mWindowManager.removeView(mPanelView);
            mWindowManager = null;
            mPanelView = null;
            mParams = null;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
        }
    }
}