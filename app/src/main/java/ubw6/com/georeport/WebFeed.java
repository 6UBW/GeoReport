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

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * WebFeed is a static class for accessing resources from the web services.
 * Used to retrieve and parse JSON strings from URLs.
 *
 *
 * @author James Stump
 * @version 4/20/15
 */
public class WebFeed {
    // TODO method that only returns result string?
    // TODO URL parsing (for spaces, question marks, ect...)
    // TODO store URL as a field and simply pass webservice name?
    // TODO unique method for each service? ie: String createUser()?

    /** Tag used for Log calls within this class. */
    private static String LOG_TAG = "WebFeed";

    // Empty Constructor
    private WebFeed() {
        // Does nothing
    }

    /**
     * Check the current status of the web services
     * @return
     */
    public static boolean webStatus() {
        String res;
        final JSONObject jO = readURL("http://450.atwebpages.com/test.php");

        if (jO == null) {
            res = "fail";
        } else {
            try {
                res = jO.getString("result");
            } catch (JSONException e) {
                res = "fail";
            }
        }

        return new FeedResult(res).isSuccess();
    }

    /**
     * Gets the current terms of agreement from the web service.
     * @return Current terms of agreement
     */
    public static FeedResult getTerms() {
        FeedResult res;
        final JSONObject jO = readURL("http://450.atwebpages.com/agreement.php");

        if (jO == null) {
            res = new FeedResult(false, "Error getting terms.");
        } else {
            try {
                res = new FeedResult(true, jO.getString("agreement"));
            } catch (JSONException e) {
                res = new FeedResult(false, "Error getting terms.");
            }
        }

        return res;
    }

    /**
     * Gets the current terms of agreement from the web service.
     * @return Current terms of agreement
     */
    public static FeedResult logPoint(double lat, double lon, double speed, double heading, double time, String uid) {
        FeedResult res;
        String eUID;
        try {
            eUID = URLEncoder.encode(uid, "utf-8");
        } catch(Exception e) {
            eUID = "";
        }

        final StringBuilder url = new StringBuilder("http://450.atwebpages.com/logAdd.php");
        url.append("?lat=" + lat);
        url.append("&lon=" + lon);
        url.append("&speed=" + speed);
        url.append("&heading=" + heading);
        url.append("&timestamp=" + time);
        url.append("&source=" + eUID);

        final JSONObject jO = readURL(url.toString());

        if (jO == null) {
            res = new FeedResult(false, "Error connecting to web service.");
        } else {
            try {
                if (jO.getString("result").equalsIgnoreCase("success")) {
                    res = new FeedResult(true);
                } else if (jO.has("error")) {
                    res = new FeedResult(false, jO.getString("error"));
                } else {
                    res = new FeedResult(false);
                }
            } catch (JSONException e) {
                res = new FeedResult(false, "Error connecting to web service.");
            }
        }

        return res;
    }

    /**
     * Attempts to authenticate a user
     * @param email email address of user
     * @param password password of user
     * @return user id in message of FeedResult if successful
     */
    public static FeedResult login(String email, String password) {
        FeedResult res;
        String eEmail, ePassword;
        try {
            eEmail = URLEncoder.encode(email, "utf-8");
            ePassword = URLEncoder.encode(password, "utf-8");
        } catch(Exception e) {
            eEmail = "";
            ePassword = "";
        }
        final JSONObject jO = readURL("http://450.atwebpages.com/login.php?email=" + eEmail + "&password=" + ePassword);

        if (jO == null) {
            res = new FeedResult(false, "Error logging in.");
        } else {
            try {
                if (jO.has("userid")) {
                    res = new FeedResult(true, jO.getString("userid"));
                } else if (jO.has("error")) {
                    res = new FeedResult(false, jO.getString("error"));
                } else {
                    res = new FeedResult(false);
                }
            } catch (JSONException e) {
                res = new FeedResult(false, "Error getting terms.");
            }
        }

        return res;
    }


    /**
     * Attempts to register a user
     * @param email email address of user
     * @param password password of user
     * @param question email address of user
     * @param answer password of user
     * @return FeedResult with true if successful, false with an error message if not.
     */
    public static FeedResult register(String email, String password, String question, String answer) {
        FeedResult res;
        String eEmail, ePassword, eQuestion, eAnswer;
        try {
            eEmail = URLEncoder.encode(email, "utf-8");
            ePassword = URLEncoder.encode(password, "utf-8");
            eQuestion = URLEncoder.encode(question, "utf-8");
            eAnswer = URLEncoder.encode(answer, "utf-8");
        } catch(Exception e) {
            eEmail = "";
            ePassword = "";
            eQuestion = "";
            eAnswer = "";
        }
        final JSONObject jO = readURL("http://450.atwebpages.com/adduser.php?email=" + eEmail + "&password=" + ePassword
                + "&question=" + eQuestion + "&answer=" + eAnswer);

        if (jO == null) {
            res = new FeedResult(false, "Error creating account.");
        } else {
            try {
                if (jO.getString("result").equalsIgnoreCase("success")) {
                    res = new FeedResult(true, jO.getString("message"));
                } else if (jO.has("error")) {
                    res = new FeedResult(false, jO.getString("error"));
                } else {
                    res = new FeedResult(false);
                }
            } catch (JSONException e) {
                res = new FeedResult(false, "Error creating account.");
            }
        }

        return res;
    }

    /**
     * Given a URL, gets the JSON response and returns it.
     *
     * @param url URL of web service
     * @return JSONObject of response
     */
    private static JSONObject readURL(final String url) {

        /** Catches the input stream from the http response. */
        InputStream inStream = null;

        /** JSONObject to hold results for return. */
        JSONObject jObject = null;

        /** StringBuilder to hold JSON results. */
        final StringBuilder jsonSB = new StringBuilder();

        // Attempt an http request to web service
        try {
            final HttpResponse response = new DefaultHttpClient().execute(new HttpPost(url));
            inStream = response.getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            // TODO Clean up exception catches once done with initial dev
            Log.e(LOG_TAG, "Unsupported Encoding");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.e(LOG_TAG, "Protocol Exception");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "IO Exception");
            e.printStackTrace();
        }

        // Attempt to read http response and convert it to a JSONObject
        try {
            if (inStream != null) {
                final BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream, "iso-8859-1"), 8);
                String oneLine;

                // Append each line read to StringBuilder
                while ((oneLine = bReader.readLine()) != null) {
                    jsonSB.append(oneLine);
                }

                jObject = new JSONObject(jsonSB.toString());

                inStream.close();
            }

        } catch (JSONException e) {
            // TODO Clean up exception catches once done with initial dev
            jObject = null;
            Log.e(LOG_TAG, "JSON Exception converting String to JSONObject");
        } catch (Exception e) {
            Log.e(LOG_TAG, "JSON Exception converting String to JSONObject");
            e.printStackTrace();
        }

        // Return JSON Object
        return jObject;
    }
}
