package com.hcs.testlaunchmode;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testlaunchmode.databinding.ActivitySingleInstanceBinding;

public class SingleInstanceActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivitySingleInstanceBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySingleInstanceBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.btnSingleInstance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(getPackageName(), FirstActivity.class.getName()));
        startActivity(intent);
    }
}