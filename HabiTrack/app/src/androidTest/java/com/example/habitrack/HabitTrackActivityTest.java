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


    /**
     * Will test an Invalid Type because User had not entered required fields
     * @throws Exception
     */
    public void signUp() throws Exception {
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "gitrekt");
        solo.clickOnView(solo.getView(R.id.button9));
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong activity, not main activity", MainActivity.class);
    }

    public void testInvalidType() throws Exception {
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "Harjot");
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);

        // Click on Create Button redirects to -> Habit Type Creation page
        solo.clickOnView(solo.getView(R.id.button));
        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.clickOnView(solo.getView(R.id.button6));
        solo.waitForText("Invalid Creation"); /* Asserts that the User has failed to create a new Habit Type */
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
    }

    /**
     * Will test that the User has created a proper Habit Type
     * and is able to create a Habit Event
     * @throws Exception
     */
    public void testValidType() throws Exception {
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.button));
        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText3), "test title");
        solo.enterText((EditText) solo.getView(R.id.editText4), "test Reason");
        solo.enterText((EditText) solo.getView(R.id.editText5), "11/11/2017");
        solo.clickOnView(solo.getView(R.id.sunday));
        solo.clickOnView(solo.getView(R.id.monday));
        solo.clickOnView(solo.getView(R.id.tuesday));
        solo.clickOnView(solo.getView(R.id.wednesday));
        solo.clickOnView(solo.getView(R.id.thursday));
        solo.clickOnView(solo.getView(R.id.friday));
        solo.clickOnView(solo.getView(R.id.saturday));
        solo.clickOnView(solo.getView(R.id.button6));
        assert(solo.waitForText("Invalid Creation") == FALSE); /* Asserts that the User has created a new Habit Type */
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
        solo.clickInList(1);
        solo.waitForActivity(NewHabitEventActivity.class);
        solo.assertCurrentActivity("Failed to Go to new Habit Event", NewHabitEventActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText7), "test event");
        solo.clickOnView(solo.getView(R.id.checkBox));
    }







    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }}




