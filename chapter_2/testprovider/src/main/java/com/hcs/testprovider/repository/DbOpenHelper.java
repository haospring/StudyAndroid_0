package com.hcs.testprovider.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "haospring DbOpenHelper";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    private static final String CREATE_TABLE_BOOK = "CREATE TABLE IF NOT EXISTS " +
            BOOK_TABLE_NAME +
            " (id integer primary key autoincrement, " +
            "book_isbn TEXT, " +
            "book_name TEXT, " +
            "book_price REAL, " +
            "book_author TEXT)";
    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            USER_TABLE_NAME +
            " (id INTEGER PRIMARY KEY, " +
            "user_gender INTEGER, " +
            "user_name TEXT," +
            "user_desc TEXT)";
    private static final String DROP_TABLE_BOOK = "DROP TABLE IF EXISTS " +
            BOOK_TABLE_NAME;
    private static final String DROP_TABLE_USER = "DROP TABLE IF EXISTS " +
            USER_TABLE_NAME;

    public DbOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "oldVersion: " + oldVersion);
        Log.d(TAG, "newVersion: " + newVersion);
        db.execSQL(DROP_TABLE_BOOK);
        db.execSQL(DROP_TABLE_USER);
        onCreate(db);
    }
}
