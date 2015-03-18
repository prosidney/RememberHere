package com.prosidney.rememberhere;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.prosidney.db.FeedReaderDbHelper;

import static com.prosidney.db.FeedReaderContract.FeedEntry;

public class MyActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_ID = "com.mycompany.myfirstapp.ID";

    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mDbHelper = new FeedReaderDbHelper(this);

//        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
//                                         "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
//                                         "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
//                                         "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
//                                         "Android", "iPhone", "WindowsMobile" };

        String[] values = getSQLData();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        final ListView listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_search:
                System.out.println("MyActivity.action_search");
                return true;
            case R.id.action_settings:
                System.out.println("MyActivity.action_settings");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        //Get the user input text value;
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();

        //Put the message inside a intent to be retrieved in the other activity;
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_ENTRY_ID, 1);
        values.put(FeedEntry.COLUMN_NAME_TITLE, message);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(FeedEntry.TABLE_NAME, null, values);

        intent.putExtra(EXTRA_ID, newRowId+"");

        startActivity(intent);
    }

    private String[] getSQLData(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = FeedEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor c = db.query(
                FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        String[] values = new String[c.getCount()];
        c.moveToFirst();
        while (c.getPosition() < c.getCount()){
            long itemId = c.getLong(c.getColumnIndexOrThrow(FeedEntry._ID));
            String itemDesc = c.getString(c.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));

            values[c.getPosition()] = itemId + "-" + itemDesc;

            c.moveToNext();
        }

        return values;
    }

}
