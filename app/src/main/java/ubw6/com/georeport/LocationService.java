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
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.TimeZone;


/**
 * Background service for the GeoTracker to ping
 * location data.
 *
 * Created by Crystal on 5/6/2015.
 * @version 6/4/15
 * @author mirafcry
 * @author stumpj
 */
public class LocationService extends IntentService {

    private static String myUID;
    private SharedPreferences mPreferences;

    /** minimum # of database entries to force upload at. */
    public static int UPLOAD_MIN_ENTRIES = 60;
    /** minimum upload interval expressed in milliseconds. */
    public static int UPLOAD_INTERVAL = 60000;
    private static final String TAG = "LocationService";
    /** minimum poll interval expressed in milliseconds. */
    private static int POLL_INTERVAL = 60000; //60 seconds
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
        return START_STICKY;
    }

    /**
     * Sets the user id
     * @param uid user id
     */
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

            // if user preferences are NOT set, check if current POLL_INTERVAL matches power status
            mPreferences = getSharedPreferences("georeport.account_logged", MODE_PRIVATE);
            if (!mPreferences.getBoolean("isPrefSaved", false)) {

                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                Intent batteryStatus = this.getBaseContext().registerReceiver(null, ifilter);
                // Are we charging / charged?
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;

                Log.i(TAG, "Battery isCharging: " + isCharging);

                // when charging, poll interval is once per minute (60000 ms)
                if (isCharging && POLL_INTERVAL != 60000) {
                    POLL_INTERVAL = 60000;
                    // alarm interval has changed, restart with new interval
                    LocationService.setServiceAlarm(this.getBaseContext(), false);
                    LocationService.setServiceAlarm(this.getBaseContext(), true);
                } // when not charging, poll interval is once per 5 minutes (300000 ms)
                  else if (!isCharging && POLL_INTERVAL != 300000) {
                    POLL_INTERVAL = 300000;
                    // alarm interval has changed, restart with new interval
                    LocationService.setServiceAlarm(this.getBaseContext(), false);
                    LocationService.setServiceAlarm(this.getBaseContext(), true);
                } else {
                    // no change. POLL_INTERVAL is already set the correct value.
                }
            }

        } else {
            // else, toast error message
            Toast.makeText(this, "Starting Service",//"Error getting location",//"Latitude: " + latLng. + ", Longitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        }
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
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + POLL_INTERVAL
                    , POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }

    }

    public static void setSampleUploadInt(int txtST, int txtUT, int spST, int spUT) {
        // set all things on min/min
        int sampleSEC = 0;
        double sampleSECinMIN = 0;
        double uploadMINperMIN = 0;
        int uploadMIN = 0;
        if (spST == 0) { // Sec
            sampleSEC = 1000 * txtST;
        } else if (spST == 1) { // Min
            sampleSEC = 1000 * txtST * 60;
        } else { // Hour
            sampleSEC = 1000 * txtST * 60 * 60;
        }

        sampleSECinMIN = ((sampleSEC / 1000) / 60);

        if (spUT == 0) { // Hour
            uploadMIN = txtST * 60;
        } else { // Day
            uploadMIN = txtST * 60 * 24;
        }

        uploadMINperMIN = uploadMIN / sampleSECinMIN;
        POLL_INTERVAL = sampleSEC;
        UPLOAD_INTERVAL = ((int) uploadMINperMIN);
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
        final long t = curTime();
        final Sample s = new Sample(l.getLongitude(), l.getLatitude(),
                l.getBearing(), l.getSpeed(), t, myUID);
        LatLng latLng = new LatLng(l.getLatitude(), l.getLongitude());
        db.insert(s);
        Log.i("LOC", "Point added to local. DB Size = " + db.getSize());

        // only upload if network connection exists
        if (WebFeed.webStatus()) {
            // attempt upload if UPLOAD_MIN_ENTRIES is satisfied OR the required interval has passed
            mPreferences = getSharedPreferences(
                    "georeport.account_logged", MODE_PRIVATE);
            long currentTime = System.currentTimeMillis();
            if ((currentTime - mPreferences.getLong("lastupload", currentTime)) >= UPLOAD_INTERVAL
                    || db.getSize() > UPLOAD_MIN_ENTRIES) {
                // update the timestamp of the last upload to the current time
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putLong("lastupload", System.currentTimeMillis());
                editor.commit();
                uploadSamples(db); // handles the upload
            }
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

    public static void manualUploadSample(Context context) {
        Criteria criteria = new Criteria();
        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location myLocation = locationManager.getLastKnownLocation(provider);
        if (myLocation != null) {
            Location l = myLocation;
            final long t = curTime();

            final Sample s = new Sample(l.getLongitude(), l.getLatitude(),
                    l.getBearing(), l.getSpeed(), t, myUID);
            Log.i("LOC", "Manual upload sample to Web");
            FeedResult res = WebFeed.logPoint(s);
            String strStatus;
            if (res.isSuccess()) {
                Log.i("LOC", "Manual upload Successful");
                strStatus = " Successful";
            } else {
                Log.i("LOC", "Manual upload Failed");
                strStatus = " Failed";
            }
            Toast.makeText(context, "Manual upload" + strStatus, Toast.LENGTH_LONG).show();
        } else {
            // else, toast error message
            Toast.makeText(context, "Error getting location",//"Latitude: " + latLng. + ", Longitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        }
    }

    private static long curTime() {
        final long t = (System.currentTimeMillis() + TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings());
        Log.i("LOC", "Time is: " + new Date(t));


        return t / 1000;
    }

}
