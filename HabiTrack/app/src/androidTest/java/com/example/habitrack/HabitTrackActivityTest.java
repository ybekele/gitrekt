package com.example.habitrack;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

import static java.lang.Boolean.FALSE;

/**
 * Created by yonaelbekele on 2017-10-22.
 */

public class HabitTrackActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{
    private Solo solo;


    public HabitTrackActivityTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
/*
    public void testStart() throws Exception {
        Activity activity = getActivity();
    }*/



    public void testInvalidType() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.button));
        //solo.clickOnButton(solo.getString(R.string.button));


        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.clickOnView(solo.getView(R.id.button6));
        solo.waitForText("Invalid Creation");
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
    }

    public void testValidType() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.button));
        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText3), "test title");
        solo.enterText((EditText) solo.getView(R.id.editText4), "test Reason");
        solo.enterText((EditText) solo.getView(R.id.editText5), "7/10/2018");
        solo.clickOnView(solo.getView(R.id.button6));
        assert(solo.waitForText("Invalid Creation") == FALSE);
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }}




