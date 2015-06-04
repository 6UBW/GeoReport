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
public class LoginTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    private static int ETXT_EMAIL = 1;
    private static int ETXT_PASSWORD = 0;

    public LoginTest() {
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

        solo.clearEditText(ETXT_EMAIL);
        solo.clearEditText(ETXT_PASSWORD);
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void testNoInput() {
        solo.unlockScreen();

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Email is Empty") && solo.searchText("Password is Empty");


        assertTrue("Invalid error messages", textFound);
    }

    public void testNoPassword() {
        solo.enterText(ETXT_EMAIL, "Bob");
        solo.enterText(ETXT_PASSWORD, "");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Password is Empty");
        assertTrue("Only password error should show", textFound);
    }

    public void testNoEmail() {
        solo.enterText(ETXT_EMAIL, "");
        solo.enterText(ETXT_PASSWORD, "passpass");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Email is Empty") && !solo.searchText("Password is Empty");
        assertTrue("Only password error should show", textFound);
    }

    public void testShortPass() {
        solo.enterText(ETXT_EMAIL, "");
        solo.enterText(ETXT_PASSWORD, "pass");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("at least 6");
        assertTrue("Short password error should show", textFound);
    }

    public void testInvalidEmail() {
        solo.enterText(ETXT_EMAIL, "Bob");
        solo.enterText(ETXT_PASSWORD, "passpas");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("enter a valid email");
        assertTrue("Invalid email message should show", textFound);
    }

    public void testWrongPassword() {
        solo.enterText(ETXT_EMAIL, "bob@bob.bob");
        solo.enterText(ETXT_PASSWORD, "passpass");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("Incorrect email or pass");
        assertTrue("Incorrect email/password error should show", textFound);
    }

    public void testCorrect() {
        solo.enterText(ETXT_EMAIL, "usaname@gmail.com");
        solo.enterText(ETXT_PASSWORD, "passpass");

        solo.clickOnButton("Login");
        boolean textFound = solo.searchText("My Account");
        assertTrue("Should be logged in", textFound);
    }

}