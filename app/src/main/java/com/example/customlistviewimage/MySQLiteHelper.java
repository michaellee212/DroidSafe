package com.example.customlistviewimage;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {


    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_URL = "url";
    public static final String TABLE_EXCLUSION = "exclusion";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_INDEX = "_index";
    public static final String COLUMN_ACTION = "action";

    public static final String COLUMN_UINDEX = "uindex";
    public static final String COLUMN_UID = "uid";
    public static final String COLUMN_UDOMAIN = "udomain";

    public static final String COLUMN_EINDEX = "eindex";
    public static final String COLUMN_EDOMAIN = "edomain";
    public static final String COLUMN_EACTION = "eaction";


    private static final String DATABASE_NAME = "categories.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_CATEGORY + "(" +
            COLUMN_INDEX + " INTEGER PRIMARY KEY NOT NULL," +
            COLUMN_ID + " INTEGER NOT NULL,"+
            COLUMN_ACTION + " INTEGER NOT NULL)";

    private static final String DATABASE_CREATE2 = "CREATE TABLE "
            + TABLE_URL + "(" +
            COLUMN_UINDEX + " INT PRIMARY KEY NOT NULL," +
            COLUMN_UDOMAIN + " TEXT NOT NULL,"+
            COLUMN_UID + " INT)";

    private static final String DATABASE_CREATE3 = "CREATE TABLE "
            + TABLE_EXCLUSION + "(" +
            COLUMN_EINDEX + " INT PRIMARY KEY NOT NULL," +
            COLUMN_EDOMAIN + " TEXT NOT NULL," +
            COLUMN_EACTION + " INT NOT NULL)";



    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE2);
        database.execSQL(DATABASE_CREATE3);

        database.execSQL("CREATE TABLE IF NOT EXISTS " +TABLE_CATEGORY +" (_INDEX, CATEGORY_ID, ACTION)");

        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (0, 1001, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (1, 1002, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (2, 1003, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (3, 1004, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (4, 1005, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (5, 1006, 0)");
        database.execSQL("INSERT INTO " + TABLE_CATEGORY + " VALUES (6, 1007, 0)");



        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_URL + "(_INDEX, UDOMAIN, CATEGORY_ID)");

        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (0, 'www.drugs.com', 1001)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (1, 'skinsgambling.com', 1002)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (2, 'www.miniclip.com', 1003)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (3, 'www.porn.com', 1004)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (4, 'www.facebook.com', 1005)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (5, 'www.youtube.com', 1006)");
        database.execSQL("INSERT INTO " + TABLE_URL + " VALUES (6, 'shopping.pchome.com.tw', 1007)");


        database.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXCLUSION + "(_INDEX, DOMAIN, ACTION)");

        database.execSQL("INSERT INTO " + TABLE_EXCLUSION + " VALUES (0, 'www.aegislab.com', 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }


}

