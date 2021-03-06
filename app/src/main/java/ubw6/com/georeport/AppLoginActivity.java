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
import android.os.StrictMode;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kjudoy on 4/10/2015.
 *
 * @author kjudoy
 *         <p/>
 *         Creates Login Activity
 */
public class AppLoginActivity extends Activity {

    private Button btnLogin, btnRegister;
    private EditText txtEmail, txtPass;
    private TextView lblForgot;

    //creates login activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnRegister = (Button) findViewById(R.id.btn_login_register);
        lblForgot = (TextView) findViewById(R.id.lbl_login_forgotPass);
        txtEmail = (EditText) findViewById(R.id.txt_login_email);
        txtEmail.setHint("Email");
        txtPass = (EditText) findViewById(R.id.txt_login_pass);
        txtPass.setHint("Password");
        txtPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        //when register button is pressed takes user to register screen
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterTermActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        //when forgot password link is pressed takes user to forgot password screen
        lblForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //if email and password field is filled out appropriately and user clicks login button
        //user is taken to their account screen
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                StringBuilder builder = new StringBuilder();
                int errorCount = 0;
                if (txtEmail.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Email is Empty\n");
                }
                if (txtPass.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Password is Empty\n");
                }
                if (txtPass.getText().toString().length() < 6) {
                    errorCount++;
                    builder.append("Passwords needs to be at least 6 in length\n");
                }

                if (errorCount == 0) {
                    FeedResult res = WebFeed.login(txtEmail.getText().toString(), txtPass.getText().toString());

                    if (res.isSuccess()) {
                        // This would save a shared pref of the new account registered
                        SharedPreferences sharedPref = getSharedPreferences("georeport.account_logged", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("email", txtEmail.getText().toString());
                        editor.putString("uid", res.getMessage());
                        //editor.putString("secQ", );
                        //editor.putString("secA", );
                        editor.putBoolean("isTracking", true);
                        editor.commit();

                        // Start Service when login is successful
                        SharedPreferences mPreferences = getSharedPreferences(
                                "georeport.account_logged", MODE_PRIVATE);
                        startService(new Intent(getBaseContext(), LocationService.class));
                        LocationService.setUID(mPreferences.getString("uid", ""));
                        LocationService.setServiceAlarm(v.getContext(), true);

                        //Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_LONG).show();
                        Intent intent;
                        intent = new Intent(v.getContext(), MyAccountActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        errorCount++;
                        builder.append(res.getMessage());

                        String strError = "Errors: " + errorCount;
                        builder.append(strError);
                        Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                        //return;
                    }
                } else {
                    String strError = "Errors: " + errorCount;
                    builder.append(strError);
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    //return;
                }
            }
        });

        //TextView t = (TextView) findViewById(R.id.mainText);
        //t.setText(WebFeed.webStatus());
    }
}
