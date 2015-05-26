package ubw6.com.georeport;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;


public class UserPrefActivity extends Activity {

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
        btnTrackingToggle = (ToggleButton) findViewById(R.id.btn_pref_trackingToggle);

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
            layoutParamsUser.height = -2;
        } else {
            layoutParamsUser.height = 0;
        }
        linearLayoutUser.setLayoutParams(layoutParamsUser);
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
