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

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.TimeZone;


/**
 * Background service for the GeoTracker to ping
 * location data.
 *
 * Created by Crystal on 5/6/2015.
 */
public class LocationService extends IntentService {

    private static String myUID;

    private static final int UPLOAD_MIN = 10;
    private static final String TAG = "LocationService";
    private static final int POLL_INTERVAL = 60000; //60 seconds
//    private static final int POLL_INTERVAL = 5000; // 5 seconds

    //constructor
    public LocationService() {

        super(TAG);
    }

    /**
     * Start up service
     * @param intent intent
     * @param flags flags
     * @param startId startId
     * @return START_STICKY
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "Service Starting");
        //Toast.makeText(this, "Service Starting", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    public static void setUID(String uid) {
        myUID = uid;
    }

    /**
     * Service task. Getting Location data is done here
     * @param intent intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //Do task here
        Log.i("LocationService", "Service Running");

        //Obtain latest location data
        Criteria criteria = new Criteria();
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);


//===========Code I had in MapsActivity=============================================================
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {//testing
                //double latitude = location.getLatitude();
                //double longitude = location.getLongitude();
                //double speed = location.getSpeed();
                //long timeStamp = location.getTime();
//                LatLng latLng = new LatLng(latitude, longitude);
                //heading
                //id
                //create sample object from this data and send to sqlite database
//                Toast.makeText(LocationService.this, latLng.toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location myLocation = locationManager.getLastKnownLocation(provider);


        if (myLocation != null) {
            // if location was successful, upload to service
            takeSample(myLocation);
        } else {
            // else, toast error message
            Toast.makeText(this, "Error getting location",//"Latitude: " + latLng. + ", Longitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        }

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

    /**
     * Stop service
     */
    @Override
    public void onDestroy() {
        Log.i(TAG, "Service Stopped");
        //Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
    }

    /**
     * Upload a location to the database
     * @param l Current location
     */
    private void takeSample(Location l) {
        SQLData db = new SQLData(this.getBaseContext());
        final long t  = System.currentTimeMillis() / 1000 + (TimeZone.getDefault().getRawOffset() / 1000)
                + (TimeZone.getDefault().getDSTSavings() / 1000);
        final Sample s = new Sample(l.getLongitude(), l.getLatitude(),
                l.getBearing(), l.getSpeed(), t, myUID);
        LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
        db.insert(s);
        Log.i("LOC", "Point added to local. DB Size = " + db.getSize());
        if (db.getSize() > UPLOAD_MIN) {
            uploadSamples(db);
        }
        db.close();
    }

    /**
     * Uploads all samples in the local database to the remote database
     * @param db Local database connection
     */
    private void uploadSamples(SQLData db) {
        Log.i("LOC", "Uploading points. DB Size = " + db.getSize());
        int lastPID = -1;
        int retries = 0;
        while(!db.isEmpty()) {
            final Sample s = db.getNext();
            Log.i("LOC", "Got point, now attempting to upload. PID = " + s.myPID);
            //error checking. If it can't upload a point to the web service after 3 tries, remove from local
            if (s.myPID == lastPID) {
                retries++;
                if (retries >= 4) {
                    Log.i("LOC", "Couldn't upload point, now attempting to delete local. P = " + s.toString());
                    FeedResult res = WebFeed.logPoint(s);
                    Log.i("LOC", "Error: " + res.getMessage());
                    db.removePoint(s.myPID);
                }
            } else {
                retries = 0;
                lastPID = s.myPID;
                FeedResult res = WebFeed.logPoint(s);
                Log.i("LOC", "Uploaded point, now attempting to delete local. PID = " + s.myPID);
                if (res.isSuccess()) {
                    db.removePoint(s.myPID);
                }
            }
        }
        Log.i("LOC", "Uploaded points. DB Size = " + db.getSize());
    }
}
