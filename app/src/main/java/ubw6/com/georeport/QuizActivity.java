package ubw6.com.georeport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends ActionBarActivity {

    private Button mTrueButton, mFalseButton, mNextButton;
    private TextView mQuestionTextView;
    private SharedPreferences mPreferences;
    private static final String currentIndex = "current_index";

    private QuestionBank[] mQuestionBank = {
            new QuestionBank(R.string.question_oceans, true),
            new QuestionBank(R.string.question_mideast, false),
            new QuestionBank(R.string.question_africa, false),
            new QuestionBank(R.string.question_america, true),
            new QuestionBank(R.string.question_asia, true)
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_main);

        mPreferences = getPreferences(Context.MODE_PRIVATE);
        mCurrentIndex = mPreferences.getInt(getString(R.string.question_index), 0);

        // Another way but this is only possible if your activity's
        // onDestroy is called.
        //if (savedInstanceState != null) {
        //    mCurrentIndex = savedInstanceState.getInt(currentIndex, 0);
        //}

        mQuestionTextView = (TextView) findViewById(R.id.question_textView);
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);

        TrueFalseListener trueFalseListener = new TrueFalseListener();
        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(trueFalseListener);
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(trueFalseListener);


        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                int question = mQuestionBank[mCurrentIndex].getQuestion();
                mQuestionTextView.setText(question);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(getString(R.string.question_index), mCurrentIndex);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPreferences = getPreferences(Context.MODE_PRIVATE);
        mCurrentIndex = mPreferences.getInt(getString(R.string.question_index), 0);

        mQuestionTextView = (TextView) findViewById(R.id.question_textView);
        int question = mQuestionBank[mCurrentIndex].getQuestion();
        mQuestionTextView.setText(question);
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("current_index", mCurrentIndex);
        super.onSaveInstanceState(outState);
    }*/

    private class TrueFalseListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            boolean answer = mQuestionBank[mCurrentIndex].isTrueQuestion();
            if (answer && v == mTrueButton || !answer && v == mFalseButton) {
                Toast.makeText(QuizActivity.this, R.string.correct_toast, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(QuizActivity.this, R.string.incorrect_toast, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
}
