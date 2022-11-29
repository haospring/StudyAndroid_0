package com.hcs.testlaunchmode;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testlaunchmode.databinding.ActivityFirstBinding;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityFirstBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFirstBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setTaskDescription(new ActivityManager.TaskDescription("FirstActivity",
                BitmapFactory.decodeResource(getResources(), R.drawable.calendar)));

        mBinding.btnFirst.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction("com.hcs.testlaunchmode.SecondActivity");
        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            startActivity(intent);
        }
    }
}