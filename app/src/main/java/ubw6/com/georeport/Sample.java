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
 * Class to represent a single sample of location/speed/heading/time
 */
public class Sample {
    public final double myLon;
    public final double myLat;
    public final double myHeading;
    public final double mySpeed;
    public final long myTime;
    public final String myUID;
    public final int myPID;

    /**
     * Constructs a Sample object without a point ID
     * @param theLon Longitude of the sample
     * @param theLat Latitude of the sample
     * @param theHeading Heading of the sample
     * @param theSpeed Speed at time of the sample
     * @param theTime Timestamp of the sample (Unix Timestamp format)
     * @param theUID User ID
     */
    public Sample(double theLon, double theLat, double theHeading,
                  double theSpeed, long theTime, String theUID) {
        this(theLon, theLat, theHeading, theSpeed, theTime, theUID, -1);
    }

    /**
     * Constructs a Sample object with a point ID
     * @param theLon Longitude of the sample
     * @param theLat Latitude of the sample
     * @param theHeading Heading of the sample
     * @param theSpeed Speed at time of the sample
     * @param theTime Timestamp of the sample (Unix Timestamp format)
     * @param theUID User ID
     * @param thePID ID of sample from local database
     */
    public Sample(double theLon, double theLat, double theHeading, double theSpeed,
                  long theTime, String theUID, int thePID) {
        this.myPID = thePID;
        this.myLon = theLon;
        this.myLat = theLat;
        this.myHeading = theHeading;
        this.mySpeed = theSpeed;
        this.myTime = theTime;
        this.myUID = theUID;
    }

}
