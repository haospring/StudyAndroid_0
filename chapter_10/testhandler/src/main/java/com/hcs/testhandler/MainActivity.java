package com.hcs.testhandler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hcs.testhandler.databinding.ActivityMainBinding;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private WeakReference<Handler> mWeakHandler;
    private final MyHandler mHandler = new MyHandler(getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mWeakHandler = new WeakReference<>(mHandler);
        mWeakHandler.get().post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private static class MyHandler extends Handler {
        public MyHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWeakHandler != null && mWeakHandler.get() != null) {
            mWeakHandler.get().removeCallbacksAndMessages(null);
            mWeakHandler = null;
        }
    }
}