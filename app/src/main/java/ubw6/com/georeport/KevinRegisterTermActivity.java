package ubw6.com.georeport;

import android.app.Activity;
import android.content.Intent;
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
                    //Toast.makeText(v.getContext(), "Success!", Toast.LENGTH_LONG).show();

                    Intent intent;
                    intent = new Intent(v.getContext(), KevinAccountActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    //finishActivity(1001);
                } else {
                    builder.append("The terms is not accepted\n");
                    Toast.makeText(v.getContext(), builder.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

    }
}
