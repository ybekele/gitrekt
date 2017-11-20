package com.example.habitrack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import junit.framework.TestCase;

import java.lang.reflect.Array;
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
    private ArrayList<Integer> schedule = new ArrayList<Integer>();

    // Test Habit type
    HabitType ht;
    HabitType newht;
    HabitTypeController htc;
    Context ctx;


    /**
     *  Function that runs before any tests. Creates a basic habit type
     *  for any simple testing
     */
    protected void setUp(){
        this.ctx = InstrumentationRegistry.getContext();
        this.htc = new HabitTypeController(this.ctx);
        htc.loadHTID();
        htc.loadFromFile();
        this.title = "testTitle1";
        this.reason = "testReason1";
        this.startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -2);
        //this.schedule.add(startDate.get(Calendar.DAY_OF_WEEK));
        this.schedule.add(Calendar.MONDAY);
        htc.createNewHabitType(title, reason, startDate, schedule);
        this.ht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
    }

    /**
     * This function validates the basic habit type created
     * by the setup function
     */
    public void testCreateHabit(){
        assertEquals(this.title, this.ht.getTitle());
        assertEquals(this.reason, this.ht.getReason());
        assertTrue(this.startDate.equals(this.ht.getStartDate()));
        assertTrue(this.schedule.equals(this.ht.getSchedule()));
    }

    /**
     * This function will change the habit title of  basic
     * habit type. And then it restores the title
     */
    public void testChangeHabitTitle(){
        String newTitle = "changedTitle";
        this.ht.setTitle(newTitle);
        assertEquals(newTitle, this.ht.getTitle());
        this.ht.setTitle(this.title);
        assertEquals(this.title, this.ht.getTitle());
    }

    /**
     * This function will change the reason of the basic
     * habit type, and restores it, and verifies each change
     */
    public void testChangeHabitReason(){
        String newReason = "changedReason";
        this.ht.setReason(newReason);
        assertEquals(newReason, this.ht.getReason());
        this.ht.setReason(this.reason);
        assertEquals(this.reason, this.ht.getReason());
    }

    /**
     * This function will set the date of the basic habit type
     * to 2 days behind, and then check the date, and then restore
     * the date
     */
    public void testChangeStartDate(){
        Calendar newCal = Calendar.getInstance();
        newCal.add(Calendar.DATE, -2);
        this.ht.setStartDate(newCal);
        assertTrue(this.ht.getStartDate().equals(newCal));
        this.ht.setStartDate(this.startDate);
        assertTrue(this.ht.getStartDate().equals(this.startDate));
    }

    /**
     * This function will check if the basic habit type is also
     * in the habit types for today
     */
    public void testHabitsForToday1(){
        this.htc.generateHabitsForToday();
        ArrayList<HabitType> habits = this.htc.getHabitTypesForToday();
        assertTrue(habits.contains(this.ht));
    }

    /**
     * This function verifies that the allHabitTypes list is
     * updated every time a new habit gets created
     */

    public void testgetAllHabits(){
        ArrayList<HabitType> habits = new ArrayList<HabitType>();
        assertEquals(habits.size(), 0);
        habits = htc.getAllHabitTypes();
        assertEquals(habits.size(), 1);
        htc.createNewHabitType("testTitle2", reason, startDate, schedule);
        assertEquals(habits.size(), 2);

    }

    // Fucntion that runs after all tests
    protected void tearDown(){}


}