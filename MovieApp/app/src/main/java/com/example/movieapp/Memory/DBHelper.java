package com.example.movieapp.Memory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    static final String TABLE_NAME = "Movie";
    private static final String DATABASE_NAME = "MovieListDB";
    static final String FIELD1 = "ID";
    static final String FIELD2 = "Title";
    static final String FIELD3 = "Year";
    static final String FIELD4 = "UrlPoster";



    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + FIELD1 + " TEXT, " + FIELD2 + " TEXT, " + FIELD3
            + " TEXT, " + FIELD4 + " TEXT);";


    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
