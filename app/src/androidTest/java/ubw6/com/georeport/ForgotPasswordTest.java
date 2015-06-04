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
 * @version 5/28/15
 */
public class ForgotPasswordTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    private static String ERROR_NO_EMAIL = "Please enter a valid email.";
    private static String ERROR_INVALID_EMAIL = "Please enter a valid email.";
    private static String ERROR_WRONG_EMAIL = "Email not found.";
    private static String SUCCESS_MESSAGE = "An email with";

    public ForgotPasswordTest() {
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

        solo.clickOnText("Forgot Your Password");
    }



    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testNoInput() {
        solo.clickOnButton("Submit");

        boolean emptyEmail = solo.searchText(ERROR_NO_EMAIL);
        assertTrue("Empty Email", emptyEmail);

    }

    public void testInvalidEMail() {
        solo.enterText(0, "thisisan@invalidemail");
        solo.clickOnButton("Submit");

        boolean invalidEmail = solo.searchText(ERROR_INVALID_EMAIL);
        assertTrue("Invalid Email", invalidEmail);
    }

    public void testWrongEMail() {
        solo.enterText(0, "thisisawrongemail@wrongaddress.wrongdomain");
        solo.clickOnButton("Submit");

        boolean wrongEmail = solo.searchText(ERROR_WRONG_EMAIL);
        assertTrue("Wrong Email", wrongEmail);
    }

    public void testCorrectEMail() {
        solo.enterText(0, "usaname@gmail.com");
        solo.clickOnButton("Submit");

        boolean correctEmail = solo.searchText(SUCCESS_MESSAGE);
        assertTrue("Correct", correctEmail);
    }

}