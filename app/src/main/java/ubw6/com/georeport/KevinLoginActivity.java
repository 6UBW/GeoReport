package ubw6.com.georeport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by kjudoy on 4/10/2015.
 * @author kjudoy
 */
public class KevinLoginActivity extends Activity {

    private Button btnLogin, btnRegister;
    private EditText txtEmail, txtPass;
    private TextView lblForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_login);

        btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnRegister = (Button) findViewById(R.id.btn_login_register);
        lblForgot = (TextView) findViewById(R.id.lbl_login_forgotPass);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KevinRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lblForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = (EditText) findViewById(R.id.txt_login_email);
                txtPass = (EditText) findViewById(R.id.txt_login_pass);

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
                    // This would save a shared pref of the new account registered
                    SharedPreferences sharedPref = getSharedPreferences("georeport.account_logged", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", txtEmail.getText().toString());
                    editor.putString("pass", txtPass.getText().toString());
                    //editor.putString("secQ", );
                    //editor.putString("secA", );
                    editor.commit();

                    Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_LONG).show();
                    Intent intent;
                    intent = new Intent(v.getContext(), KevinMyAccountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String strError = "Errors: " + errorCount;
                    builder.append(strError);
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    //return;
                }
            }
        });


        // to allow reading from URL
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
       // StrictMode.setThreadPolicy(policy);

        //TextView t = (TextView) findViewById(R.id.mainText);
        //t.setText(WebFeed.webStatus());
    }
}
