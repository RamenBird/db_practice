package com.ramenbird.example.bigfireball;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ramenbird.GeneratedSqlClass;

/**
 * Created by RamenBird on 2017/1/12.
 */

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    private final static String DB_NAME = "a.db";
    private final static int DB_VERSION = 1;

    public MyDatabaseOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        GeneratedSqlClass.createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
