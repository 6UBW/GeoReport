package ubw6.com.georeport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * Created by kjudoy on 4/14/2015.
 */
public class KevinRegisterTermActivity extends Activity{
    private Button btnSubmit;
    private CheckBox boxTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kevin_terms);


        btnSubmit = (Button) findViewById(R.id.btn_terms_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxTerms = (CheckBox) findViewById(R.id.box_terms_acceptTerms);



                StringBuilder builder = new StringBuilder();
                if (boxTerms.isChecked()) {
                    Toast.makeText(v.getContext(), "Registration Success!", Toast.LENGTH_LONG).show();

                    // This gets the string on the edit texts from the RegisterActivity
                    Intent prevIntent = getIntent();
                    Bundle extras = prevIntent.getExtras();
                    String email = extras.getString("email");
                    String pass = extras.getString("pass");
                    String secQ = extras.getString("secQ");
                    String secA = extras.getString("secA");

                    // This would save a shared pref of the new account registered just like login
                    SharedPreferences sharedPref = getSharedPreferences("georeport.account_logged", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("email", email);
                    editor.commit();

                    Intent intent;
                    intent = new Intent(v.getContext(), KevinAccountActivity.class);
                    // This clears all the previous activities just so the user cannot go back to prev activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    builder.append("The terms is not accepted\n");
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }
}
