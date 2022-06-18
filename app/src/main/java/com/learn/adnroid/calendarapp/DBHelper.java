package com.learn.adnroid.calendarapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="SQLiteDBTest";

    public DBHelper(Context context) {
        super(context, DetailContract.DB_NAME, null, DetailContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG,getClass().getName()+".onCreate()");
        db.execSQL(DetailContract.Users.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        Log.i(TAG,getClass().getName() +".onUpgrade()");
        db.execSQL(DetailContract.Users.DELETE_TABLE);
        onCreate(db);
    }

    public long insertUserByMethod(String title, String startTime, String endTime, String address, String note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DetailContract.Users.KEY_TITLE, title);
        values.put(DetailContract.Users.KEY_START_TIME, startTime);
        values.put(DetailContract.Users.KEY_END_TIME, endTime);
        values.put(DetailContract.Users.KEY_ADDRESS, address);
        values.put(DetailContract.Users.KEY_NOTE, note);

        return db.insert(DetailContract.Users.TABLE_NAME,null,values);
    }

    public Cursor getAllUsersByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DetailContract.Users.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = DetailContract.Users._ID +" = ?";
        String[] whereArgs ={_id};
        return db.delete(DetailContract.Users.TABLE_NAME, whereClause, whereArgs);
    }
}
