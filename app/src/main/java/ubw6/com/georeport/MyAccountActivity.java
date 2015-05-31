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
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by kjudoy on 4/10/2015.
 *
 * @author kjudoy
 *         <p/>
 *         Creates "My Account" activity
 */
public class MyAccountActivity extends Activity {

    public static final String MM_DD_YY = "MM/dd/yy"; //In which you need put here

    private EditText txtStartDate, txtEndDate, txtStartTime, txtEndTime;
    private Button btnLogout, btnFindTrajectory;
    private SharedPreferences mPreferences;
    private Calendar myCalendar;
    private TimePicker myTimePicker;
    private Button btnSettings;

    //creates my account
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount);

        myCalendar = Calendar.getInstance();

        TextView t = (TextView) findViewById(R.id.lbl_myacc_email);
        txtStartDate = (EditText) findViewById(R.id.txt_myacc_startDate);
        txtStartDate.setHint("MM/DD/YY");
        txtEndDate = (EditText) findViewById(R.id.txt_myacc_endDate);
        txtEndDate.setHint("MM/DD/YY");
        txtStartTime = (EditText) findViewById(R.id.txt_myacc_startTime);
        txtStartTime.setHint("HH:mm");
        txtEndTime = (EditText) findViewById(R.id.txt_myacc_endTime);
        txtEndTime.setHint("HH:mm");
        btnSettings = (Button) findViewById(R.id.btn_myacc_settings);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(v.getContext(), PrefActivity.class); //MapsActivity.class);
                startActivity(intent);
            }
        });

        // get the logged email from the shared pref
        mPreferences = getSharedPreferences(
                "georeport.account_logged", MODE_PRIVATE);
        t.setText(mPreferences.getString("email", "Blank"));

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog myDatePicker;
                myDatePicker = new DatePickerDialog(MyAccountActivity.this, dateStart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                myDatePicker.setTitle("Select a Start Date");
                myDatePicker.show();
            }
        });

        txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog myDatePicker;
                myDatePicker = new DatePickerDialog(MyAccountActivity.this, dateEnd, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                myDatePicker.setTitle("Select an End Date");
                myDatePicker.show();
            }
        });

        txtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(MyAccountActivity.this, startTime
                        , myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                myTimePicker.setTitle("Select a Start Time");
                myTimePicker.show();
            }
        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog myTimePicker;
                myTimePicker = new TimePickerDialog(MyAccountActivity.this, endTime
                        , myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
                myTimePicker.setTitle("Select an End Time");
                myTimePicker.show();
            }
        });


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
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US);
                Date parsedDateStart = null;
                Date parsedDateEnd = null;
                Boolean goodInput = true;
                try {
                    parsedDateStart = dateFormat.parse(txtStartDate.getText().toString() + " 00:00");
                    parsedDateEnd = dateFormat.parse(txtEndDate.getText().toString() + " 23:59");
                } catch (ParseException e) {
                    goodInput = false;
                    Toast.makeText(v.getContext(), "Bad Input", Toast.LENGTH_LONG).show();
                }
                if (goodInput) {
                    Timestamp timestampStart = new java.sql.Timestamp(parsedDateStart.getTime());
                    Timestamp timestampEnd = new java.sql.Timestamp(parsedDateEnd.getTime());
                    //Toast.makeText(v.getContext(), timestampStart + " - " + timestampEnd, Toast.LENGTH_LONG).show();

                    List<Sample> testSample = WebFeed.getPoints((timestampStart.getTime() / 1000),
                            (timestampEnd.getTime() / 1000), mPreferences.getString("uid", ""));
                    if (!testSample.isEmpty()) {
                        Intent intent;
                        intent = new Intent(v.getContext(), MapsActivity.class); //MapsActivity.class);
                        intent.putExtra("startDate", (timestampStart.getTime() / 1000));
                        intent.putExtra("endDate", (timestampEnd.getTime() / 1000));
                        startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), "The dates provide doesn't have points", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        /**


         btnShowList = (Button) findViewById(R.id.btn_myacc_showlist);
         btnShowList.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy HH:mm", Locale.US);
        Date parsedDateStart = null;
        Date parsedDateEnd = null;
        Boolean goodInput = true;
        String sTime = txtStartTime.getText().toString();
        String eTime = txtEndTime.getText().toString();
        if (sTime.matches(""))
        sTime = "00:00";
        if (eTime.matches(""))
        eTime = "23:59";

        try {
        parsedDateStart = dateFormat.parse(txtStartDate.getText().toString() + " " + sTime);
        parsedDateEnd = dateFormat.parse(txtEndDate.getText().toString() + " " + eTime);
        } catch (ParseException e) {
        goodInput = false;
        Toast.makeText(v.getContext(), "Bad Input", Toast.LENGTH_LONG).show();
        }
        if (goodInput) {
        Timestamp timestampStart = new java.sql.Timestamp(parsedDateStart.getTime());
        Timestamp timestampEnd = new java.sql.Timestamp(parsedDateEnd.getTime());
        //Toast.makeText(v.getContext(), timestampStart + " - " + timestampEnd, Toast.LENGTH_LONG).show();

        List<Sample> testSample = WebFeed.getPoints( (timestampStart.getTime() / 1000),
        (timestampEnd.getTime() / 1000), mPreferences.getString("uid", ""));
        if (!testSample.isEmpty()) {
        Intent intent;
        intent = new Intent(v.getContext(), MapsListActivity.class); //MapsActivity.class);
        intent.putExtra("startDate", (timestampStart.getTime() / 1000));
        intent.putExtra("endDate", (timestampEnd.getTime() / 1000));
        startActivity(intent);
        } else {
        Toast.makeText(v.getContext(), "The dates provide doesn't have points", Toast.LENGTH_LONG).show();
        }
        }
        }
        });
         */
    }

    //logout method
    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences("georeport.account_logged", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("email", "");
        editor.putString("uid", "");
        editor.putBoolean("isTracking", false);
        editor.commit();

        // Stop Service at logout
        stopService(new Intent(getBaseContext(), LocationService.class));
        LocationService.setServiceAlarm(this, false);

        Intent intent;
        intent = new Intent(this, AppLoginActivity.class);
        // This clears all the previous activities just so the user cannot go back to prev activities
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // DATEPICKER POPUP RESOURCE:
// http://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
//================================== DATEPICKER POPUP START ======================================
    DatePickerDialog.OnDateSetListener dateStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YY, Locale.US);
            txtStartDate.setText(sdf.format(myCalendar.getTime()));
        }

    };

    DatePickerDialog.OnDateSetListener dateEnd = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YY, Locale.US);
            txtEndDate.setText(sdf.format(myCalendar.getTime()));
        }


    };
//================================== DATEPICKER POPUP END ======================================

    TimePickerDialog.OnTimeSetListener startTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            txtStartTime.setText(selectedHour + ":" + selectedMinute);
        }
    };

    TimePickerDialog.OnTimeSetListener endTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            txtEndTime.setText(selectedHour + ":" + selectedMinute);
        }
    };
}



