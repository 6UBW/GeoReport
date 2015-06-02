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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

/**
 * Created by kjudoy on 4/10/2015.
 * @author kjudoy
 *
 * Main activity
 */
public class MainActivity extends Activity{

    private SharedPreferences mPreferences;
    private String loggedEmail;

    //if user is currently logged in takes them to
    //screen they were on previously. if not, then
    //login screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // read the shared pref
        mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        loggedEmail = mPreferences.getString("email", "");

        Intent intent;
        if (loggedEmail.equals("")) { // check the shared pref if an email is logged in
            intent = new Intent(this, AppLoginActivity.class);
        } else {
            intent = new Intent(this, MyAccountActivity.class);
        }
        startActivity(intent);
        finish();
    }

}
