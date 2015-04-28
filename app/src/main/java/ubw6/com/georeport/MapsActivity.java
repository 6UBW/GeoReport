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

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.location.R;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;
//import com.google.android.gms.maps.model.Polyline;


public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

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
               // mMap.setMyLocationEnabled(true); // shows the My location button
               //mMap.addMarker(new MarkerOptions().position(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude())).title(("I'm HERE!")));
                setUpMap();
               // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
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
        List<LatLng> listPos = new ArrayList<>();
        listPos.add(new LatLng(47.22, -122.47));
        listPos.add(new LatLng(47.30, -122.20));
        listPos.add(new LatLng(47.35, -121.95));
        listPos.add(new LatLng(47.39, -122.05));
        listPos.add(new LatLng(47.32, -122.25));
        listPos.add(new LatLng(47.33, -122.26));
        listPos.add(new LatLng(47.34, -122.28));
        listPos.add(new LatLng(47.35, -122.31));
        listPos.add(new LatLng(47.34, -122.32));
        listPos.add(new LatLng(47.33, -122.30));
        listPos.add(new LatLng(47.32, -122.31));
        listPos.add(new LatLng(47.33, -122.33));


        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng pos: listPos) {
            //mMap.addMarker(new MarkerOptions().position(pos).title("Marker"));
            polylineOptions.add(pos);
            builder.include(pos);
        }
        mMap.addPolyline(polylineOptions);
        LatLngBounds bounds = builder.build();




        //CameraPosition cameraPosition = new CameraPosition.Builder().target(
        //        pos).zoom(12).build();


        // draw polyline and draw marker
        // draw again per submit



        // Adjust the camera depending on the position



        int padding = 0; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, padding);
        // check if its far out zoom on the first point
        mMap.moveCamera(cu);
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        //mMap.getCameraPosition().zoom();

    }

}
