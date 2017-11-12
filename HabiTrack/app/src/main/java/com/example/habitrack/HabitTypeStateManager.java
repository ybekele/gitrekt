package com.example.habitrack;

import android.util.Log;

import java.util.Calendar;

import java.util.ArrayList;

/**
 * HabitTypeStateManager
 *
 * Version 2.0
 *
 * Created by sshussai on 11/4/17.
 *
 */

public class HabitTypeStateManager {

    /**
     * This class is the state manager for all Habit Type related events. It contains a
     * list that has all the habit types created so far, along with all the habit types
     * for today.
     * It can add habit types to the list, retrieve and remove them, and calculate habit
     * types for today.
     * Added functions to set and get loaded or saved IDs
     *
     */

    private static Integer habitTypeID;
    private Calendar cal = Calendar.getInstance();
    public static HabitTypeStateManager htManager = new HabitTypeStateManager();

    private static ArrayList<HabitType> ALL_HABITTYPES = new ArrayList<HabitType>();
    private static ArrayList<HabitType> HABITTYPES_FOR_TODAY = new ArrayList<HabitType>();


    private HabitTypeStateManager(){}

    public static HabitTypeStateManager getHTStateManager(){
        return htManager;
    }

    public void calculateHabitsForToday(){
        HABITTYPES_FOR_TODAY.clear();
        for(Integer count = 0; count < ALL_HABITTYPES.size(); count++){
            Calendar currCal = ALL_HABITTYPES.get(count).getStartDate();
                if(currCal.get(Calendar.DAY_OF_WEEK) == cal.get(Calendar.DAY_OF_WEEK)){
                HABITTYPES_FOR_TODAY.add(ALL_HABITTYPES.get(count));
            }
        }
    }

    public ArrayList<HabitType> getAllHabitTypes(){
        return ALL_HABITTYPES;
    }

    public void setAllHabittypes(ArrayList<HabitType> allHabittypes){
        this.ALL_HABITTYPES = allHabittypes;
    }

    public ArrayList<HabitType> getHabitTypesForToday(){
        calculateHabitsForToday();
        return HABITTYPES_FOR_TODAY;
    }

    public void removeAllHabitTypes(){
        ALL_HABITTYPES.clear();
    }

    public void removeHabitTypesForToday(){
        HABITTYPES_FOR_TODAY.clear();
    }

    public void storeHabitType(HabitType ht){
        ALL_HABITTYPES.add(ht);


        HABITTYPES_FOR_TODAY.add(ht);

    }

    public HabitType getHabitType(Integer requestedID){
        for(Integer count = 0; count < ALL_HABITTYPES.size(); count++){
            if(ALL_HABITTYPES.get(count).getID() == requestedID){
                return ALL_HABITTYPES.get(count);
            }
        }
        HabitType ht = new HabitType(-1);
        return ht;
    }

    public void removeHabitType(Integer requestedID){
        for(Integer count = 0; count < ALL_HABITTYPES.size(); count++){
            if(ALL_HABITTYPES.get(count).getID() == requestedID){
                HabitType rmht = ALL_HABITTYPES.get(count);
                ALL_HABITTYPES.remove(rmht);
            }
        }
        for(Integer count = 0; count < HABITTYPES_FOR_TODAY.size(); count++){
            if(HABITTYPES_FOR_TODAY.get(count).getID() == requestedID){
                HabitType rmht = HABITTYPES_FOR_TODAY.get(count);
                HABITTYPES_FOR_TODAY.remove(rmht);
            }
        }
    }

    public void setID(Integer savedID){
        HabitTypeStateManager.habitTypeID = savedID;
    }

    public Integer getIDToSave(){
        return HabitTypeStateManager.habitTypeID;
    }

    public Integer getHabitTypeID(){
        HabitTypeStateManager.habitTypeID++;
        return HabitTypeStateManager.habitTypeID;
    }
}
