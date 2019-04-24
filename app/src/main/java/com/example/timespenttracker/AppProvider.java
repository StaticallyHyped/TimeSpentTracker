package com.example.timespenttracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by StaticallyHyped
 * Provider for the TimeSpentTracker app. This is the only class that knows about {@link AppDatabase}
 */

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.example.timespenttracker.provider";
    public static final Uri CONTENT_AUTHORITY_URI =  Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final int TASKS = 100;
    public static final int TASKS_ID = 101;
    public static final int TIMINGS = 200;
    public static final int TIMINGS_ID = 201;

//    private static final int TASK_TIMINGS = 400;
//    private static final int TASK_TIMINGS_ID = 401;


    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,  String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
