package ubw6.com.georeport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by kjudoy on 4/10/2015.
 */
public class KevinLoginActivity extends Activity {

    private Button btnLogin, btnRegister;
    private EditText txtEmail, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_login);

        btnLogin = (Button) findViewById(R.id.btn_login_login);
        btnRegister = (Button) findViewById(R.id.btn_login_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), KevinRegisterActivity.class);
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
                if (txtPass.getText().toString().length() < 8) {
                    errorCount++;
                    builder.append("Passwords needs to be at least 8 in length\n");
                }

                if (errorCount == 0) {
                    Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_LONG).show();
                    Intent intent;
                    intent = new Intent(v.getContext(), KevinAccountActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    builder.append("Errors: " + errorCount);
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    return;
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
