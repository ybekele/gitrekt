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


    // Function that runs before any tests
    protected void setUp(){
        this.ctx = InstrumentationRegistry.getContext();
        this.htc = new HabitTypeController(this.ctx);
        htc.loadHTID();
        htc.loadFromFile();
        this.title = "testTitle1";
        this.reason = "testReason1";
        this.startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -2);
        this.schedule.add(Calendar.MONDAY);

    }

//    public void testCreateHabit(){
//        htc.createNewHabitType(title, reason, startDate, schedule);
//        this.ht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
//        Log.d("T:tch:title", ht.getTitle());
//        Log.d("T:tch:Reason", ht.getReason());
//        Log.d("T:tch:Schedule", ht.getSchedule().toString());
//        Log.d("T:tch:StartDate", ht.getStartDate().toString());
//        assertEquals(title, this.ht.getTitle());
//        assertEquals(reason, this.ht.getReason());
//        assertTrue(startDate.equals(this.ht.getStartDate()));
//        assertTrue(schedule.equals(this.ht.getSchedule()));
//
//    }

//    public void testgetAllHabits(){
//        ArrayList<HabitType> habits = new ArrayList<HabitType>();
//        assertEquals(habits.size(), 0);
//        htc.createNewHabitType(title, reason, startDate, schedule);
//        habits = htc.getAllHabitTypes();
//        Log.d("T:tgah", habits.get(0).toString());
//        assertEquals(habits.size(), 1);
//        htc.createNewHabitType("testTitle2", reason, startDate, schedule);
//        Log.d("T:tgah", habits.get(1).toString());
//        assertEquals(habits.size(), 2);
//
//    }

    public void testHabitsForToday(){
        ArrayList<HabitType> habits = new ArrayList<HabitType>();
        ArrayList<HabitType> ht = new ArrayList<HabitType>();
        assertEquals(habits.size(), 0);
        htc.createNewHabitType(title, reason, startDate, schedule);
        habits = htc.getAllHabitTypes();
        //Log.d("T:thft:1", habits.get(0).toString());
        assertEquals(habits.size(), 1);
        htc.createNewHabitType("testTitle2", reason, Calendar.getInstance(), schedule);
        //Log.d("T:thft:2", habits.get(1).toString());
        assertEquals(habits.size(), 2);
        // ---
        ht = htc.getHabitTypesForToday();
        //assertEquals(ht.size(), 0);
        htc.generateHabitsForToday();
        Log.d("T:thft:3", ht.get(0).toString());
        //assertEquals(ht.size(), 1);
    }


    // Fucntion that runs after all tests
    protected void tearDown(){}


}