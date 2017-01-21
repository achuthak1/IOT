package com.example.hp.iot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by HP on 21/01/2017.
 */

public class SqlLiteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "LedStatus";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_STATUS_NAME = "Led_status";

    SqlLiteHelper(Context context) {
        super(context, "IOT", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_STATUS_NAME + " VARCHAR);");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    private SQLiteDatabase database;

    public boolean insertStatus(String status) {
        database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_STATUS_NAME,status);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
        return true;
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        return res;
    }
}

