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

    //private HashMap hp;

    // public DBHelper(Context context) {
    public SQLData(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table samples " +
                        "(pid integer primary key autoincrement, uid text, lon float, lat float, " +
                        "heading float, speed float, time float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
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

        // TODO query the database and construct/return a Sample back

        // select * from samples order by pid limit 1


            SQLiteDatabase db = this.getReadableDatabase();
            Cursor result =  db.rawQuery("select * from samples order by pid limit 1", null );
            System.err.print("getNext column names are:" + result.getColumnName(0) +
            result.getColumnName(1) + result.getColumnName(2) + " etc etc");
            //Sample res = new Sample(result.getDouble());
            result.close();
            return null; //TODO create Sample object to be fed back?
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

//}


//public class SQLData {
//
//    private SQLData singleton;
//    private SQLiteDatabase myDatabase;
//
//    /**
//     * Private constructor to prevent instantiation.
//     */
//    private SQLData() {
//        // Stop trying to create me and just use my static methods.
//    }
//
//    private boolean openDatabase() {
//        // myDatabase = openOrCreateDatabase("GeoReportDB.db", MODE_PRIVATE, null);
//        return false;
//    }
//
//    /**
//     * Adds a sample data point to the local database. Returns true upon success or
//     * false if an error was encountered.
//     *
//     * @param s The sample point to be added to local database.
//     * @return Returns true upon success.
//     */
//    public static boolean insert(Sample s) {
//        boolean outcome = false;
//
//        // TODO Feed Sample s into SQL database
//
//        return outcome;
//    }
//
//    /**
//     * Returns a sample data point within the database. This will most likely be the sample data
//     * point which has been
//     *
//     * @return The next sample data point
//     */
//    public static Sample getNext() {
//
//        // TODO query the database and construct/return a Sample back
//
//        return null;
//    }
//
//    /**
//     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
//     * false if a sample by that ID number is not found OR it was unable to delete.
//     *
//     * @param PID The sample data point's unique ID
//     * @return Returns true upon successful location and deletion of sample data point by ID.
//     */
//    public static boolean removeSample(int PID) {
//        boolean outcome = false;
//
//        // TODO delete the data in the database by using the given PID value
//
//        return outcome;
//    }
//
//    /**
//     * Removes the sample by its ID number. Returns true upon successful deletion. Will return
//     * false if a sample by that ID number is not found OR it was unable to delete.
//     *
//     * @param PID The sample data point's unique ID
//     * @return Returns true upon successful location and deletion of sample data point by ID.
//     */
//    public static boolean removePoint(int PID) {
//        return removeSample(PID);
//    }
//
//    /**
//     * Returns true if the local database is empty.
//     *
//     * @return Returns true if the local database is empty.
//     */
//    public static boolean isEmpty() {
//        if (getSize() == 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Returns the number of sample data points stored within the local database.
//     *
//     * @return The number of sample data points stored within the local database.
//     */
//    public static int getSize() {
//        int size = 0;
//        // TODO query database to check how many sample data points exist
//        /**
//         SQLiteDatabase db = this.getReadableDatabase();
//         int numRows = (int) DatabaseUtils.queryNumEntries(db, SAMPLE_POINT_DATA);
//         return numRows;
//         */
//        return size;
//    }
//
//    private class DBHelper extends SQLiteOpenHelper {
