package ubw6.com.georeport;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


/**
 * @author James Stump
 * @version 5/28/15
 */
public class RegisterTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    private static int ETXT_EMAIL = 1;
    private static int ETXT_PASSWORD = 0;

    private static String ERROR_NO_EMAIL = "Email is Empty";
    private static String ERROR_NO_PASSWORD = "Password is Empty";
    private static String ERROR_NO_QUESTION = "Secret Question is Empty";
    private static String ERROR_NO_ANSWER = "Secret Answer is Empty";
    private static String ERROR_SHORT_PASSWORD = "at least 6";




    public RegisterTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

        //in case logged in already
        if (solo.searchText("My Account")) {
            solo.clickOnButton("Logout");
        }

        //in case at the register screen
        if (solo.searchText("Confirm Password")) {
            solo.goBack();
        }

        solo.clickOnButton("Register");
    }



    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testNoInput() {
        solo.clickOnButton("Next");

        boolean emptyAnswer = solo.searchText(ERROR_NO_ANSWER);
        boolean emptyEmail = solo.searchText(ERROR_NO_EMAIL);
        boolean emptyPass = solo.searchText(ERROR_NO_PASSWORD);
        //boolean emptyQuestion =  solo.searchText(ERROR_NO_QUESTION);
        boolean shortPass = solo.searchText(ERROR_SHORT_PASSWORD);


        assertTrue("Empty Answer", emptyAnswer);
        assertTrue("Empty Email", emptyEmail);
        assertTrue("Empty Password", emptyPass);
        //assertTrue("Empty Question", emptyQuestion);
        assertTrue("Short Password", shortPass);

    }



}