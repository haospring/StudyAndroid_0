package com.hcs.testbinderpool.impl;

import android.os.RemoteException;

import com.hcs.testbinderpool.ICompute;

public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
