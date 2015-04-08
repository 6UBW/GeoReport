package ubw6.com.georeport;

/**
 * Created by mmuppa on 3/27/15.
 */
public class QuestionBank {
    private int mQuestion;

    private boolean mTrueQuestion;


    public QuestionBank(int question, boolean trueQuestion) {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

    public int getQuestion() {
        return mQuestion;
    }

    public void setQuestion(int question) {
        mQuestion = question;
    }

    public boolean isTrueQuestion() {
        return mTrueQuestion;
    }

    public void setTrueQuestion(boolean trueQuestion) {
        mTrueQuestion = trueQuestion;
    }


}
