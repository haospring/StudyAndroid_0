package com.hcs.testlaunchmode;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testlaunchmode.databinding.ActivityFourthBinding;

public class FourthActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityFourthBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFourthBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());


    }

    @Override
    public void onClick(View v) {

    }
}