package com.example.pw_12;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private static final Uri CONTENT_URI = Uri.parse("content://com.example.pw_12.provider/movies");
    private static final String TAG = "MyContentProvider";

    @Override
    public boolean onCreate() {
        DBHelper dbHelper = new DBHelper(getContext());
        db = dbHelper.getWritableDatabase();
        return (db != null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "Query request received.");
        Cursor cursor = db.query("movies", projection, selection, selectionArgs, null, null, sortOrder);
        if (cursor != null) {
            Log.d(TAG, "Query returned " + cursor.getCount() + " rows.");
        } else {
            Log.d(TAG, "Query returned null.");
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "Insert request received with values: " + values.toString());
        long rowID = db.insert("movies", "", values);
        Uri insertedUri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(insertedUri, null);
        Log.d(TAG, "New row inserted at URI: " + insertedUri.toString());
        return insertedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = db.delete("movies", selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = db.update("movies", values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.example.movies";
    }
}
