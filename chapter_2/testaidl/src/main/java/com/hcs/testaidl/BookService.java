package com.hcs.testaidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.hcs.testaidl.dao.Book;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BookService extends Service {
    private static final String TAG = "haospring BookService";
    private final CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();
    private final RemoteCallbackList<IOnNewBookListener> mCallbackList = new RemoteCallbackList<>();

    private final IBookManager mBookManager = new IBookManager.Stub() {
        @Override
        public List<Book> getBooks() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            int callbackCount = mCallbackList.beginBroadcast();
            for (int i = 0; i < callbackCount; i++) {
                mCallbackList.getBroadcastItem(i).onNewBookArrived(book);
            }
            mCallbackList.finishBroadcast();
        }

        @Override
        public void register(IOnNewBookListener listener) throws RemoteException {
            Log.d(TAG, "register");
            mCallbackList.register(listener);
        }

        @Override
        public void unregister(IOnNewBookListener listener) throws RemoteException {
            Log.d(TAG, "unregister");
            mCallbackList.unregister(listener);
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            // 等价于 checkPermission，只能在 IPC 的过程中调用，在 onBind 中调用会一直返回 -1
            int check = checkCallingPermission("com.hcs.testaidl.permission.ACCESS_BOOK_SERVICE");
            if (check == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            int checkPermission = checkPermission("com.hcs.testaidl.permission.ACCESS_BOOK_SERVICE", getCallingPid(), getCallingUid());
            if (checkPermission == PackageManager.PERMISSION_DENIED) {
                return false;
            }

            String packageName = "";
            String[] packages = getPackageManager().getPackagesForUid(getCallingUid());
            if (packages != null && packages.length > 0) {
                packageName = packages[0];
            }
            if (!packageName.startsWith("com.hcs")) {
                return false;
            }

            return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book(1, "Android 开发艺术探索"));
        mBookList.add(new Book(2, "Android 进阶之光"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBookManager.asBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}