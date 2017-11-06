package com.example.habitrack;

import java.util.Calendar;

import java.util.ArrayList;

/**
 * HabitEventStateManager
 *
 * Version 1.0
 *
 * Created by sshussai on 11/5/17.
 */

public class HabitEventStateManager {
    /**
     * Version 1.0
     * This class is the state manager for all Habit event related entities
     *
     */

    private static Integer habitEventID;

    public static HabitEventStateManager heManager = new HabitEventStateManager();

    //private static final ArrayList<Integer> ALL_HABITEVENTS_ID = new ArrayList<Integer>();
    private static final ArrayList<HabitEvent> ALL_HABITEVENTS = new ArrayList<HabitEvent>();
    private static final ArrayList<HabitEvent> RECENT_HABITEVENTS = new ArrayList<HabitEvent>();


    private HabitEventStateManager(){
        this.habitEventID = 0;
    }

    public static HabitEventStateManager getHEStateManager(){
        return heManager;
    }

    public void updateRecentHabitEvents(){
        // Get date from 4 weeks ago, by adding -28 to today's date
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -28);
        // Check if any currently recent events are old
        for(Integer count = 0; count < RECENT_HABITEVENTS.size(); count++){
            Calendar currCal = RECENT_HABITEVENTS.get(count).getDate();
            if (currCal.before(cal)){
                RECENT_HABITEVENTS.remove(count.intValue());
            }

        }
    }

    public void storeHabitEvent(HabitEvent he){
        RECENT_HABITEVENTS.add(he);
        ALL_HABITEVENTS.add(he);
        //ALL_HABITEVENTS_ID.add(he.getHabitEventID());
    }

    public HabitEvent getHabitEvent(Integer requestedID){
        for(Integer count = 0; count < RECENT_HABITEVENTS.size(); count++){
            if(RECENT_HABITEVENTS.get(count).getHabitEventID() == requestedID){
                return RECENT_HABITEVENTS.get(count);
            }
        }
        for(Integer count = 0; count < ALL_HABITEVENTS.size(); count++){
            if(ALL_HABITEVENTS.get(count).getHabitEventID() == requestedID){
                return ALL_HABITEVENTS.get(count);
            }
        }
        HabitEvent he = new HabitEvent(-1, -1);
        return he;
    }


    public void removeHabitEvent(Integer requestedID){
        for(Integer count = 0; count < RECENT_HABITEVENTS.size(); count++){
            if(RECENT_HABITEVENTS.get(count).getHabitEventID() == requestedID){
                RECENT_HABITEVENTS.remove(count.intValue());
            }
        }
        for(Integer count = 0; count < ALL_HABITEVENTS.size(); count++){
            if(ALL_HABITEVENTS.get(count).getHabitEventID() == requestedID){
                ALL_HABITEVENTS.remove(count.intValue());
            }
        }
        /*
        for(Integer count = 0; count < ALL_HABITEVENTS_ID.size(); count++){
            if(ALL_HABITEVENTS_ID.get(count) == requestedID){
                ALL_HABITEVENTS_ID.remove(count.intValue());
            }
        }
        */
    }

    public Integer getHabitEventID(){
        HabitEventStateManager.habitEventID++;
        return HabitEventStateManager.habitEventID;
    }


}
