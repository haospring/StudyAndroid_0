package com.hcs.testsocket.viewmodel;

import androidx.lifecycle.MutableLiveData;

public class MainActivityModel {
    private static volatile MainActivityModel sInstance;
    private final MutableLiveData<Boolean> isServer = new MutableLiveData<>();

    private MainActivityModel() {
    }

    public static MainActivityModel getInstance() {
        if (sInstance == null) {
            synchronized (MainActivityModel.class) {
                if (sInstance == null) {
                    sInstance = new MainActivityModel();
                }
            }
        }
        return sInstance;
    }

    public MutableLiveData<Boolean> getIsServer() {
        return isServer;
    }
}
