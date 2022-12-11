package com.hcs.testprovider.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.hcs.testprovider.repository.DbOpenHelper;

import java.net.URI;
import java.util.Arrays;

public class BookProvider extends ContentProvider {
    private static final String TAG = "haospring BookProvider";
    public static final String AUTHORITY = "com.hcs.testprovider.book.provider";
    private static final int CODE_BOOK_DIR = 0;
    private static final int CODE_USER_DIR = 1;
    private static final int CODE_BOOK_ITEM = 2;
    private static final int CODE_USER_ITEM = 3;
    private static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase mDb;
    private Context mContext;

    static {
        sUriMatcher.addURI(AUTHORITY, "book", CODE_BOOK_DIR);
        sUriMatcher.addURI(AUTHORITY, "user", CODE_USER_DIR);
        sUriMatcher.addURI(AUTHORITY, "book/#", CODE_BOOK_ITEM);
        sUriMatcher.addURI(AUTHORITY, "user/#", CODE_USER_ITEM);
    }

    public BookProvider() {
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: " + Thread.currentThread().getName());
        mContext = getContext();
        DbOpenHelper dbOpenHelper = new DbOpenHelper(mContext, "book_provider.db", null, 3);
        mDb = dbOpenHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: " + Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (uri.getPathSegments().size() > 1) {
            selectionArgs = new String[]{uri.getPathSegments().get(1)};
            selection = "id = ?";
        }
        return mDb.query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = mDb.insert(table, null, values);
        Uri newUri = Uri.parse("content://" + AUTHORITY + "/" + table + "/" + id);
        return newUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        if (uri.getPathSegments().size() > 1) {
            selection = "id = ?";
            selectionArgs = new String[]{uri.getPathSegments().get(1)};
        }
        Log.d(TAG, "uri: " + uri);
        Log.d(TAG, "update selection: " + selection);
        Log.d(TAG, "update selectionArgs: " + Arrays.toString(selectionArgs));
        return mDb.update(table, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        if (uri.getPathSegments().size() > 1) {
            selection = "id = ?";
            selectionArgs = new String[]{uri.getPathSegments().get(1)};
        }
        Log.d(TAG, "delete selection: " + selection);
        Log.d(TAG, "delete selectionArgs: " + Arrays.toString(selectionArgs));
        return mDb.delete(table, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        String type = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_BOOK_DIR:
                type = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".book";
                break;
            case CODE_BOOK_ITEM:
                type = "vnd.android.cursor.item/vnd." + AUTHORITY + ".book";
                break;
            case CODE_USER_DIR:
                type = "vnd.android.cursor.dir/vnd." + AUTHORITY + ".user";
                break;
            case CODE_USER_ITEM:
                type = "vnd.android.cursor.item/vnd." + AUTHORITY + ".user";
                break;
            default:
        }
        return type;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case CODE_BOOK_DIR:
            case CODE_BOOK_ITEM:
                tableName = "book";
                break;
            case CODE_USER_DIR:
            case CODE_USER_ITEM:
                tableName = "user";
                break;
            default:
        }
        return tableName;
    }

    public static class Book {
        public static final Uri BOOK_URI = Uri.parse("content://" + AUTHORITY + "/book");
        public static final String BOOK_ID = "id";
        public static final String BOOK_ISBN = "book_isbn";
        public static final String BOOK_NAME = "book_name";
        public static final String BOOK_PRICE = "book_price";
        public static final String BOOK_AUTHOR = "book_author";
    }

    public static class User {
        public static final Uri USER_URI = Uri.parse("content://" + AUTHORITY + "/user");
        public static final String USER_ID = "id";
        public static final String USER_NAME = "user_name";
        public static final String USER_GENDER = "user_gender";
        public static final String USER_DESCRIPTION = "user_description";
    }
}