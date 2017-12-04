package com.example.habitrack;

import android.test.AndroidTestCase;

/**
 * Created by sshussai on 11/4/17.
 */

public class HabitTypeControllerTest extends AndroidTestCase {

//    // Properties of a test habit type
//    private String title;
//    private String reason;
//    private Calendar startDate;
//    private ArrayList<Integer> schedule = new ArrayList<Integer>();
//
//    // Test Habit type
//    HabitType ht;
//    HabitType newht;
//    HabitTypeController htc;
//    Context ctx;
//
//
//    /**
//     *  Function that runs before any tests. Creates a basic habit type
//     *  for any simple testing
//     */
//    protected void setUp(){
//        this.ctx = getContext();
//        this.htc = new HabitTypeController(this.ctx);
//        //this.htc.loadHTID();
//        HabitTypeStateManager.getHTStateManager().setID(0);
//        //htc.loadFromFile();
//        //HabitTypeStateManager.getHTStateManager().setHabitTypeDate(Calendar.getInstance());
//        this.htc.deleteAllHabitTypes();
//        this.title = "testTitle1";
//        this.reason = "testReason1";
//        this.startDate = Calendar.getInstance();
//        this.schedule.add(this.startDate.get(Calendar.DAY_OF_WEEK));
//        //startDate.add(Calendar.DATE, -2);
//        this.htc.createNewHabitType(title, reason, startDate, schedule, Boolean.TRUE, null);
//        this.ht = HabitTypeStateManager.getHTStateManager().getHabitType(1);
//    }
//
//    /**
//     * This function validates the basic habit type created
//     * by the setup function
//     */
//    public void testCreateHabit(){
//        assertEquals(this.title, this.ht.getTitle());
//        assertEquals(this.reason, this.ht.getReason());
//        assertTrue(this.startDate.equals(this.ht.getStartDate()));
//        assertTrue(this.schedule.equals(this.ht.getSchedule()));
//    }
//
//    /**
//     * This function will change the habit title of  basic
//     * habit type. And then it restores the title
//     */
//    public void testChangeHabitTitle(){
//        String newTitle = "changedTitle";
//        this.ht.setTitle(newTitle);
//        assertEquals(newTitle, this.ht.getTitle());
//        this.ht.setTitle(this.title);
//        assertEquals(this.title, this.ht.getTitle());
//    }
//
//    /**
//     * This function will change the reason of the basic
//     * habit type, and restores it, and verifies each change
//     */
//    public void testChangeHabitReason(){
//        String newReason = "changedReason";
//        this.ht.setReason(newReason);
//        assertEquals(newReason, this.ht.getReason());
//        this.ht.setReason(this.reason);
//        assertEquals(this.reason, this.ht.getReason());
//    }
//
//    /**
//     * This function will set the date of the basic habit type
//     * to 2 days behind, and then check the date, and then restore
//     * the date
//     */
//    public void testChangeStartDate(){
//        Calendar newCal = Calendar.getInstance();
//        newCal.add(Calendar.DATE, -2);
//        this.ht.setStartDate(newCal);
//        assertTrue(this.ht.getStartDate().equals(newCal));
//        this.ht.setStartDate(this.startDate);
//        assertTrue(this.ht.getStartDate().equals(this.startDate));
//    }
//
//    /**
//     * This function will check if the basic habit type is also
//     * in the habit types for today
//     */
//    public void testHabitsForToday1(){
//        this.htc.generateHabitsForToday();
//        ArrayList<HabitType> habits = this.htc.getHabitTypesForToday();
//        assertTrue(habits.contains(this.ht));
//    }
//
//    /**
//     * This function will change the day so that it is not the current day,
//     * and then generate the habit types for today. The basic habit type should
//     * not be in the array
//     */
//    public void testHabitsForToday2(){
//        ArrayList<Integer> newSchedule = new ArrayList<>();
//        newSchedule.add(100);
//        this.ht.setSchedule(newSchedule);
//        assertTrue(this.ht.getSchedule().equals(newSchedule));
//        this.htc.generateHabitsForToday();
//        ArrayList<HabitType> habits = this.htc.getHabitTypesForToday();
//        assertFalse(habits.contains(this.ht));
//    }
//    /**
//     * This function verifies that the allHabitTypes list is
//     * updated every time a new habit gets created
//     */
//
//    public void testgetAllHabits(){
//        ArrayList<HabitType> habits = new ArrayList<HabitType>();
//        assertEquals(habits.size(), 0);
//        habits = htc.getAllHabitTypes();
//        assertEquals(habits.size(), 1);
//        htc.createNewHabitType("testTitle2", reason, startDate, schedule);
//        assertEquals(habits.size(), 2);
//
//    }
//
//    /**
//     * This function will verify if the amx counter of the habit type
//     * is updated upon a new occurrence
//     */
//    public void testMaxCounterIncrement(){
//        // Set the date of the habit type to 3 weeks ago
//        Calendar newCal = Calendar.getInstance();
//        newCal.add(Calendar.DATE, -21);
//        this.ht.setStartDate(newCal);
//        assertTrue(this.ht.getStartDate().equals(newCal));
//        // set the occurence measured date to earlier than 3 weeks
//        Calendar newDate = Calendar.getInstance();
//        newDate.add(Calendar.DATE, -22);
//        HabitTypeStateManager.getHTStateManager().setHabitTypeDate(newDate);
//        this.htc.saveHTDate();
//        // Check first occurrence
//        this.htc.generateHabitsForToday();
//        assertEquals(1, this.htc.getMaxCounter(1).intValue());
//        // Set the date of the current ht to two weeks ago
//        newCal.add(Calendar.DATE, 7);
//        this.ht.setStartDate(newCal);
//        assertTrue(this.ht.getStartDate().equals(newCal));
//        // reset the occurrences measured date to earlier than 2 weeks
//        newDate.add(Calendar.DATE, 7);
//        HabitTypeStateManager.getHTStateManager().setHabitTypeDate(newDate);
//        this.htc.saveHTDate();
//        // check 2nd occurrence
//        this.htc.generateHabitsForToday();
//        assertEquals(2, this.htc.getMaxCounter(1).intValue());
//        // Set the date of the current ht to one week ago
//        newCal.add(Calendar.DATE, 7);
//        this.ht.setStartDate(newCal);
//        assertTrue(this.ht.getStartDate().equals(newCal));
//        // reset the occurrences measured date to earlier than 1 week
//        newDate.add(Calendar.DATE, 7);
//        HabitTypeStateManager.getHTStateManager().setHabitTypeDate(newDate);
//        this.htc.saveHTDate();
//        // check third occurrence
//        this.htc.generateHabitsForToday();
//        assertEquals(3, this.htc.getMaxCounter(1).intValue());
//    }
//
//    public void testSaveChanges(){
//        String changedTitle = "changedTitle";
//        String changedReason = "changedReason";
//        ArrayList<Integer> plan = new ArrayList<>();
//        plan.add(100);
//        this.htc.editHabitTypeTitle(1, changedTitle);
//        this.htc.editHabitTypeReason(1, changedReason);
//        this.htc.editHabitTypeSchedule(1, plan);
//        this.htc.incrementHTMaxCounter(1);
//        this.htc.incrementHTCurrentCounter(1);
//        this.htc.loadFromFile();
//
//    }
//
//    // Fucntion that runs after all tests
//    protected void tearDown(){}


}