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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Controller class that interacts with a local (SQL) database to temporarily
 * store the sampled data points while awaiting upload to remote server.
 *
 * @author Kirsten Grace
 * @version 4/28/2015
 */
public class SQLData extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "GeoReportDB.db";
    public static final String SAMPLES_TABLE_NAME = "samples";
    public static final String SAMPLES_COLUMN_PID = "pid";
    public static final String SAMPLES_COLUMN_UID = "uid";
    public static final String SAMPLES_COLUMN_LONG = "lon";
    public static final String SAMPLES_COLUMN_LAT = "lat";
    public static final String SAMPLES_COLUMN_HEADING = "heading";
    public static final String SAMPLES_COLUMN_SPEED = "speed";
    public static final String SAMPLES_COLUMN_TIME = "time";

    /* The column numbers are based on the order they are passed
        when table was created. */
    public static final int PID_COLUMN = 0;
    public static final int UID_COLUMN = 1;
    public static final int LON_COLUMN = 2;
    public static final int LAT_COLUMN = 3;
    public static final int HEADING_COLUMN = 4;
    public static final int SPEED_COLUMN = 5;
    public static final int TIME_COLUMN = 6;

    /**
     * Creates an SQLData object that is able to interact with local database. When this object
     * is no longer needed, call close() method to close the connection please.
     *
     * @param context If created from an activity, use keyword "this" as the Context.
     */
    public SQLData(Context context) {
        super(context, DATABASE_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table samples " +
                        "(pid integer primary key autoincrement, uid string, lon double, " +
                        "lat double, heading double, speed double, time long)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS samples");
        onCreate(db);
    }

    /**
     * Adds a sample data point to the local database. Returns true upon success or
     * false if an error was encountered.
     *
     * @param s The sample point to be added to local database.
     * @return Returns true upon success.
     */
    public boolean insert(Sample s) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(SAMPLES_COLUMN_UID, s.myUID );
        contentValues.put(SAMPLES_COLUMN_LONG, s.myLon);
        contentValues.put(SAMPLES_COLUMN_LAT, s.myLat);
        contentValues.put(SAMPLES_COLUMN_HEADING, s.myHeading);
        contentValues.put(SAMPLES_COLUMN_SPEED, s.mySpeed);
        contentValues.put(SAMPLES_COLUMN_TIME, s.myTime);

        return db.insert(SAMPLES_TABLE_NAME, null, contentValues) != -1;
    }

    /**
     * Returns a sample data point within the database. This will most likely be the sample data
     * point which has been
     *
     * @return The next sample data point
     */
    public Sample getNext() {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c =  db.rawQuery("select * from samples order by pid limit 1", null);
            c.moveToNext();

            /* new Sample(double theLon, double theLat, double theHeading,
            double theSpeed, int theTime, String theUID, int thePID); */
        final double lon = c.getDouble(LON_COLUMN);
        final double lat = c.getDouble(LAT_COLUMN);
        final double heading = c.getDouble(HEADING_COLUMN);
        final double speed = c.getDouble(SPEED_COLUMN);
        final long time = c.getLong(TIME_COLUMN);
        final String uid = c.getString(UID_COLUMN);
        final int pid = c.getInt(PID_COLUMN);

        Sample s = new Sample(lon, lat, heading, speed, time, uid, pid);       /*PID*/



//        Sample s = new Sample(c.getDouble(LON_COLUMN),  /*Longitude*/
//                    c.getDouble(LAT_COLUMN),     /*Latitude*/
//                    c.getDouble(HEADING_COLUMN),     /*Heading*/
//                    c.getDouble(SPEED_COLUMN),     /*Speed*/
//                    c.getLong(TIME_COLUMN),       /*Time*/
//                    c.getString(UID_COLUMN),     /*UserID*/
//                    c.getInt(PID_COLUMN));       /*PID*/
            c.close();
            return s;
    }

//        public Cursor getData(int pid){
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor res =  db.rawQuery( "select * from samples where pid="+pid+"", null );
//            return res;
//        }

    /**
     * Returns the number of sample data points stored within the local database.
     *
     * @return The number of sample data points stored within the local database.
     */
    public int getSize() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SAMPLES_TABLE_NAME);
        return numRows;
    }

    /**
     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
     * false if a sample by that ID number is not found OR it was unable to delete.
     *
     * @param PID The sample data point's unique ID
     * @return Returns true upon successful location and deletion of sample data point by ID.
     */
    public boolean removeSample(int PID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return (db.delete(SAMPLES_TABLE_NAME, "pid = ? ", new String[] { Integer.toString(PID) }))
                != 0;
    }

    /**
     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
     * false if a sample by that ID number is not found OR it was unable to delete.
     *
     * @param PID The sample data point's unique ID
     * @return Returns true upon successful location and deletion of sample data point by ID.
     */
    public boolean removePoint(int PID) {
        return removeSample(PID);
    }

    /**
     * Returns true if the local database is empty.
     *
     * @return Returns true if the local database is empty.
     */
    public boolean isEmpty() {
        if (getSize() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
