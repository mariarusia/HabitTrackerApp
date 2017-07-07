package com.example.android.habittrackerapp.data;

import android.provider.BaseColumns;

/**
 * Created by maria on 07.07.2017.
 * a contact class that defines the habits database
 */

public final class HabitTrackerContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public HabitTrackerContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class HabitEntry implements BaseColumns {
        public static final String TABLE_NAME = "habits";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HABIT_STEPS = "number_of_steps";
        public static final String COLUMN_HABIT_SLEEP = "hours_of_sleep";
        public static final String COLUMN_HABIT_ACTIVITY = "physical_activity";
        public static final String COLUMN_HABIT_HEADACHE = "headache";
        public static final String COLUMN_HABIT_INFORMATION = "new_information";

        //specific constants
        //ACTIVITY
        public static final int ACTIVITY_NONE = 0;
        public static final int ACTIVITY_JOGGING = 1;
        public static final int ACTIVITY_SWIMMING = 2;
        public static final int ACTIVITY_WALKING = 3;

        //HEADACHE
        public static final int HEADACHE_YES = 1;
        public static final int HEADACHE_NO = 0;

    }


}
