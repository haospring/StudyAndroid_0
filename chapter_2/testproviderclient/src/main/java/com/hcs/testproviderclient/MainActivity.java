package com.hcs.testproviderclient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.hcs.testprovider.provider.BookProvider;
import com.hcs.testproviderclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "haospring MainActivity";

    private ActivityMainBinding mBinding;
    private ContentResolver mBookResolver;
    private Uri currentUri;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBookResolver = getContentResolver();

        mBinding.btnQuery.setOnClickListener(this);
        mBinding.btnInsert.setOnClickListener(this);
        mBinding.btnDelete.setOnClickListener(this);
        mBinding.btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == mBinding.btnQuery.getId()) {
            String[] projection = {
                    BookProvider.Book.BOOK_ID,
                    BookProvider.Book.BOOK_ISBN,
                    BookProvider.Book.BOOK_NAME,
                    BookProvider.Book.BOOK_PRICE,
                    BookProvider.Book.BOOK_AUTHOR
            };
            String selection = "book_name = ?";
            String[] selectionArgs = {"《Android 进阶之光》"};
            String sortOrder = "id desc";
            Cursor cursor = mBookResolver.query(BookProvider.Book.BOOK_URI, projection, selection, selectionArgs, sortOrder);
            while (cursor.moveToNext()) {
                Log.d(TAG, "query book: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                Log.d(TAG, "query book: " + cursor.getString(cursor.getColumnIndexOrThrow("book_isbn")));
                Log.d(TAG, "query book: " + cursor.getString(cursor.getColumnIndexOrThrow("book_name")));
                Log.d(TAG, "query book: " + cursor.getDouble(cursor.getColumnIndexOrThrow("book_price")));
                Log.d(TAG, "query book: " + cursor.getString(cursor.getColumnIndexOrThrow("book_author")));
            }
            cursor.close();
            Log.d(TAG, "==================================");

            sortOrder = "id asc";
            cursor = mBookResolver.query(BookProvider.User.USER_URI, null, null, null, sortOrder);
            while (cursor.moveToNext()) {
                Log.d(TAG, "query user id: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                Log.d(TAG, "query user user_name: " + cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
                Log.d(TAG, "query user user_gender: " + cursor.getInt(cursor.getColumnIndexOrThrow("user_gender")));
                Log.d(TAG, "query user user_desc: " + cursor.getString(cursor.getColumnIndexOrThrow("user_desc")));
            }
            cursor.close();
            Log.d(TAG, "==================================");

            if (currentUri != null) {
                cursor = mBookResolver.query(currentUri, null, null, null, null);
                while (cursor.moveToNext()) {
                    if ("book".equals(currentUri.getPathSegments().get(0))) {
                        Log.d(TAG, "current: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        Log.d(TAG, "current: " + cursor.getString(cursor.getColumnIndexOrThrow("book_isbn")));
                        Log.d(TAG, "current: " + cursor.getString(cursor.getColumnIndexOrThrow("book_name")));
                        Log.d(TAG, "current: " + cursor.getDouble(cursor.getColumnIndexOrThrow("book_price")));
                        Log.d(TAG, "current: " + cursor.getString(cursor.getColumnIndexOrThrow("book_author")));
                    } else if ("user".equals(currentUri.getPathSegments().get(1))) {
                        Log.d(TAG, "query user id: " + cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                        Log.d(TAG, "query user user_name: " + cursor.getString(cursor.getColumnIndexOrThrow("user_name")));
                        Log.d(TAG, "query user user_gender: " + cursor.getInt(cursor.getColumnIndexOrThrow("user_gender")));
                        Log.d(TAG, "query user user_desc: " + cursor.getString(cursor.getColumnIndexOrThrow("user_desc")));
                    }
                }
                cursor.close();
            }

            Log.d(TAG, "book mimeType: " + mBookResolver.getType(BookProvider.Book.BOOK_URI));
            Log.d(TAG, "user mimeType: " + mBookResolver.getType(BookProvider.User.USER_URI));
        } else if (viewId == mBinding.btnInsert.getId()) {
            ContentValues values = new ContentValues();

            values.put("user_name", "任玉刚");
            values.put("user_gender", "1");
            values.put("user_desc", "《Android 开发艺术探索》作者");
            currentUri = mBookResolver.insert(BookProvider.User.USER_URI, values);
            Log.d(TAG, "insert newUri: " + currentUri);
            values.clear();

            values.put("user_name", "刘望舒");
            values.put("user_gender", "1");
            values.put("user_desc", "《Android 进阶之光》作者");
            currentUri = mBookResolver.insert(BookProvider.User.USER_URI, values);
            Log.d(TAG, "insert newUri: " + currentUri);
            values.clear();

            // values.put("book_isbn", "978-7-121-26939-4");
            // values.put("book_name", "《Android 开发艺术探索》");
            // values.put("book_price", 79.00);
            // values.put("book_author", "任玉刚");
            values.put("book_isbn", "978-7-121-40549-5");
            values.put("book_name", "《Android 进阶之光》");
            values.put("book_price", 119.00);
            values.put("book_author", "刘望舒");
            currentUri = mBookResolver.insert(BookProvider.Book.BOOK_URI, values);
            Log.d(TAG, "insert newUri: " + currentUri);
        } else if (viewId == mBinding.btnUpdate.getId()) {
            ContentValues values = new ContentValues();
            values.put("book_isbn", "978-7-121-34838-9");
            values.put("book_name", "《Android 进阶解密》");
            values.put("book_price", 199.00);
            values.put("book_author", "刘望舒");
            String where = "id = ?";
            String[] selectionArgs = {"4"};
            mBookResolver.update(BookProvider.Book.BOOK_URI, values, where, selectionArgs);
            if (currentUri != null) {
                mBookResolver.update(currentUri, values, null, null);
            }
        } else if (viewId == mBinding.btnDelete.getId()) {
            mBookResolver.delete(BookProvider.Book.BOOK_URI, "id > ? and id < ?", new String[]{"4", "9"});
        }
    }
}