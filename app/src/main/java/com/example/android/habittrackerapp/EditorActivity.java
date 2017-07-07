package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittrackerapp.data.HabitTrackerHelper;

import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.ACTIVITY_JOGGING;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.ACTIVITY_NONE;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.ACTIVITY_SWIMMING;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.ACTIVITY_WALKING;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_ACTIVITY;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_HEADACHE;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_INFORMATION;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_SLEEP;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.COLUMN_HABIT_STEPS;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.HEADACHE_NO;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.HEADACHE_YES;
import static com.example.android.habittrackerapp.data.HabitTrackerContract.HabitEntry.TABLE_NAME;

public class EditorActivity extends AppCompatActivity {

    //declare the views
    private EditText mNumberOfSteps;
    private EditText mHoursOfSleep;
    private Spinner mPhysicalActivity;
    private Spinner mHeadacheReport;
    private EditText mInformation;

    //declare the db Helper
    private HabitTrackerHelper mDbHelper;

    //spinner selections
    private int mActivity;
    private int mHeadache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNumberOfSteps = (EditText) findViewById(R.id.edit_number_steps);
        mHoursOfSleep = (EditText) findViewById(R.id.edit_hours_of_sleep);
        mPhysicalActivity = (Spinner) findViewById(R.id.spinner_activity);
        mHeadacheReport = (Spinner) findViewById(R.id.spinner_headache);
        mInformation = (EditText) findViewById(R.id.edit_new_information);

        //initialize the dbHelper
        mDbHelper = new HabitTrackerHelper(this);

        //setup spinners
        setupPhysicalActivitySpinner();
        setupHeadacheSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the kind of activity (walking, jogging and so on)
     */
    private void setupPhysicalActivitySpinner() {

        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter physicalActivitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_activity_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        physicalActivitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mPhysicalActivity.setAdapter(physicalActivitySpinnerAdapter);

        // Set the integer mActivity to the constant values
        mPhysicalActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.activity_unknown))) {
                        mActivity = ACTIVITY_NONE; // No activity
                    } else if (selection.equals(getString(R.string.activity_walking))) {
                        mActivity = ACTIVITY_WALKING; // Walking
                    } else if (selection.equals(getString(R.string.activity_jogging))) {
                        mActivity = ACTIVITY_JOGGING; // Jogging
                    } else {
                        mActivity = ACTIVITY_SWIMMING; // Swimming
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mActivity = ACTIVITY_NONE; // No activity
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the absence or presence of headache
     */
    private void setupHeadacheSpinner() {

        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter headacheSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_headache_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        headacheSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mHeadacheReport.setAdapter(headacheSpinnerAdapter);

        // Set the integer mHeadache to the constant values
        mHeadacheReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.yes))) {
                        mHeadache = HEADACHE_YES; // Headache this date
                    } else {
                        mHeadache = HEADACHE_NO; // No headache
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mHeadache = HEADACHE_NO; // No headache
            }
        });
    }

    //insert data into the database
    private void insertData() {

        int numberOfSteps;
        //check if the user has entered smth that could be considered a number
        try {
            numberOfSteps = Integer.parseInt(mNumberOfSteps.getText().toString().trim());
        } catch (java.lang.NumberFormatException e) {
            //if the user has entered nothing or smth that is not number, consider the value to be zero
            numberOfSteps = 0;
        }

        int hoursOfSleep;
        //cheack if the user has entered smth that could be considered a number
        try {
            hoursOfSleep = Integer.parseInt(mHoursOfSleep.getText().toString().trim());
        } catch (java.lang.NumberFormatException e) {
            //if the user has entered nothing or smth that is not number, consider the value to be zero
            hoursOfSleep = 0;
        }

        String information = mInformation.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        //add values
        values.put(COLUMN_HABIT_SLEEP, hoursOfSleep);
        values.put(COLUMN_HABIT_STEPS, numberOfSteps);
        values.put(COLUMN_HABIT_HEADACHE, mHeadache);
        values.put(COLUMN_HABIT_ACTIVITY, mActivity);
        values.put(COLUMN_HABIT_INFORMATION, information);

        // Insert the new row, returning the primary key value of the new row
        long entry_id = db.insert(
                TABLE_NAME,
                null,
                values);

        //Little toast message to make sure everything is ok
        if (entry_id < 0) {
            Toast.makeText(this, "Error creating a record" + entry_id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "New record added with id" + entry_id, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Insert the data
                insertData();
                finish();

                return true;

            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.action_save, menu);
        return true;
    }
}
