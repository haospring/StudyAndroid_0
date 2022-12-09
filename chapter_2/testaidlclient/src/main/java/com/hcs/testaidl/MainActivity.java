package com.hcs.testaidl;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.hcs.testaidl.dao.Book;
import com.hcs.testaidl.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "haospring";
    private static final String ACTION_BOOK_SERVICE = "com.hcs.testaidlservice.BookService";
    private static final String PKG_BOOK_SERVICE = "com.hcs.testaidl";

    private ActivityMainBinding mBinding;
    private IBookManager mBookManager;
    private IOnNewBookListener mListener;

    private int mBookId = 3;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service != null) {
                mBookManager = IBookManager.Stub.asInterface(service);
                try {
                    mBookManager.register(mListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        initData();

        mBinding.btnBind.setOnClickListener(this);
        mBinding.btnQuery.setOnClickListener(this);
        mBinding.btnAdd.setOnClickListener(this);
    }

    private void initData() {
        mListener = new IOnNewBookListener.Stub() {
            @Override
            public void onNewBookArrived(com.hcs.testaidl.dao.Book newBook) throws RemoteException {
                Log.d(TAG, "onNewBookArrived: " + newBook);
            }
        };
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == mBinding.btnBind.getId()) {
            Intent intent = new Intent();
            intent.setPackage(PKG_BOOK_SERVICE);
            intent.setAction(ACTION_BOOK_SERVICE);
            try {
                bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        } else if (viewId == mBinding.btnQuery.getId()) {
            if (mBookManager != null) {
                try {
                    Log.d(TAG, "onClick books: " + mBookManager.getBooks());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if (viewId == mBinding.btnAdd.getId()) {
            if (mBookManager != null) {
                try {
                    mBookManager.addBook(new Book(mBookId++, "Android 进阶解密"));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            try {
                mBookManager.unregister(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}