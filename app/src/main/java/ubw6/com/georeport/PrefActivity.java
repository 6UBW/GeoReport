package ubw6.com.georeport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;


public class PrefActivity extends Activity {

    SharedPreferences mPreferences;
    private LinearLayout linearLayoutUser;
    //private RadioButton radioUser;
    //private RadioGroup radiogrpSettings;
    //private ViewGroup.LayoutParams layoutParamsUser;
    private EditText txtSampleIntUser, txtUploadIntUser;
    private ToggleButton btnTrackingToggle;
    private Spinner spinnerSampleInt;
    private Spinner spinnerUploadInt;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);

        linearLayoutUser = (LinearLayout) findViewById(R.id.linear_pref_user);
        //radioUser = (RadioButton) findViewById((R.id.radio_pref_user));
        //radiogrpSettings = (RadioGroup) findViewById((R.id.radiogrp_pref_settings));
        //layoutParamsUser = linearLayoutUser.getLayoutParams();
        txtSampleIntUser = (EditText) findViewById(R.id.txt_pref_sampleintUser);
        txtUploadIntUser = (EditText) findViewById(R.id.txt_pref_uploadintUser);
        //txtSampleIntUser.setHint("Sample Interval");
        //txtUploadIntUser.setHint("Upload Interval");
        //txtSampleIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        //txtUploadIntUser.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        btnTrackingToggle = (ToggleButton) findViewById(R.id.btn_pref_trackingToggle);
        btnSave = (Button) findViewById(R.id.btn_pref_save);

        spinnerSampleInt = (Spinner) findViewById(R.id.spinner_sampleInt);
        String[] itemsSt = new String[]{"second", "min", "hour"};
        ArrayAdapter<String> adapterSt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsSt);
        adapterSt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSampleInt.setAdapter(adapterSt);

        spinnerUploadInt = (Spinner) findViewById(R.id.spinner_uploadInt);
        String[] itemsUt = new String[]{"hour", "day"};
        ArrayAdapter<String> adapterUt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemsUt);
        adapterUt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUploadInt.setAdapter(adapterUt);

        // Set the Users Preference
        txtSampleIntUser.setText(mPreferences.getInt("sampleInt", 60) + "");
        txtUploadIntUser.setText(mPreferences.getInt("uploadInt", 1) + "");
        spinnerSampleInt.setSelection(mPreferences.getInt("sampleIntDrop", 0));
        spinnerUploadInt.setSelection(mPreferences.getInt("uploadIntDrop", 0));

        toggleIsTrackON();
        toggleServiceStartStop();
        btnTrackingToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleServiceStartStop();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int txtST = Integer.parseInt(txtSampleIntUser.getText().toString());
                int txtUT = Integer.parseInt(txtUploadIntUser.getText().toString());
                int spST = spinnerSampleInt.getSelectedItemPosition();
                int spUT = spinnerUploadInt.getSelectedItemPosition();

                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putInt("sampleInt", txtST);
                editor.putInt("uploadInt", txtUT);
                editor.putInt("sampleIntDrop", spST);
                editor.putInt("uploadIntDrop", spUT);
                editor.putBoolean("isPrefSaved", true);
                editor.commit();

                LocationService.setSampleUploadInt(txtST, txtUT, spST, spUT);
                LocationService.setServiceAlarm(getBaseContext(), false);
                LocationService.setServiceAlarm(getBaseContext(), true);
                Toast.makeText(v.getContext(), "Preference has been Saved", Toast.LENGTH_LONG).show();
            }
        });

        /*
        toggleUserRelative();
        //radiogrpSettings.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
        //        toggleUserRelative();
        //    }
        });
        */


    }

    /*
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
    */

    private void toggleIsTrackON() {
        Boolean isTracking = mPreferences.getBoolean("isTracking", true);
        if (isTracking) {
            btnTrackingToggle.setChecked(true);
        } else {
            btnTrackingToggle.setChecked(false);
        }
    }

    private void toggleServiceStartStop() {
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
