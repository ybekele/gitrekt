package com.example.habitrack;

/**
 *
 * HabitTypeController
 *
 * Version 1.0
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
        HabitType ht = new HabitType(4);
        ht.setTitle(title);
        ht.setReason(reason);
        ht.setStartDate(startDate);
        ht.setSchedule(schedule);

    }

    public void delete(Integer requestedID) {
        HabitTypeManager.getHTManager().removeHabitType(requestedID);

    }

    public HabitType viewDetails(Integer requestedID) {
        HabitType ht = HabitTypeManager.getHTManager().getHabitType(requestedID);
        return ht;
    }

    public static void editHabitTypeTitle(Integer requestedID, String newTitle){
        HabitType ht = HabitTypeManager.getHTManager().getHabitType(requestedID);

        if(ht != null){
            ht.setTitle(newTitle);
        }
    }

    public static void editHabitTypeReason(Integer requestedID, String newReason){
        HabitType ht = HabitTypeManager.getHTManager().getHabitType(requestedID);
        if(ht != null){
            ht.setReason(newReason);
        }
    }

    public static void editHabitTypeStartDate(Integer requestedID, Calendar newDate){
        HabitType ht = HabitTypeManager.getHTManager().getHabitType(requestedID);
        if(ht != null){
            ht.setStartDate(newDate);
        }
    }

    public static void editHabitTypeSchedule(Integer requestedID, ArrayList<Integer> newSchedule){
        HabitType ht = HabitTypeManager.getHTManager().getHabitType(requestedID);
        if(ht != null){
            ht.setSchedule(newSchedule);
        }
    }

}



