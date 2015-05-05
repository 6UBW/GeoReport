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

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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

import android.location.LocationManager;
//import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
//import com.google.android.gms.maps.model.Polyline;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static CameraPosition cp;
    private boolean isBackPressed = false;
    private static final int POLL_INTERVAL = 6000; //60 seconds


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
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
        final List<LatLng> listPos = new ArrayList<>();
        Criteria criteria = new Criteria();
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, true);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double speed = location.getSpeed();
                long timeStamp = location.getTime();
                LatLng latLng = new LatLng(latitude, longitude);
                listPos.add(latLng);
                //heading
                //id
                //create sample object from this data and send to sqlite database
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
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        listPos.add(latLng);

        //===============================================================
//        List<LatLng> listPos = new ArrayList<>();
//        listPos.add(new LatLng(47.221, -122.47));
//        listPos.add(new LatLng(47.2215, -122.471));
//        listPos.add(new LatLng(47.222, -122.4715));
//        listPos.add(new LatLng(47.222, -122.4718));
//        listPos.add(new LatLng(47.2225, -122.4722));
//        listPos.add(new LatLng(47.2223, -122.4723));


        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(listPos.get(0));

        for (LatLng pos: listPos) {
            mMap.addMarker(new MarkerOptions().position(pos).title("Marker"));
//            polylineOptions.add(pos);
            builder.include(pos);
        }
//        mMap.addPolyline(polylineOptions);
        LatLngBounds bounds = builder.build();

        if (cp == null) {
            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, padding);
            // check if its far out zoom on the first point
            mMap.moveCamera(cu);
            Float zoom = (mMap.getCameraPosition().zoom+2);
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom), 2000, null);
            //Toast.makeText(this.getApplicationContext(), "" + zoom , Toast.LENGTH_LONG).show();

            //
            //mMap.getCameraPosition().zoom();
        } else {
            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
            //Toast.makeText(this.getApplicationContext(), "" + mMap.getCameraPosition().zoom , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        cp = null;
        isBackPressed = true;
        //super.onBackPressed();
    }


}
