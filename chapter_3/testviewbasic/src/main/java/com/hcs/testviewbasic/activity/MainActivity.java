package com.hcs.testviewbasic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.hcs.testviewbasic.R;
import com.hcs.testviewbasic.databinding.ActivityMainBinding;
import com.hcs.testviewbasic.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private String mBtn5Text = "0";
    private int mLastBtn5Visibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        LogUtils.logD(TAG, "touchSlop = %d", ViewConfiguration.get(this).getScaledTouchSlop());
        LogUtils.logD(TAG, "density = %f", getResources().getDisplayMetrics().density);

        mBinding.tvv1.setOnClickListener(this);
        // 事件拦截体系中，setOnTouchListener的优先级最高，onTouch返回false，View的onTouchEvent才会被调用，
        // 返回true，则onTouchEvent不会被调用
        mBinding.tvv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            LogUtils.logD(TAG, "versionName = %s", versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mBinding.tb4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.logD(TAG, "test button onclick");
            }
        });

        mBinding.tb5.setOnClickListener(this);

        // 测试监听view的可见性
        mBinding.tb6.setOnClickListener(this);

        View childAt = ((ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0);
        LogUtils.logD(TAG, "childAt = " + childAt);
        LogUtils.logD(TAG, "mBinding.getRoot() = " + mBinding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();
        ViewTreeObserver viewTreeObserver = mBinding.tb5.getViewTreeObserver();
        viewTreeObserver.addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {
                LogUtils.logD(TAG, "onWindowAttached");
            }

            @Override
            public void onWindowDetached() {
                LogUtils.logD(TAG, "onWindowDetached");
            }
        });
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LogUtils.logD(TAG, "tb5 visibility = " + mBinding.tb5.getVisibility());
                if (mBinding.tb5.getVisibility() == View.VISIBLE && mLastBtn5Visibility != View.VISIBLE) {
                    mBinding.tb5.setText(mBtn5Text);
                    LogUtils.logD(TAG, "btn5Text#2 = " + mBinding.tb5.getText());
                }
                mLastBtn5Visibility = mBinding.tb5.getVisibility();
            }
        });

        new Thread(() -> {
            int i = 0;
            while (i < 100) {
                mBtn5Text = i + "";
                i++;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.tvv1.getId()) {
//            mBinding.tvv1.smoothScrollTo(-350, -350);
        } else if (v.getId() == mBinding.tb5.getId()) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
        } else if (v.getId() == mBinding.tb6.getId()) {
            if (mBinding.tb5.getVisibility() == View.VISIBLE) {
                mBinding.tb5.setVisibility(View.GONE);
            } else {
                mBinding.tb5.setVisibility(View.VISIBLE);
            }
        }
    }
}