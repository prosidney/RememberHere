package com.prosidney.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by Sidney on 15-03-30.
 */
public class GoogleAPIHandler {

    private Context mContext;

    public static String GOOGLE_KEY = "AIzaSyDyvmXO-2zIUbnDqfrBrMXxOUJU8QZgaTQ";

    public GoogleAPIHandler(Context context) {
        mContext = context;
    }

    public Map<String, Object> getNearbyStore(String storeName, double latitue, double longitude) throws IOException {
        String url = "https://maps.googleapis.com/maps/api/place/search/json?location=" + latitue + "," + longitude +
                "&radius=1000&sensor=true&name=" + storeName + "&type=establishment&key=" + GOOGLE_KEY;

        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute(new URL(url));

        return null;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, String> {

        protected String doInBackground(URL... urls) {
            try {
                HttpURLConnection con = (HttpURLConnection) urls[0].openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Type type = new TypeToken<Map<String, Object>>() {}.getType();

                Map<String, Object> o = new Gson().fromJson(response.toString(), type);

                if(o.get("results") == null){
                    return "No Places nearby";
                }

                List<Map<String, Object>> places = (List<Map<String, Object>>) o.get("results");

                if(places.size() == 0){
                    return "No Places nearby";
                }

                //TODO refactor
                return places.get(0).get("name") + " - " + places.get(0).get("vicinity");
            } catch (Exception e) {
                e.printStackTrace();
                return "Error";
            }
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
        }
    }
}
