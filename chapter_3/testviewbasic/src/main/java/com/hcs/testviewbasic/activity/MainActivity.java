package com.hcs.testviewbasic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == mBinding.tvv1.getId()) {
//            mBinding.tvv1.smoothScrollTo(-350, -350);
        }
    }
}