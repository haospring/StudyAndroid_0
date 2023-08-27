package com.hcs.testviewbasic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hcs.testviewbasic.databinding.ActivityThirdBinding;

public class ThirdActivity extends AppCompatActivity {
    private ActivityThirdBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityThirdBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }
}