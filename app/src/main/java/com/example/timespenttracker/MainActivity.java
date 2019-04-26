package com.example.timespenttracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE);
        if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //   READ_CONTACTS_GRANTED = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
        }*/

        //queries the database using the content provider
        String[] projection = {TasksContract.Columns._ID,
                TasksContract.Columns.TASKS_NAME,
                TasksContract.Columns.TASKS_DESCRIPTION,
                TasksContract.Columns.TASKS_SORTORDER};
        ContentResolver contentResolver = getContentResolver();

        ContentValues values = new ContentValues();
        values.put(TasksContract.Columns.TASKS_SORTORDER, "99");
        values.put(TasksContract.Columns.TASKS_SORTORDER, "Completed");
        String selection = TasksContract.Columns.TASKS_SORTORDER + " = " + 2;
        int count = contentResolver.update(TasksContract.CONTENT_URI, values, selection, null);
        Log.d(TAG, "onCreate: " + count + " record(s) updated");

       /* values.put(TasksContract.Columns.TASKS_NAME, "Content Provider");
        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Record content provider video");
        int count = contentResolver.update(TasksContract.buildTaskUri(1), values, null, null);
        Log.d(TAG, "onCreate: " + count + " record(s) updated");*/
        /*values.put(TasksContract.Columns.TASKS_NAME, "New Task 1");
        values.put(TasksContract.Columns.TASKS_DESCRIPTION, "Description 1");
        values.put(TasksContract.Columns.TASKS_SORTORDER, 2);
        Uri uri = contentResolver.insert(TasksContract.CONTENT_URI, values);*/

        Cursor cursor = contentResolver.query(TasksContract.CONTENT_URI,
                projection,
                null,
                null,
                TasksContract.Columns.TASKS_SORTORDER);

        //makes sure I have something valid back from the content provider
        if (cursor != null) {
            Log.d(TAG, "onCreate: num of rows: " + cursor.getCount()); //returns how many records I got back from the CP
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    Log.d(TAG, "onCreate: " + cursor.getColumnName(i) + ": " + cursor.getString(i));
                }
                Log.d(TAG, "onCreate: =============");
            }
            cursor.close();
        }

//        AppDatabase appDatabase = AppDatabase.getInstance(this);
//        final SQLiteDatabase db = appDatabase.getReadableDatabase();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

  /*  @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_WRITE_EXTERNAL_STORAGE: {

                // if request is cancelled, the rest arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission was granted, yay! Do the
                    //contacts-related task you need to do
                    Log.d(TAG, "permission granted");
                    // READ_CONTACTS_GRANTED = true;
                } else {
                    Log.d(TAG, "on request denied");
                    //permission denied, disable functionality
                }
                //if the user hits Deny and don't ask again, it will turn off the button
                //make it unclickable
                // fab.setEnabled(READ_CONTACTS_GRANTED);
            }
        }
    }*/
@Override
public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        }

@Override
public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.menumain_settings){
        return true;
        }

        return super.onOptionsItemSelected(item);
        }
}
