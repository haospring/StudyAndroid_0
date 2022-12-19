package com.hcs.testbinderpool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.hcs.testbinderpool.manager.BinderPoolManager;
import com.hcs.testbinderpool.utils.Constants;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 2;
    private static final int QUEUE_CAPACITY = 4;
    private static final int KEEP_ALIVE_TIME = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.MINUTES, new ArrayBlockingQueue<>(QUEUE_CAPACITY), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());

        // 线程池最大容量为6，超出则会抛出异常
        executor.execute(this::doWork);
        executor.execute(this::doWork);
        executor.execute(this::doWork);
        executor.execute(this::doWork);
        executor.execute(this::doWork);
        executor.execute(this::doWork);
    }

    private void doWork() {
        Log.d(TAG, "doWork: " + Thread.currentThread().getName());
        IBinder securityBinder = BinderPoolManager.getInstance(this).queryBinder(Constants.BINDER_CODE_SECURITY);
        IBinder computeBinder = BinderPoolManager.getInstance(this).queryBinder(Constants.BINDER_CODE_COMPUTE);

        ISecurityCenter securityCenter = ISecurityCenter.Stub.asInterface(securityBinder);
        ICompute compute = ICompute.Stub.asInterface(computeBinder);

        try {
            String encrypt = securityCenter.encrypt("haospring");
            Log.d(TAG, "onCreate encrypt = " + encrypt);
            String decrypt = securityCenter.decrypt(encrypt);
            Log.d(TAG, "onCreate decrypt = " + decrypt);

            Log.d(TAG, "onCreate 1 + 3 = " + compute.add(1, 3));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}