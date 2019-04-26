package com.example.timespenttracker;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by StaticallyHyped
 * Provider for the TimeSpentTracker app. This is the only class that knows about {@link AppDatabase}
 */

public class AppProvider extends ContentProvider {
    private static final String TAG = "AppProvider";

    private AppDatabase mOpenHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    static final String CONTENT_AUTHORITY = "com.example.timespenttracker.provider";
    public static final Uri CONTENT_AUTHORITY_URI =  Uri.parse("content://" + CONTENT_AUTHORITY);
    // tasks class
    public static final int TASKS = 100;
    public static final int TASKS_ID = 101;
    // timings class
    private static final int TIMINGS = 200;
    private static final int TIMINGS_ID = 201;
    // durations class

    private static final int TASK_DURATIONS = 400;
    private static final int TASK_DURATIONS_ID = 401;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // eg. content://com.example.timespenttracker.provider/tasks
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME, TASKS);
        //eg content://com.example.timespenttracker.provider/tasks/8
        matcher.addURI(CONTENT_AUTHORITY, TasksContract.TABLE_NAME + "/#", TASKS_ID);

//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME, TIMINGS);
//        matcher.addURI(CONTENT_AUTHORITY, TimingsContract.TABLE_NAME + "/#", TIMINGS_ID);
//
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME, TASK_DURATIONS);
//        matcher.addURI(CONTENT_AUTHORITY, DurationsContract.TABLE_NAME + "/#", TASK_DURATIONS_ID);

        return matcher;
    }
    @Override
    public boolean onCreate() {
        mOpenHelper = AppDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query: called with URI " + uri);
        final int match = sUriMatcher.match(uri);
        Log.d(TAG, "query: match is " + match);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        //using the switch statement to choose different blocks of code, depending on the result of the uri
        //when the uri matches times or tasks or durations, the table is chosen which to query from
        switch(match) {
            case TASKS:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                break;

            case TASKS_ID:
                queryBuilder.setTables(TasksContract.TABLE_NAME);
                long taskId = TasksContract.getTaskId(uri);
                queryBuilder.appendWhere(TasksContract.Columns._ID + " = " + taskId);
                break;

//            case TIMINGS:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                break;

//            case TIMINGS_ID:
//                queryBuilder.setTables(TimingsContract.TABLE_NAME);
//                long timingId = TimingsContract.getTimingId(uri);
//                queryBuilder.appendWhere(TimingsContract.Columns._ID + " = " + timingId);
//                break;
//
//            case TASK_DURATIONS:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                break;

//            case TASK_DURATIONS_ID:
//                queryBuilder.setTables(DurationsContract.TABLE_NAME);
//                long durationId = DurationsContract.getDuration(uri);
//                queryBuilder.appendWhere(DurationsContract.Columns._ID + " = " + durationId);
//                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        return queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "entering inster, called with uri: " + uri);
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;

        Uri returnUri;
        long recordId;

        switch(match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TasksContract.TABLE_NAME, null, values);
                if(recordId >= 0) {
                    returnUri = TasksContract.buildTaskUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());
                }
                break;
            case TIMINGS:
                /*db = mOpenHelper.getWritableDatabase();
                recordId = db.insert(TimingsContract.Timings.buildTimingUri(recordId));
                if(recordId >= 0 ){
                    returnUri = TimingsContract.Timings.buildTimingUri(recordId);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri.toString());

                }
                break;*/
            default:
                    throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TasksContract.TABLE_NAME, selection, selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;

                if((selection != null) && (selection.length() >0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(TasksContract.TABLE_NAME,  selectionCriteria, selectionArgs);
                break;

            /*case TIMINGS:
                db = mOpenHelper.getWritableDatabase();
                count = db.delete(TimingsContract.TABLE_NAME, selection, selectionArgs);
                break;

            case TIMINGS_ID:
                db = mOpenHelper.getWritableDatabase();
                long timingsId = TimingsContract.getTaskId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;

                if((selection != null) && (selection.length() >0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.delete(TimingsContract.TABLE_NAME, selectionCriteria, selectionArgs);
                break;*/

            default:
                throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "Existing update, returning " + count);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update called with uri " + uri);
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase db;
        int count;

        String selectionCriteria;

        switch(match) {
            case TASKS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TasksContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TASKS_ID:
                db = mOpenHelper.getWritableDatabase();
                long taskId = TasksContract.getTaskId(uri);
                selectionCriteria = TasksContract.Columns._ID + " = " + taskId;

                if((selection != null) && (selection.length() >0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(TasksContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;

            /*case TIMINGS:
                db = mOpenHelper.getWritableDatabase();
                count = db.update(TimingsContract.TABLE_NAME, values, selection, selectionArgs);
                break;

            case TIMINGS_ID:
                db = mOpenHelper.getWritableDatabase();
                long timingsId = TimingsContract.getTaskId(uri);
                selectionCriteria = TimingsContract.Columns._ID + " = " + timingsId;

                if((selection != null) && (selection.length() >0)) {
                    selectionCriteria += " AND (" + selection + ")";
                }
                count = db.update(TimingsContract.TABLE_NAME, values, selectionCriteria, selectionArgs);
                break;*/

            default:
                    throw new IllegalArgumentException("Unknown uri: " + uri);
        }
        Log.d(TAG, "Existing update, returning " + count);
        return count;
    }
}
