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

import junit.framework.TestCase;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author James Stump
 * @version 5/7/15
 */
public class FeedTest extends TestCase {

    //UID used for testing purposes
    private static final String UID = "ead846cdc73c1a305a61cfb963ed1684d188e8ba";

    public void setUp() throws Exception {
        super.setUp();

    }

    // Test is WebFeed is up
    public void testFeedStatus() {
        assertTrue("Web Feed Status", WebFeed.webStatus());
    }

    // Test login with known account
    public void testLogin() {
        assertTrue("Login should work", WebFeed.login("usaname@gmail.com", "passpass").isSuccess());
    }

    // Test login with known account
    public void testLoginFail() {
        assertFalse("Login should fail", WebFeed.login("usaname@gmail.com", "passpasss").isSuccess());
    }

    // Tests the results of webStatus
    public void testRegisterUser() throws Exception {
        FeedResult f = WebFeed.register("usaname@gmail.com", "abc123", "What is your name?", "Bob");
        assertEquals("That email is already registered!", f.getMessage());
    }

    // Tests terms
    public void testGetTerms() {
        assertTrue("Terms pulled from feed", WebFeed.getTerms().isSuccess());
    }

    // Ensures the length of terms is expected (greater than 50)
    public void testTermsLength() {
        FeedResult res = WebFeed.getTerms();
        assertTrue(res.getMessage().length() > 50);
    }

    //attempts to upload the same point twice with the same ID
    public void testSamePoint() {
        Sample s = new Sample(50, 50, 50, 50, 1234567, UID);
        WebFeed.logPoint(s);
        FeedResult res = WebFeed.logPoint(s);
        assertFalse(res.isSuccess());
    }

    //attempts to log a valid point
    public void testValidPoint() {
        Sample s = new Sample(50, 50, 50, 50, System.currentTimeMillis() / 1000 - 10000, UID);
        FeedResult res = WebFeed.logPoint(s);
        assertTrue(res.isSuccess());
    }

    //attempts to get a range of points (expected to get 4 points)
    public void testGetPoints() throws Exception {
        List<Sample> res = WebFeed.getPoints(new Timestamp(1423006056), new Timestamp(1423096175), "ead846cdc73c1a305a61cfb963ed1684d188e8ba");
        assertEquals(4, res.size());

    }

    //attempts to reset an invalid email password
    public void testResetInvalid() {
        assertFalse(WebFeed.resetPassword("invalidpass@nothing.nope").isSuccess());
    }

    //attempts to reset a valid email password
    public void testResetValid() {
        assertTrue(WebFeed.resetPassword("usaname@gmail.com").isSuccess());
    }
}