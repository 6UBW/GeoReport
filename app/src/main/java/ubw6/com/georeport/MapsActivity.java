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


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.location.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;


//import com.google.maps.android.PolyUtil;

//import java.security.Timestamp;
//import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
//import com.google.android.gms.maps.model.Polyline;

/**
 * Google maps for the GeoTracker app.
 * Displays location data of user as points
 * on a map.
 *
 * @author kjudoy
 */
public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static CameraPosition cp;
    private boolean isBackPressed = false;
    private TextView lblList;
    private ToggleButton btnToggleMapList;
    private ViewGroup.LayoutParams layoutParamsMap, layoutParamsList;
    private RelativeLayout relmap, rellist;
//    private static final int POLL_INTERVAL = 60000; //60 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lblList = (TextView) findViewById(R.id.lbl_maps_list);
        btnToggleMapList = (ToggleButton) findViewById(R.id.btn_maps_toggleMapList);
        relmap = (RelativeLayout) findViewById(R.id.relative_maps_gmap);
        rellist = (RelativeLayout) findViewById(R.id.relative_maps_list);
        layoutParamsMap = relmap.getLayoutParams();
        layoutParamsList = rellist.getLayoutParams();

        toggleRelativeMapList();
        btnToggleMapList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleRelativeMapList();
            }
        });

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isBackPressed)
            cp = mMap.getCameraPosition();
        //Toast.makeText(this.getApplicationContext(), "Pause "    , Toast.LENGTH_LONG).show();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_maps_gmap))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.setMyLocationEnabled(true);

        SharedPreferences mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        Long startDate = extras.getLong("startDate");
        Long endDate = extras.getLong("endDate");
        //Toast.makeText(this, startDate + " - "+ endDate, Toast.LENGTH_LONG).show();
        List<Sample> listPos = WebFeed.getPoints(startDate, endDate, mPreferences.getString("uid", ""));
        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        StringBuilder strbuilder = new StringBuilder();
        int i = 1;

        //Sample pos = listPos.get(0);
        for (Sample pos : listPos) {
            //Toast.makeText(this, "lon: " + pos.getMyLon() + ", lat: " + pos.getMyLat() , Toast.LENGTH_LONG).show();
            mMap.addMarker(new MarkerOptions().position(new LatLng(pos.getMyLat(), pos.getMyLon())).title("Marker"));
            polylineOptions.add(new LatLng(pos.getMyLat(), pos.getMyLon()));
            builder.include(new LatLng(pos.getMyLat(), pos.getMyLon()));

            String time = getDate(pos.getMyTime());//getDate(pos.getMyTime());

            strbuilder.append("  Point " + i +
                    "\n    Time: " + time +
                    "\n    Longitude: " + pos.getMyLon() +
                    "\n    Latitude: " + pos.getMyLat() + "\n\n");
            i++;
        }
        mMap.addPolyline(polylineOptions);
        LatLngBounds bounds = builder.build();
        lblList.setText(strbuilder);

        if (cp == null) {
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, padding);
            // check if its far out zoom on the first point
            mMap.moveCamera(cu);
            Float zoom = (mMap.getCameraPosition().zoom + 2);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            //Toast.makeText(this.getApplicationContext(), "" + zoom , Toast.LENGTH_LONG).show();

            //
            //mMap.getCameraPosition().zoom();
        } else {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            //Toast.makeText(this.getApplicationContext(), "" + mMap.getCameraPosition().zoom , Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Convert timestamp to date and hour format
     * @param time the timestamp long value
     * @return a date in a string format
     */
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time*1000);
        String date = DateFormat.format("MM/dd/yyyy hh:mm:ss", cal).toString();
        return date;
    }


    /**
     * Toggle between viewing Map or List
     */
    private void toggleRelativeMapList() {
        if (btnToggleMapList.isChecked()) {
            relmap.setVisibility(View.INVISIBLE);
            rellist.setVisibility(View.VISIBLE);
            //mMap = null;
        } else {
            relmap.setVisibility(View.VISIBLE);
            rellist.setVisibility(View.INVISIBLE);
            //setUpMapIfNeeded();
        }
        rellist.setLayoutParams(layoutParamsList);
        relmap.setLayoutParams(layoutParamsMap);
    }

    /**
     * When back button is pressed
     */
    @Override
    public void onBackPressed() {
        finish();
        cp = null;
        isBackPressed = true;
        //super.onBackPressed();
    }


}
