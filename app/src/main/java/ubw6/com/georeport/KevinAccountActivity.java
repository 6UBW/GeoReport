package ubw6.com.georeport;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

/**
 * Created by kjudoy on 4/10/2015.
 */
public class KevinAccountActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_myaccount);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView t = (TextView) findViewById(R.id.logged_email);
        t.setText("Email Dude 2");
    }
}
