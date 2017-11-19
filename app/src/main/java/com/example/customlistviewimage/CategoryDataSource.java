package com.example.customlistviewimage;


import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;


import java.util.ArrayList;

import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_ACTION;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_EDOMAIN;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_EINDEX;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_ID;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_INDEX;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_UDOMAIN;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_UID;
import static com.example.customlistviewimage.MySQLiteHelper.COLUMN_UINDEX;
import static com.example.customlistviewimage.MySQLiteHelper.TABLE_CATEGORY;
import static com.example.customlistviewimage.MySQLiteHelper.TABLE_EXCLUSION;
import static com.example.customlistviewimage.MySQLiteHelper.TABLE_URL;

public class CategoryDataSource {
    private final Context context;
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    ContentValues values = new ContentValues();


    public CategoryDataSource(Context context) {
        this.context = context;
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }


    public void close() {

        dbHelper.close();
    }


    public void updateActionBlock(int index) {
        Cursor cursor;
        open();
        values.put(Integer.toString(index), 0);
        System.out.println("Action updated to block with index: " + index);
        database.execSQL("UPDATE " + TABLE_CATEGORY + " SET " + COLUMN_ACTION + " = " + '0' + " WHERE "
                + COLUMN_INDEX + " = " + index);
        cursor = database.rawQuery("SELECT " + COLUMN_ACTION + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_INDEX + " = " + index, null);
        cursor.moveToFirst();
        Log.d("AAA",("getIntB: " + cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION))));
        close();
    }
    public void updateActionPass(int index){
        Cursor cursor;
        open();
        values.put(Integer.toString(index), 1);
        System.out.println("Action updated to pass with index: " + index);
        database.execSQL("UPDATE " + TABLE_CATEGORY + " SET " + COLUMN_ACTION + " = " + '1' + " WHERE "
                + COLUMN_INDEX + " = " + index);
        cursor = database.rawQuery("SELECT " + COLUMN_ACTION + " FROM " + TABLE_CATEGORY , null);
        cursor.moveToFirst();
        System.out.println("getInt: " + cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION))) ;
        close();
    }
    public boolean savedActionState(int index){
        Cursor cursor;
        open();
        boolean action = false;
        //index = 0;
        cursor = database.rawQuery("SELECT " + COLUMN_ACTION + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_INDEX + " = " + index, null);
        if(cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION)) == 0) {
                   action = true; //blocked
                } else if (cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION)) == 1) {
                   action = false; //pass
                }
            }
        else{
            action = false;
        }
        cursor.close();
        database.close();
        return action;

    }

    public void insertDB(String url, int action){
        Cursor cursor;
        open();
        cursor = database.rawQuery("SELECT " + COLUMN_EINDEX + " FROM " + TABLE_EXCLUSION, null);
        if(cursor != null) {
            cursor.moveToLast();
            int newInd = cursor.getInt(cursor.getColumnIndex(COLUMN_EINDEX));
            newInd = newInd + 1;
            database.execSQL("INSERT INTO " + TABLE_EXCLUSION + " VALUES (" + newInd + "," + "'" + url + "'" +  "," + action + ")");
        }
       //take url from editText and insert into exclusion table
        //get index of last row(MAX) and ++ for the row being inserted
        //database.rawQuery("SELECT MAX(COLUMN_INDEX)...
        cursor.close();
        database.close();

    }

    public boolean isBlock(String url) {
        Cursor cursor;
        boolean blocked = false;
        open();
        //check exclusion list for black list(0) or whitelist (1)
        cursor = database.rawQuery("SELECT " + COLUMN_UID + "," + COLUMN_UINDEX+ " FROM " + TABLE_URL + " WHERE " + COLUMN_UDOMAIN + " = '" + url + "'", null);
        if(cursor != null && cursor.getCount() == 1) {
            cursor.moveToFirst();
            int uid = cursor.getInt(cursor.getColumnIndex(COLUMN_UID));
            cursor = database.rawQuery("SELECT " + COLUMN_ACTION + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_ID + " = " + uid, null);
            if (cursor!=null&&cursor.getCount()==1) {
                cursor.moveToFirst();
                if (cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION)) == 0) {
                    blocked = true;
                } else if (cursor.getInt(cursor.getColumnIndex(COLUMN_ACTION)) == 1) {
                    blocked = false;
                }
            }
        }
        else{
            blocked = false;
        }
        if(blocked == true){
            Uri uri = Uri.parse("https://www.google.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri );
            try {
                intent.setPackage("com.android.chrome");
                intent.putExtra(Browser.EXTRA_APPLICATION_ID,"com.android.chrome");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                System.out.println("k");
            }
            catch (ActivityNotFoundException e) {
                Log.e("AAA" ,"ActivityNotFoundException: " + "com.example.customlistviewimage");
            }
        }
        else {

        }

        cursor.close();
        database.close();
        System.out.println(blocked);
        return blocked;
    }

    public ArrayList printDb(){
        //black list
        //whitelist
        ArrayList<String> items= new ArrayList<>();
        Cursor cursor;
        open();
        cursor = database.rawQuery("SELECT " + COLUMN_EDOMAIN + " FROM " + TABLE_EXCLUSION, null);
        cursor.moveToFirst();
        while(cursor!=null){
            System.out.println(cursor.getString(cursor.getColumnIndex(COLUMN_EDOMAIN)));
            items.add(cursor.getString(cursor.getColumnIndex(COLUMN_EDOMAIN)));
            if(cursor.moveToNext()){
                continue;
            }
            else{
                 break;
            }
            //return (cursor.getString(cursor.getColumnIndex(COLUMN_EDOMAIN)));
        }
            cursor.close();
            database.close();
            return items;
        }

}
