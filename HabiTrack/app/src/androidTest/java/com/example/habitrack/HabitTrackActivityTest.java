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
    Populates listviews for Testing
     */
    public void testA() throws Exception {
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "yonael");
        solo.clickOnView(solo.getView(R.id.button9));
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Wrong activity, not main activity", MainActivity.class);

        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.clickOnView(solo.getView(R.id.createHabitButton));
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText3), "test set up ");
        solo.enterText((EditText) solo.getView(R.id.editText4), "test first reason");
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

        solo.waitForActivity(NewHabitTypeActivity.class);
        solo.clickOnView(solo.getView(R.id.createHabitButton));
        solo.assertCurrentActivity("Failed to Switch to NewHabitTypeActivity",NewHabitTypeActivity.class);
        solo.enterText((EditText) solo.getView(R.id.editText3), "2nd set up ");
        solo.enterText((EditText) solo.getView(R.id.editText4), "test second reason");
        solo.clickOnText("Select Date");
        solo.setDatePicker(0, 2017, 11, 16);
        solo.clickOnText("Done");
        solo.clickOnView(solo.getView(R.id.sunday));
        solo.clickOnView(solo.getView(R.id.monday));
        solo.clickOnView(solo.getView(R.id.tuesday));
        solo.clickOnView(solo.getView(R.id.wednesday));
        solo.clickOnView(solo.getView(R.id.thursday));
        solo.clickOnView(solo.getView(R.id.friday));
        solo.clickOnView(solo.getView(R.id.saturday));
        solo.clickOnView(solo.getView(R.id.button6));

        solo.clickOnView(solo.getView(R.id.button10));
    }


    /**
     * Tests invalid user name
     * @throws Exception
     */
    public void testAtInvalidLogin() throws Exception {
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "Don't Exist");
        solo.clickOnView(solo.getView(R.id.button5));
        solo.waitForText("No existing user. Please Sign Up!");
    }

    /**
     *  Tests valid User
     * @throws Exception
     */
    public void testAtLogin() throws Exception {
        solo.clickOnView(solo.getView(R.id.editText));
        solo.enterText(0, "yonael");
        solo.clickOnView(solo.getView(R.id.button9));
        solo.clickOnView(solo.getView(R.id.button5));
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
        solo.clickOnView(solo.getView(R.id.createHabitButton));
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
    }

    // Assumes that at 0 in the ListView Habit doesn't have an Event
    public void testHabitEvent() throws Exception {
        // Entering the NewHabitEvent
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
        solo.clickInList(0);
        solo.waitForActivity(NewHabitEventActivity.class);
        solo.assertCurrentActivity("Failed to Go to new Habit Event", NewHabitEventActivity.class);
        solo.enterText((EditText) solo.getView(R.id.heCommentBox), "test event");
        solo.clickOnView(solo.getView(R.id.addEventButton));
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Failed to Switch back to MainActivity", MainActivity.class);
    }

    /**
     * Tests history, making sure items are selectable
     * @throws Exception
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
        solo.waitForActivity(AllHabitTypesActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not all types activity", AllHabitTypesActivity.class);
        solo.clickInList(0);
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

    public void testSocial() throws Exception {
        solo.assertCurrentActivity("Wrong Activity, not main activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.button4));
        solo.waitForActivity(SocialActivity.class);
        solo.assertCurrentActivity("Wrong Activity, not social activity", SocialActivity.class);
        solo.clickOnView(solo.getView(R.id.friendsButton));
        solo.waitForActivity(MyFollowers.class);
        solo.assertCurrentActivity("Wrong Activity, not myFollowers class", MyFollowers.class);
    }



    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }}





