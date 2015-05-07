/*
 * Copyright (c) SPRING15.
 * TCSS450A - Mobile App Programming
 * team6 - Unlimited Budget Works
 * Romero, Kevingil kjudoy
 * Miraflor, Crystal mirafcry
 * Grace, Kirsten kngrace
 * Stump, James stumpj
 */

package ubw6.com.georeport;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;

import android.util.Log;
import android.widget.Toast;



/**
 * Background service for the GeoTracker to ping
 * location data.
 *
 * Created by Crystal on 5/6/2015.
 */
public class LocationService extends IntentService {

    private static final String TAG = "LocationService";
    private static final int POLL_INTERVAL = 60000; //60 seconds

    public LocationService() {
        super("LocationService");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "Service Starting");
        Toast.makeText(this, "Service Starting", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Do task here
        Log.i("LocationService", "Service Running");

        //Obtain latest location data
        Criteria criteria = new Criteria();
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        //Data needed to be input into the database
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double speed = location.getSpeed();
        long timeStamp = location.getTime();
        Toast.makeText(this, "Latitude: " + latitude + ", Longitude: " + longitude,
                Toast.LENGTH_LONG).show();
        //put here
        //create Sample object from this data
        //send to SQLite Database

//===========Code I had in MapsActivity=============================================================
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {//testing
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                double speed = location.getSpeed();
//                long timeStamp = location.getTime();
//                LatLng latLng = new LatLng(latitude, longitude);
//                //heading
//                //id
//                //create sample object from this data and send to sqlite database
//            }
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//            @Override
//            public void onProviderEnabled(String provider) {}
//            @Override
//            public void onProviderDisabled(String provider) {}
//        };
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, POLL_INTERVAL, 0, locationListener);
//==================================================================================================
    }

    /**
     * Sets alarm for a certain poll interval for the service.
     *
     * @param context Context
     * @param isOn Boolean if service is on
     */
    public static void setServiceAlarm(Context context, boolean isOn) {
        Intent i = new Intent(context, LocationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    , POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "Service Stopped");
        Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }


}
