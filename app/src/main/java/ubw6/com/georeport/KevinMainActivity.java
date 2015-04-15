package ubw6.com.georeport;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by kjudoy on 4/10/2015.
 */
public class KevinMainActivity extends Activity{


    private SharedPreferences mPreferences;
    private String loggedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        loggedEmail = mPreferences.getString("email", null);

        Intent intent;
        if (loggedEmail == null) {
            intent = new Intent(this, KevinLoginActivity.class);
        } else {
            intent = new Intent(this, KevinAccountActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
