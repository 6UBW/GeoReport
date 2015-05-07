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
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by crystal
 * @author crystal
 *
 * Class for "Forgot Password" activity
 */
public class ForgotPasswordActivity extends Activity {

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //after email is entered and submit button is pressed, email is sent
        //to user for password reset
        btnSubmit = (Button) findViewById(R.id.btn_forgot_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtEmail = (EditText) findViewById(R.id.txt_forgot_email);

                FeedResult res = WebFeed.resetPassword(txtEmail.getText().toString());

                if (res.isSuccess()) {
                    Toast.makeText(v.getContext(), res.getMessage(), Toast.LENGTH_LONG).show();

                    Intent intent;
                    intent = new Intent(v.getContext(), AppLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(v.getContext(), res.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * When back button is pressed, returns to login screen
     */
    @Override
    public void onBackPressed() {
        Intent intent;
        intent = new Intent(this, AppLoginActivity.class);
        startActivity(intent);
        finish();
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    //    // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_forgot_password, menu);
    //    return true;
    //}

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    //    int id = item.getItemId();

        //noinspection SimplifiableIfStatement
     //   if (id == R.id.action_settings) {
     //       return true;
     //   }

    //    return super.onOptionsItemSelected(item);
    //}
}