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
