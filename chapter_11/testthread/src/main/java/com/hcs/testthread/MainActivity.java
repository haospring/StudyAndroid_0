package com.hcs.testthread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;

import com.hcs.testthread.databinding.ActivityMainBinding;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding mBinding;
    private TestTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initClickListener();

        mTask = new TestTask();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void initClickListener() {
        mBinding.btnLoading.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBinding.btnLoading) {
            mTask.execute("正在加载中...");
        } else if (v == mBinding.btnCancel) {
            mTask.cancel(true);
        }
    }

    private class TestTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "onProgressUpdate: " + Arrays.toString(values));
            if (values.length > 0) {
                mBinding.progressBar.setProgress(values[0]);
            }
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
            Log.d(TAG, "onCancelled: " + s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "onCancelled: ");
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: " + Arrays.toString(strings));

            int progress = 0;
            while (progress <= 100) {
                publishProgress(progress);
                progress++;
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            return "" + progress;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
            mTask = null;
        }
    }
}