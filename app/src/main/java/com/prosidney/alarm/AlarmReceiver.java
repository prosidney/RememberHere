package com.prosidney.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import com.prosidney.location.GPSTracker;
import com.prosidney.service.GoogleAPIHandler;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Sidney on 15-03-20.
 */
public class AlarmReceiver extends BroadcastReceiver {
    // GPSTracker class
    GPSTracker gps;

    GoogleAPIHandler apiHandler;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        //get and send location information
        System.out.println("AlarmReceiver.onReceive");

        // create class object
        gps = new GPSTracker(context);

        apiHandler = new GoogleAPIHandler(context);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();


            // \n is for new line
            //Toast.makeText(context, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();
            //System.out.println("Your Location is - \nLat: " + latitude + "\nLong: " + longitude);

           try {
                Map<String, Object> ret = apiHandler.getNearbyStore("NoFrills", 43.6928138, -79.4707626);
                //System.out.println(ret.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }
}
