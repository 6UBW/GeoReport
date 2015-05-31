package ubw6.com.georeport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;


public class PrefActivity extends Activity {

    private LinearLayout linearLayoutUser;
    private RadioButton radioUser;
    private RadioGroup radiogrpSettings;
    private ViewGroup.LayoutParams layoutParamsUser;
    private EditText txtSampleIntUser, txtUploadIntUser;
    private ToggleButton btnTrackingToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);

        linearLayoutUser = (LinearLayout) findViewById(R.id.linear_pref_user);
        radioUser = (RadioButton) findViewById((R.id.radio_pref_user));
        radiogrpSettings = (RadioGroup) findViewById((R.id.radiogrp_pref_settings));
        layoutParamsUser = linearLayoutUser.getLayoutParams();
        txtSampleIntUser = (EditText) findViewById(R.id.txt_pref_sampleintUser);
        txtUploadIntUser = (EditText) findViewById(R.id.txt_pref_uploadintUser);
        txtSampleIntUser.setHint("Sample Interval");
        txtUploadIntUser.setHint("Upload Interval");
        txtSampleIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        txtUploadIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        btnTrackingToggle = (ToggleButton) findViewById(R.id.btn_pref_trackingToggle);

        toggleIsTrackON();
        toggleServiceStartStop();
        btnTrackingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleServiceStartStop();
            }
        });


        toggleUserRelative();
        radiogrpSettings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                toggleUserRelative();
            }
        });


    }

    private void toggleUserRelative() {
        if (radioUser.isChecked()) {
            //layoutParamsUser.height = -2;
            txtSampleIntUser.setEnabled(true);
            txtUploadIntUser.setEnabled(true);
            txtSampleIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
            txtUploadIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);

        } else {
            //layoutParamsUser.height = 0;
            txtSampleIntUser.setEnabled(false);
            txtUploadIntUser.setEnabled(false);
            txtSampleIntUser.setInputType(InputType.TYPE_NULL);
            txtSampleIntUser.setInputType(InputType.TYPE_NULL);
        }
        //linearLayoutUser.setLayoutParams(layoutParamsUser);
    }

    private void toggleIsTrackON() {
        SharedPreferences mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        Boolean isTracking = mPreferences.getBoolean("isTracking", true);
        if (isTracking) {
            btnTrackingToggle.setChecked(true);
        } else {
            btnTrackingToggle.setChecked(false);
        }
    }

    private void toggleServiceStartStop() {
        SharedPreferences mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        if (btnTrackingToggle.isChecked()) {
            editor.putBoolean("isTracking", true);
            startService(new Intent(getBaseContext(), LocationService.class));
            LocationService.setUID(mPreferences.getString("uid", ""));
            LocationService.setServiceAlarm(this, true);
        } else {
            editor.putBoolean("isTracking", false);
            stopService(new Intent(getBaseContext(), LocationService.class));
            LocationService.setServiceAlarm(this, false);
        }
        editor.commit();
    }

/**
 @Override public boolean onCreateOptionsMenu(Menu menu) {
 // Inflate the menu; this adds items to the action bar if it is present.
 getMenuInflater().inflate(R.menu.menu_user_pref, menu);
 return true;
 }

 @Override public boolean onOptionsItemSelected(MenuItem item) {
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
 */
}
