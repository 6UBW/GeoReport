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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by kjudoy on 4/10/2015.
 * @author kjudoy
 *
 * Creates "My Account" activity
 */
public class MyAccountActivity extends Activity{

    private Button btnLogout, btnFindTrajectory;
    private SharedPreferences mPreferences;

    //creates my account
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);

        TextView t = (TextView) findViewById(R.id.lbl_myacc_email);

        // get the logged email from the shared pref
        mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        t.setText(mPreferences.getString("email", "Blank"));

        //when logout button is clicked, user is logged out
        btnLogout = (Button) findViewById(R.id.btn_myacc_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        //when trajectory button is clicked, user is taken to trajectory screen
        btnFindTrajectory = (Button) findViewById(R.id.btn_myacc_trajetory);
        btnFindTrajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    //@Override
    //public void onBackPressed() {
    //    logout();
    //}

    //logout method
    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences("georeport.account_logged", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", "");
        editor.putString("uid", "");
        editor.commit();

        Intent intent;
        intent = new Intent(this, AppLoginActivity.class);
        // This clears all the previous activities just so the user cannot go back to prev activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}
