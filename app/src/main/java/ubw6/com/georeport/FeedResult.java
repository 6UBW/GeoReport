package ubw6.com.georeport;

/**
 * Simple class containing results of a web service call.
 *
 *
 * @author James Stump
 * @version 4/16/15
 */
public class FeedResult {
    private boolean mySuccess;
    private String myMessage;

    FeedResult(String success) {
        this(success, "");
    }

    FeedResult(boolean success) {
        this(success, "");
    }

    FeedResult(String success, String message) {
        mySuccess = "success".equalsIgnoreCase(success);
        myMessage = message;
    }

    FeedResult(boolean success, String message) {
        mySuccess = success;
        myMessage = message;
    }

    public String getMessage() {
        return myMessage;
    }

    public boolean isSuccess() {
        return mySuccess;
    }
}
