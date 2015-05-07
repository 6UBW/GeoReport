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

    public void testGetPoints() throws Exception {
        List<Sample> res = WebFeed.getPoints(new Timestamp(1423046056), new Timestamp(1423046175), "ead846cdc73c1a305a61cfb963ed1684d188e8ba");
        assertEquals(4, res.size());

    }
}