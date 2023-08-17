package com.hcs.testwindow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        mPanelView = LayoutInflater.from(this).inflate(R.layout.layout_panel, null);

        mBinding.btnJump.setOnClickListener(this);
        mPanelView.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                default:
            }
            return true;
        });
    }

    private void updateParams(int y, int downY) {
        int dy = y - downY;
        if (dy > 50) {
            mParams.y = mParams.y - dy;
            Log.d(TAG, "updateParams: " + mParams.y + " dy = " + dy);
            mWindowManager.updateViewLayout(mPanelView, mParams);
        }
    }

    private void initWindow() {
        mWindowManager = getWindowManager();
        mParams = new LayoutParams();
        mParams.width = LayoutParams.MATCH_PARENT;
        mParams.height = LayoutParams.MATCH_PARENT;
        mParams.type = LayoutParams.TYPE_APPLICATION;
        mParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_FULLSCREEN | LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | LayoutParams.FLAG_LAYOUT_NO_LIMITS | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.x = 0;
        mParams.y = 0;
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
    }
}