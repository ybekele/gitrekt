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
        ht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
        assertEquals(title, ht.getTitle());
        assertEquals(reason, ht.getReason());
        assertTrue(startDate.equals(ht.getStartDate()));
        assertTrue(schedule.equals(ht.getSchedule()));
    }

    // Fucntion that runs after all tests
    protected void tearDown(){}


}