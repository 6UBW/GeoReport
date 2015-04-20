package ubw6.com.georeport;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Provides unit tests for the WebFeed class.
 *
 * @author James Stump
 * @version 4/7/15
 */
public class WebFeedTest extends TestCase {

    // Tests the results of webStatus
    public void testRegisterUser() throws Exception {
        FeedResult f = WebFeed.register("test@test.test", "abc123", "What is your name?", "Bob");
        assertEquals("That email address has already been registered.", f.getMessage());
    }
}