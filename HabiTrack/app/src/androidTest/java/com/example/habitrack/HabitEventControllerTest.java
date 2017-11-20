package com.example.habitrack;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sshussai on 11/13/17.
 */

public class HabitEventControllerTest extends TestCase {

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
        this.ctx = InstrumentationRegistry.getContext();
        this.hec = new HabitEventController(this.ctx);
        this.htc = new HabitTypeController(this.ctx);
        hec.loadHEID();
        hec.loadFromFile();
        ArrayList<Integer> schedule = new ArrayList<>();
        schedule.add(Calendar.MONDAY);
        htc.createNewHabitType("title", "reason", Calendar.getInstance(), schedule);
        this.comment = "testComment1";
        hec.createNewHabitEvent(1);
        hec.createNewHabitEvent(1, comment);
        this.he = hec.getHabitEvent(1);
        this.secondhe = hec.getHabitEvent(2);
    }

    public void testCreateHabitEvent(){
        assertEquals(this.secondhe.getComment(), comment);
    }

    protected void tearDown(){}

}
