package com.hcs.testbinderpool.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.hcs.testbinderpool.IBinderPool;
import com.hcs.testbinderpool.utils.Constants;

import java.util.concurrent.CountDownLatch;

public class BinderPoolManager {
    private final Context mContext;
    private IBinderPool mBinderPool;
    private static volatile BinderPoolManager sInstance;
    private CountDownLatch mConnectionBinderPoolCountDownLatch;

    private final ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mBinderPoolDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectionBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private final IBinder.DeathRecipient mBinderPoolDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mBinderPoolDeathRecipient, 0);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    private synchronized void connectBinderPoolService() {
        mConnectionBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent service = new Intent();
        service.setPackage(mContext.getPackageName());
        service.setAction(Constants.ACTION_BINDER_POOL);
        mContext.bindService(service, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectionBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private BinderPoolManager(Context context) {
        mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    public static BinderPoolManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (BinderPoolManager.class) {
                sInstance = new BinderPoolManager(context);
            }
        }
        return sInstance;
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            binder = mBinderPool.queryBinder(binderCode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }
}
