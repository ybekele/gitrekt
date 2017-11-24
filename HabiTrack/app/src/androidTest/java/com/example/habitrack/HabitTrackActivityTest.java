package com.example.habitrack;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;

/**
 * Created by yonaelbekele on 2017-10-22.
 */

/*
Testing for Activities on the HabiTrack app
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
     * Meant to test sign up
     * @throws Exception
     */
    public void testSignUp() throws Exception {
        /*
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "yonael");
        solo.clickOnView(solo.getView(R.id.button9));
        solo.clickOnView(solo.getView(R.id.button5));
        */
        solo.waitForActivity(MainActivity.class);

        solo.assertCurrentActivity("Wrong activity, not main activity", MainActivity.class);
    }

    /**
     * Tests handling of invalid Habit Type
     * @throws Exception
     */
    public void testInvalidType() throws Exception {
        /*
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "yonael");
        solo.clickOnView(solo.getView(R.id.button5));
        */
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);

        // Click on Create Button redirects to -> Habit Type Creation page
        solo.clickOnView(solo.getView(R.id.createHabitButton));
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
        /*
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForActivity(MainActivity.class);
        */
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.createHabitButton));

        // Entering The NewHabitTypeActivity
        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText3), "test title");
        solo.enterText((EditText) solo.getView(R.id.editText4), "test Reason");
        solo.clickOnText("Select Date");
        solo.setDatePicker(0, 2017, 10, 16);
        solo.clickOnText("Done");
        solo.clickOnView(solo.getView(R.id.sunday));
        solo.clickOnView(solo.getView(R.id.monday));
        solo.clickOnView(solo.getView(R.id.tuesday));
        solo.clickOnView(solo.getView(R.id.wednesday));
        solo.clickOnView(solo.getView(R.id.thursday));
        solo.clickOnView(solo.getView(R.id.friday));
        solo.clickOnView(solo.getView(R.id.saturday));
        solo.clickOnView(solo.getView(R.id.button6));
        assert(!solo.waitForText("Invalid Creation")); /* Asserts that the User has created a new Habit Type */
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
        solo.clickInList(1);

        // Entering the NewHabitEvent
        solo.waitForActivity(NewHabitEventActivity.class);
        solo.assertCurrentActivity("Failed to Go to new Habit Event", NewHabitEventActivity.class);
        solo.enterText((EditText) solo.getView(R.id.heCommentBox), "test event");
        solo.clickOnView(solo.getView(R.id.addEventButton));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
    }

    /*
    Tests history, making sure items are selectable
     */
    public void testHistory() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.historyButton));
        solo.waitForActivity(HistoryActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not habit history", HabitHistory.class);
        solo.clickInList(1);

    }

    /**
     * Tests the editing of the schedule in AllHabitType
     * @throws Exception
     */
    public void testSchedule() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.allButton));
        solo.clickInList(1);
        solo.waitForActivity(HabitTypeDetailsActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not habit type details", HabitTypeDetailsActivity.class);
        solo.clickOnView(solo.getView(R.id.satCheckBox));
        solo.clickOnView(solo.getView(R.id.monCheckBox));
        solo.clickOnView(solo.getView(R.id.tuesCheckBox));
        solo.clickOnView(solo.getView(R.id.wedCheckBox));
        solo.clickOnView(solo.getView(R.id.editSchedule));
    }

    /**
     * Makes sure that you can delete a Habit Type
     * @throws Exception
     */
    public void testRemoveType() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.allButton));
        solo.clickInList(1);
        solo.waitForActivity(HabitTypeDetailsActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not habit type details", HabitTypeDetailsActivity.class);
        solo.clickOnView(solo.getView(R.id.deleteButton));
        solo.waitForActivity(HabitTypeDetailsActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not habit type details", HabitTypeDetailsActivity.class);
    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }}




