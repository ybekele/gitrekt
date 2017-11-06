package com.example.habitrack;

import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * HabitTypeController
 *
 * Version 1.2
 *
 * Created by sshussai on 10/21/17.
 *
 *
 */

public class HabitTypeController {

    /**
     * This class is the main interface for the habit type entity. It can create a new habit type
     * and delete it. It can also edit the title, reason, start date or schedule for a given
     * habit type.
     *
     */

    public HabitTypeController(){}

    public void createNewHabitType(String title, String reason,
                                   Calendar startDate, ArrayList<Integer> schedule) {
        HabitType ht = new
                HabitType(HabitTypeStateManager.getHTStateManager().getHabitTypeID());
        ht.setTitle(title);
        ht.setReason(reason);
        ht.setStartDate(startDate);
        ht.setSchedule(schedule);
        HabitTypeStateManager.getHTStateManager().storeHabitType(ht);

    }

    public void deleteHabitType(Integer requestedID) {
        HabitTypeStateManager.getHTStateManager().removeHabitType(requestedID);

    }

    public HabitType getHabitType(Integer requestedID) {
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        return ht;
    }

    public static void editHabitTypeTitle(Integer requestedID, String newTitle){
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);

        if(ht != null){
            ht.setTitle(newTitle);
        }
    }

    public static void editHabitTypeReason(Integer requestedID, String newReason){
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        if(ht != null){
            ht.setReason(newReason);
        }
    }

    public static void editHabitTypeStartDate(Integer requestedID, Calendar newDate){
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        if(ht != null){
            ht.setStartDate(newDate);
        }
    }

    public static void editHabitTypeSchedule(Integer requestedID, ArrayList<Integer> newSchedule){
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        if(ht != null){
            ht.setSchedule(newSchedule);
        }
    }

    public String getHabitTitle(Integer requestedID){
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        return ht.getTitle();
    }

}



