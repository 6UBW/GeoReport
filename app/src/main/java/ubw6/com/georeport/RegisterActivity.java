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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kjudoy on 4/10/2015.
 * @author kjudoy
 *
 * Class for Register Activity
 */
public class RegisterActivity extends Activity {

    final String REGEX_EMAIL_PAT = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private EditText txtEmail, txtPass, txtPassConf, txtSecretQ, txtSecretA;
    private Button btnSubmit;

    //creates register activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // to allow reading from URL
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //t = (TextView) findViewById(R.id.mainText);
        btnSubmit = (Button) findViewById(R.id.btn_register_submit);

        /**
         * if all fields are appropriately filled out and next button is clicked,
         * user is taken to terms and agreement screen to accept
         * warning if fields are not filled out correctly
         */
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = (EditText) findViewById(R.id.txt_register_email);
                txtPass = (EditText) findViewById(R.id.txt_register_pass);
                txtPassConf = (EditText) findViewById(R.id.txt_register_passConf);
                txtSecretQ = (EditText) findViewById(R.id.txt_register_secretQ);
                txtSecretA = (EditText) findViewById(R.id.txt_register_secretA);

                StringBuilder builder = new StringBuilder();
                int errorCount = 0;
                if (txtEmail.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Email is Empty\n");
                }
                if (!isValidEmail(txtEmail.getText().toString())) {
                    errorCount++;
                    builder.append("Invalid Email Input\n");
                }
                if (txtPass.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Password is Empty\n");
                }
                if (txtSecretQ.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Secret Question is Empty\n");
                }
                if (txtSecretA.getText().toString().matches("")) {
                    errorCount++;
                    builder.append("Secret Answer is Empty\n");
                }
                if (!txtPass.getText().toString().equals(txtPassConf.getText().toString())) {
                    errorCount++;
                    builder.append("Passwords didn't match\n");
                }
                if (txtPass.getText().toString().length() < 5) {
                    errorCount++;
                    builder.append("Passwords needs to be at least 5 in length\n");
                }
                //errorCount = 0;
                if (errorCount == 0) {

                    FeedResult res = WebFeed.register(txtEmail.getText().toString(), txtPass.getText().toString(),
                            txtSecretQ.getText().toString(), txtSecretA.getText().toString());

                    if (res.isSuccess()) {
                        Toast.makeText(v.getContext(), res.getMessage(), Toast.LENGTH_LONG).show();

                        // go to login screen when successful
                        Intent intent;
                        intent = new Intent(v.getContext(), AppLoginActivity.class);
                        // This clears all the previous activities just so the user cannot go back to prev activities
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        //onBackPressed();
                        //finish();
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
    }

    // Check is email is valid
    // Email Pattern Resource: http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(REGEX_EMAIL_PAT);
        Matcher matcher;
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //when back is pressed, user is returned to login screen
    //@Override
    //public void onBackPressed() { }

}
