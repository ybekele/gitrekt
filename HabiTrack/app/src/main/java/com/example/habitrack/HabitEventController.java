package com.example.habitrack;


import java.util.Calendar;

import java.util.ArrayList;

/**
 * HabitEventController
 *
 * Version 1.0
 *
 * Created by sshussai on 10/21/17.
 */

public class HabitEventController {
    /**
     * This class is the main interface for the habit event entity. It can create a new habit event
     * and delete it, given a corresponding habit type
     *
     */

    public HabitEventController(){}

    public void createNewHabitEvent(Integer habitTypeID){
        HabitTypeController htc = new HabitTypeController();
        HabitEvent he = new
                HabitEvent(HabitEventStateManager.getHEStateManager().getHabitEventID(), habitTypeID);
        he.setTitle(htc.getHabitTitle(habitTypeID));
        HabitEventStateManager.getHEStateManager().storeHabitEvent(he);
    }

    public void createNewHabitEvent(Integer habitTypeID, String comment){
        HabitTypeController htc = new HabitTypeController();
        HabitEvent he = new
                HabitEvent(HabitEventStateManager.getHEStateManager().getHabitEventID(), habitTypeID);
        he.setTitle(htc.getHabitTitle(habitTypeID));
        he.setComment(comment);
        HabitEventStateManager.getHEStateManager().storeHabitEvent(he);
    }

    public void deleteHabitEvent(Integer requestedID) {
        HabitEventStateManager.getHEStateManager().removeHabitEvent(requestedID);

    }

    public HabitEvent getHabitEvent(Integer requestedID) {
        HabitEvent he = HabitEventStateManager.getHEStateManager().getHabitEvent(requestedID);
        return he;
    }

    public void editHabitEventTitle(Integer requestedID, String newTitle){
        HabitEvent he = HabitEventStateManager.getHEStateManager().getHabitEvent(requestedID);

        if(he != null){
            he.setTitle(newTitle);
        }
    }

    public void editHabitEventComment(Integer requestedID, String newComment){
        HabitEvent he = HabitEventStateManager.getHEStateManager().getHabitEvent(requestedID);
        if(he != null){
            he.setComment(newComment);
        }
    }

    public void editHabitEventDate(Integer requestedID, Calendar newDate){
        HabitEvent he = HabitEventStateManager.getHEStateManager().getHabitEvent(requestedID);
        if(he != null){
            he.setDate(newDate);

        }
    }

}
