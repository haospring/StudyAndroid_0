package com.hcs.testbinderpool.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;

import com.hcs.testbinderpool.IBinderPool;
import com.hcs.testbinderpool.impl.ComputeImpl;
import com.hcs.testbinderpool.impl.SecurityCenterImpl;
import com.hcs.testbinderpool.utils.Constants;

public class BinderPoolService extends Service {
    private final Binder mBinderPool = new BinderPoolImpl();

    public BinderPoolService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static class BinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int binderCode) throws RemoteException {
            IBinder binder = null;
            switch (binderCode) {
                case Constants.BINDER_CODE_SECURITY:
                    binder = new SecurityCenterImpl();
                    break;
                case Constants.BINDER_CODE_COMPUTE:
                    binder = new ComputeImpl();
                    break;
                default:
            }
            return binder;
        }
    }
}