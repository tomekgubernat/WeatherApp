package com.codecool.weatherapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "weather2.db";
    public static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " +
                Contract.Entry.TABLE_NAME + " (" +
                Contract.Entry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Entry.COLUMN_DATE + " TEXT NOT NULL, " +
                Contract.Entry.COLUMN_MIN + " REAL NOT NULL, " +
                Contract.Entry.COLUMN_MAX + " REAL NOT NULL, " +
                Contract.Entry.COLUMN_ICON_ID + " REAL NOT NULL, " +
        ");";

                db.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.Entry.TABLE_NAME);
        onCreate(db);
    }
}
