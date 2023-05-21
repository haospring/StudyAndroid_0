package com.hcs.testviewbasic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewConfiguration;

import com.hcs.testviewbasic.R;
import com.hcs.testviewbasic.databinding.ActivityMainBinding;
import com.hcs.testviewbasic.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;

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
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.tvv1.getId()) {
//            mBinding.tvv1.smoothScrollTo(-350, -350);
        }
    }
}