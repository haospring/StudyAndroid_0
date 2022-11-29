package com.hcs.testlaunchmode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testlaunchmode.databinding.ActivityThirdBinding;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityThirdBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityThirdBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.btnThird.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction("com.hcs.testlaunchmode.FourthActivity");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}