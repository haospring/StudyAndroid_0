package com.hcs.testprocess;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hcs.testprocess.databinding.ActivityThirdBinding;

public class ThirdActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityThirdBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityThirdBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == mBinding.btnThird.getId()) {
            intent.setAction("com.hcs.testprocess.MainActivity");
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                startActivity(intent);
            }
        }
    }
}