package com.example.habitrack;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Created by yonaelbekele on 2017-10-22.
 */

public class HabitTrackActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
    private Solo solo;
    public HabitTrackActivityTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    public void testStart() throws Exception {
        Activity activity = getActivity();
    }
    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();

        // Test habit

        // Test create habit

        // Test create habit event
    }


/*
    public HabiTrackActivityTest() {
        super(ca.ualberta.cs.habitrack.HabiTrackActivity.class);
    }

    public void setUp() throws Exception{
        solo = new Solo.getInstrumentation(), getActivity();
    }
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }

        @Override
    public void tearDown() throws Exception {
            solo.finishOpenedActivities();

            // Test habit

            // Test create habit

            // Test create habit event
        }
        */

}
