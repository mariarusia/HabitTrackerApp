package com.example.android.habittrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.habittrackerapp.data.HabitTrackerHelper;

import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_ACTIVITY;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_HEADACHE;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_INFORMATION;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_SLEEP;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_STEPS;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.TABLE_NAME;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry._ID;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayDatabaseInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert data" menu option
            case R.id.action_insert_data:
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    //reads data from the data base
    // input - database, return Cursor object
    private Cursor read(SQLiteDatabase db){

        // Perform this raw SQL query "SELECT * FROM habits"
        // to get a Cursor that contains all rows from the habits table.
        String[] projection = {
                _ID,
                COLUMN_HABIT_STEPS,
                COLUMN_HABIT_SLEEP,
                COLUMN_HABIT_ACTIVITY,
                COLUMN_HABIT_HEADACHE,
                COLUMN_HABIT_INFORMATION
        };

        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, null);

        return cursor;
    }
    /**
     * Helper method to display information in the onscreen TextView about the state of
     * the habits database. And create a cursor, so that the project met requirements
     */

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        HabitTrackerHelper mDbHelper = new HabitTrackerHelper(this);

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();


        TextView displayView = (TextView) findViewById(R.id.textview);

        Cursor cursor = read(db);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The habits table contains <number of rows in Cursor> pets.
            // _id - steps - sleep - activity - headache - information
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The habit table contains " + cursor.getCount() + " days.\n\n");
            displayView.append(_ID + " - " +
                    COLUMN_HABIT_STEPS + " - " +
                    COLUMN_HABIT_SLEEP + " - " +
                    COLUMN_HABIT_ACTIVITY + " - " +
                    COLUMN_HABIT_HEADACHE + " - " +
                    COLUMN_HABIT_INFORMATION + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(_ID);
            int stepsColumnIndex = cursor.getColumnIndex(COLUMN_HABIT_STEPS);
            int sleepColumnIndex = cursor.getColumnIndex(COLUMN_HABIT_SLEEP);
            int activityColumnIndex = cursor.getColumnIndex(COLUMN_HABIT_ACTIVITY);
            int headacheColumnIndex = cursor.getColumnIndex(COLUMN_HABIT_HEADACHE);
            int infoColumnIndex = cursor.getColumnIndex(COLUMN_HABIT_INFORMATION);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                int currentSteps = cursor.getInt(stepsColumnIndex);
                String currentInfo = cursor.getString(infoColumnIndex);
                int currentActivity = cursor.getInt(activityColumnIndex);
                int currentHeadache = cursor.getInt(headacheColumnIndex);
                int currentSleep = cursor.getInt(sleepColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentSteps + " - " +
                        currentSleep + " - " +
                        currentActivity + " - " +
                        currentHeadache + " - " +
                        currentInfo));
            }
        } finally {
            //close the cursor
            cursor.close();
        }
    }
}
