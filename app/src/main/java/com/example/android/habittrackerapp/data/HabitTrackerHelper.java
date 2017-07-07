package com.example.android.habittrackerapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_ACTIVITY;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_HEADACHE;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_INFORMATION;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_SLEEP;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_STEPS;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.TABLE_NAME;


/**
 * Created by maria on 07.07.2017.
 */

public class HabitTrackerHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "HabitTracker.db";

    //constants to create a table
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_HABIT_STEPS + INT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_SLEEP + INT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_ACTIVITY + INT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_HEADACHE + INT_TYPE + COMMA_SEP +
                    COLUMN_HABIT_INFORMATION + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    //constructor
    public HabitTrackerHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //overriding on create method
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    //overriding onUpdate method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    //override onDowngrade method
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
