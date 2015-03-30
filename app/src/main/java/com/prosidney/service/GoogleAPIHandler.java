package com.prosidney.service;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

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
                "&radius=5000&sensor=true&name=" + storeName + "&type=establishment&key=" + GOOGLE_KEY;

        DownloadFilesTask downloadFilesTask = new DownloadFilesTask();
        downloadFilesTask.execute(new URL(url));

        return null;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {

        private String value;

        protected Long doInBackground(URL... urls) {
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

                System.out.println("o.size = " + o.size());

                value = String.valueOf(o.size());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0l;
        }

        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            //showDialog("Downloaded " + result + " bytes");
            Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
        }
    }
}
