package com.example.habitrack;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sshussai on 11/4/17.
 */

public class HabitTypeControllerTest extends TestCase {

    // Properties of a test habit type
    private String title;
    private String reason;
    private Calendar startDate;
    private ArrayList<Integer> schedule;

    // Test Habit type
    HabitType ht;
    HabitType newht;

    // Function that runs before any tests
    protected void setUp(){
        this.title = "testTitle1";
        this.reason = "testReason1";
        this.startDate = Calendar.getInstance();
        this.schedule = new ArrayList<Integer>();
        this.schedule.add(Calendar.SUNDAY);
        this.schedule.add(Calendar.MONDAY);
    }

    public void testCreateHabit(){
        HabitTypeController htc = new HabitTypeController();
        htc.createNewHabitType(title, reason, startDate, schedule);
        this.ht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
        assertEquals(title, this.ht.getTitle());
        assertEquals(reason, this.ht.getReason());
        assertTrue(startDate.equals(this.ht.getStartDate()));
        assertTrue(schedule.equals(this.ht.getSchedule()));

    }

    public void testDeleteHabit(){
        HabitTypeStateManager.getHTStateManager().removeHabitType(1);
        newht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
        Integer test = -1;
        Integer retID = newht.getID();
        assertEquals(test, retID);
    }

    // Fucntion that runs after all tests
    protected void tearDown(){}


}