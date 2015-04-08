package ubw6.com.georeport;

import junit.framework.TestCase;

/**
 * Provides unit tests for the WebFeed class.
 *
 * @author James Stump
 * @version 4/7/15
 */
public class WebFeedTest extends TestCase {

    // Tests the results of testReadURL
    public void testReadURL() throws Exception {
        String res = WebFeed.readURL("http://450.atwebpages.com/test.php").getString("result");
        assertEquals(res, "success");
    }
}