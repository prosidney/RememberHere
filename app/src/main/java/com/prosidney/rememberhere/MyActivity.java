package com.prosidney.rememberhere;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prosidney.alarm.AlarmReceiver;
import com.prosidney.domain.Establishment;

import java.util.List;

public class MyActivity extends ActionBarActivity{
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_ID = "com.mycompany.myfirstapp.ID";

    public static final int MSG_SERVICE_OBJ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        new LongOperation().execute();

        scheduleJob(getApplicationContext());
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
                Toast.makeText(getApplicationContext(), "MyActivity.action_search", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "MyActivity.action_settings", Toast.LENGTH_SHORT).show();
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

        startActivity(intent);
    }

    public void scheduleJob(Context context) {
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pendingIntent);
    }

    private class LongOperation extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            List<Establishment> establishments = Establishment.listAll(Establishment.class);

            String[] values = new String[establishments.size()];
            for (int i = 0; i < establishments.size(); i++) {
                values[i] = establishments.get(i).getName();
            }

            return values;
        }

        @Override
        protected void onPostExecute(String[] results) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, results){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    TextView textView = (TextView) view.findViewById(android.R.id.text1);

                    /*YOUR CHOICE OF COLOR*/
                    textView.setTextColor(Color.BLACK);

                    return view;
                }
            };

            final ListView listview = (ListView) findViewById(R.id.listView);
            listview.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
