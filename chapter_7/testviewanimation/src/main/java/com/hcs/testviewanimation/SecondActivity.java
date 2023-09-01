package com.hcs.testviewanimation;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testviewanimation.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivitySecondBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initClickListener();
    }

    private void initClickListener() {
        mBinding.btnToFirst.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mBinding.btnToFirst == v) {
            finish();
            overridePendingTransition(R.anim.animation_enter_2_before, R.anim.animation_exit_2_before);
        }
    }
}