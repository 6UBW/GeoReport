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
 * Controller class that interacts with a local (SQL) database to temporarily
 * store the sampled data points while awaiting upload to remote server.
 *
 * @author Kirsten Grace
 * @version 4/28/2015
 */
public class SQLData {

    /**
     * Private constructor to prevent instantiation.
     */
    private SQLData(){
        // Stop trying to create me and just use my static methods.
    }

    /**
     * Adds a sample data point to the local database. Returns true upon success or
     * false if an error was encountered.
     *
     * @param s The sample point to be added to local database.
     * @return Returns true upon success.
     */
    static boolean insert(Sample s) {
        boolean outcome = false;

        // TODO Feed Sample s into SQL database

        return outcome;
    }

    /**
     * Returns a sample data point within the database. This will most likely be the sample data
     * point which has been
     *
     * @return The next sample data point
     */
    static Sample getNext() {

        // TODO query the database and construct/return a Sample back

        return void;
    }

    /**
     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
     * false if a sample by that ID number is not found OR it was unable to delete.
     *
     * @param PID The sample data point's unique ID
     * @return Returns true upon successful location and deletion of sample data point by ID.
     */
    static boolean removeSample(int PID) {
        boolean outcome = false;

        // TODO delete the data in the database by using the given PID value

        return outcome;
    }

    /**
     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
     * false if a sample by that ID number is not found OR it was unable to delete.
     *
     * @param PID The sample data point's unique ID
     * @return Returns true upon successful location and deletion of sample data point by ID.
     */
    static boolean removePoint(int PID) {
        return removeSample(PID);
    }

    /**
     * Returns true if the local database is empty.
     *
     * @return Returns true if the local database is empty.
     */
    static boolean isEmpty() {
        if (getSize() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the number of sample data points stored within the local database.
     *
     * @return The number of sample data points stored within the local database.
     */
    static int getSize() {
        int size = 0;
        // TODO query database to check how many sample data points exist
        return size;
    }

}
