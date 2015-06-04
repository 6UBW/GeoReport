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

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;


/**
 * @author James Stump
 * @version 6/4/15
 */
public class RegisterTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    private static String ERROR_NO_EMAIL = "Email is Empty";
    private static String ERROR_NO_PASSWORD = "Password is Empty";
    private static String ERROR_NO_QUESTION = "Secret Question is Empty";
    private static String ERROR_NO_ANSWER = "Secret Answer is Empty";
    private static String ERROR_SHORT_PASSWORD = "at least";

    private static int TXT_EMAIL = 0;
    private static int ETET_PASS = 1;
    private static int ETXT_PASS_AGAIN = 2;
    private static int ETXT_QUESTION = 3;
    private static int EXE_ANSWER = 4;


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

    public void testNoAccept() {
        solo.clickOnButton("Next");

        assertTrue("Didn't accept terms", solo.searchText("terms is not accepted"));
        assertTrue("Still on terms page", solo.searchText("Terms and Condition"));

    }

    public void testNoInput() {
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Next");


        solo.clickOnButton("Submit");
        boolean emptyAnswer = solo.searchText(ERROR_NO_ANSWER);

        solo.clickOnButton("Submit");
        boolean emptyEmail = solo.searchText(ERROR_NO_EMAIL);

        solo.clickOnButton("Submit");
        boolean emptyPass = solo.searchText(ERROR_NO_PASSWORD);

        solo.clickOnButton("Submit");
        boolean emptyQuestion =  solo.searchText(ERROR_NO_QUESTION);

        solo.clickOnButton("Submit");
        boolean shortPass = solo.searchText(ERROR_SHORT_PASSWORD);


        assertTrue("Empty Answer", emptyAnswer);
        assertTrue("Empty Email", emptyEmail);
        assertTrue("Empty Password", emptyPass);
        assertTrue("Empty Question", emptyQuestion);
        assertTrue("Short Password", shortPass);

    }

    public void testNoMatchPassword() {
        solo.clickOnCheckBox(0);
        solo.clickOnButton("Next");

        solo.enterText(0, "abc@123.abc");
        solo.enterText(1, "passpass");
        solo.enterText(2, "ssapssap");
        solo.enterText(3, "my question");
        solo.enterText(4, "my answer");

        solo.clickOnButton("Submit");
        assertTrue("Passwords don't match", solo.searchText("Passwords didn't match"));

    }



}