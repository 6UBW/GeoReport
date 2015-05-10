package ubw6.com.georeport;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;


public class MapsListActivity extends Activity {

    TextView lblList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapslist_activity);

        lblList = (TextView) findViewById(R.id.lbl_maps_list);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        Long startDate = extras.getLong("startDate");
        Long endDate = extras.getLong("endDate");
        //Toast.makeText(this, startDate + " - "+ endDate, Toast.LENGTH_LONG).show();
        StringBuilder builder = new StringBuilder();
        int i = 1;
        List<Sample> listPos = WebFeed.getPoints(startDate, endDate, mPreferences.getString("uid", ""));
        for (Sample pos: listPos) {
            //Toast.makeText(this, "lon: " + pos.getMyLon() + ", lat: " + pos.getMyLat() , Toast.LENGTH_LONG).show();
            builder.append("  Point " + i +
                    "\n    Longitude: " + pos.getMyLon() +
                    "\n    Latitude: " + pos.getMyLat() + "\n\n");
            i++;
        }
        lblList.setText(builder);
    }

}
/**
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mapslist_activitty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

 */