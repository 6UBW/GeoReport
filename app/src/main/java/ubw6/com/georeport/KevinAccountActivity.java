package ubw6.com.georeport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kjudoy on 4/10/2015.
 */
public class KevinAccountActivity extends Activity{

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_myaccount);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView t = (TextView) findViewById(R.id.lbl_myacc_email);
        t.setText("Email Dude");

        btnLogout = (Button) findViewById(R.id.btn_myacc_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KevinLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
