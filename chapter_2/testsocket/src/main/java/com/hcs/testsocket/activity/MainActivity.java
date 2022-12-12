package com.hcs.testsocket.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.hcs.testsocket.R;
import com.hcs.testsocket.utils.LogUtils;

import java.util.ArrayList;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogUtils.logV(this, "");
        LogUtils.logV(MainActivity.class, "test %s", "haospring");
        LogUtils.logV(TAG, "test %d", 1);
    }
}