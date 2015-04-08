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

/**
 * WebFeed is a static class for accessing resources from the web services.
 * Used to retrieve and parse JSON strings from URLs.
 *
 *
 * @author James Stump
 * @version 4/7/15
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

    public static String webStatus() {
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
