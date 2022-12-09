package com.hcs.testproviderclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hcs.testproviderclient.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "haospring MainActivity";

    private ActivityMainBinding mBinding;
    private ContentResolver mBookResolver;

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
            String uri = "content://com.hcs.testprovider.book.provider";
            Cursor cursor = mBookResolver.query(Uri.parse(uri), null, null, null, null);
            Log.d(TAG, "query: " + cursor);
//            while (cursor.moveToNext()) {
//
//            }
//            cursor.close();
        } else if (viewId == mBinding.btnInsert.getId()) {

        } else if (viewId == mBinding.btnUpdate.getId()) {

        } else if (viewId == mBinding.btnDelete.getId()) {

        }
    }
}