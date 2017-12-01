package com.example.habitrack;

import android.content.Context;
import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sshussai on 11/13/17.
 */

/**
 * Unit testing for HabitEventController
 */
public class HabitEventControllerTest extends AndroidTestCase {

    // Properties of a test habit type
    private String comment;

    // Test Habit type
    HabitEvent he;
    HabitEvent secondhe;
    HabitEventController hec;
    HabitTypeController htc;
    Context ctx;

    /**
     *  Function that runs before any tests. Creates a basic habit type
     *  for any simple testing
     */
    protected void setUp(){
        this.ctx = getContext();
        this.hec = new HabitEventController(this.ctx);
        this.htc = new HabitTypeController(this.ctx);
        //hec.loadHEID();
        HabitEventStateManager.getHEStateManager().setID(0);
        HabitTypeStateManager.getHTStateManager().setID(0);
        //hec.loadFromFile();
        // Make test habit type to make events for it
        String htTitle = "htTitle1";
        String htReason = "htReason1";
        Calendar htDate = Calendar.getInstance();
        htDate.add(Calendar.DATE, -14);
        ArrayList<Integer> htSchedule = new ArrayList<>();
        htSchedule.add(htDate.get(Calendar.DAY_OF_WEEK));
        htc.createNewHabitType(htTitle, htReason,htDate, htSchedule);
        // make first test habit event with no comment
        hec.createNewHabitEvent(1);
        // make 2nd test habit event with comment for same habit type
        this.comment = "testComment1";
        hec.editHabitEventComment(1, comment);
        // set pointers for the two habit events
        this.he = hec.getHabitEvent(1);
        this.secondhe = hec.getHabitEvent(2);
        hec.editHabitEventComment(2, comment);
    }

    /**
     * This function verifies that the two events have been created
     * properly by ensuring that the 2nd event can be accessed, and ensuring
     * that the completed counter for the habit type is incremented both times
     */
    public void testCreateHabitEvent(){
        assertEquals(this.secondhe.getComment(), comment);
        assertEquals(2, this.htc.getCompletedCounter(1).intValue());
    }
    protected void tearDown(){}

}
