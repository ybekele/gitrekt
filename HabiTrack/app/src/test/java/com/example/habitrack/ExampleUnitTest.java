package com.example.habitrack;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    HabitEventController controller = new HabitEventController();
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        //testCreateEvent();
    }

    @Test
    public void testCreateEvent() throws Exception {
        controller.createHabitEvent("Title");
        assertTrue(controller.getTotalEvents() == 1);
        controller.deleteEvent();
    }

    @Test
    public void testTotalEvents() throws Exception {
        controller.createHabitEvent("Title");
        controller.createHabitEvent("Title2");
        controller.createHabitEvent("Title3");
        assertTrue(controller.getTotalEvents() == 3);
    }
}