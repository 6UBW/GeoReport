package ubw6.com.georeport;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by kjudoy on 4/10/2015.
 */
public class KevinRegisterActivity extends Activity {

    private EditText txtEmail, txtPass, txtPassConf, txtSecretQ, txtSecretA;
    private Button btnSubmit;
    private CheckBox boxTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_register);

        //t = (TextView) findViewById(R.id.mainText);
        btnSubmit = (Button) findViewById(R.id.btn_register_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = (EditText) findViewById(R.id.txt_register_email);
                txtPass = (EditText) findViewById(R.id.txt_register_pass);
                txtPassConf = (EditText) findViewById(R.id.txt_register_passConf);
                txtSecretQ = (EditText) findViewById(R.id.txt_register_secQ);
                txtSecretA = (EditText) findViewById(R.id.txt_register_secA);
                boxTerms = (CheckBox) findViewById(R.id.box_register_terms);
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
                if (txtPass.getText().toString().length() < 8) {
                    errorCount++;
                    builder.append("Passwords needs to be at least 8 in length\n");
                }
                if (!boxTerms.isChecked()) {
                    errorCount++;
                    builder.append("The terms is not accepted\n");
                }

                if (errorCount == 0) {
                    Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_LONG).show();
                    Intent intent;
                    intent = new Intent(v.getContext(), KevinAccountActivity.class);
                    startActivity(intent);
                } else {
                    builder.append("Errors: " + errorCount);
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

            }
        });
    }

}
