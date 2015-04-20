package ubw6.com.georeport;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Provides unit tests for the WebFeed class.
 *
 * @author James Stump
 * @version 4/20/15
 */
public class WebFeedTest extends TestCase {

    // Test is WebFeed is up
    public void testFeedStatus() {
        assertTrue("Web Feed Status", WebFeed.webStatus());
    }

    // Test login with known account
    public void testLogin() {
        assertTrue("Login should work", WebFeed.login("smith@aol.com", "mypass").isSuccess());
    }

    // Test login with known account
    public void testLoginFail() {
        assertFalse("Login should fail", WebFeed.login("smith@aol.com", "mypasss").isSuccess());
    }

    // Tests the results of webStatus
    public void testRegisterUser() throws Exception {
        FeedResult f = WebFeed.register("test@test.test", "abc123", "What is your name?", "Bob");
        assertEquals("That email address has already been registered.", f.getMessage());
    }

    // Tests terms
    public void testGetTerms() {
        assertTrue("Terms pulled from feed", WebFeed.getTerms().isSuccess());
    }

}