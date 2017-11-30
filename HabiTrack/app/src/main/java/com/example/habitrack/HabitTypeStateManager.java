package com.example.habitrack;

import java.util.ArrayList;
import java.util.Calendar;

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
    private static Calendar habitTypeDate;
    public static HabitTypeStateManager htManager = new HabitTypeStateManager();

    private static ArrayList<HabitType> ALL_HABITTYPES = new ArrayList<HabitType>();
    private static ArrayList<HabitType> HABITTYPES_FOR_TODAY = new ArrayList<HabitType>();
    // ArrayList to store the Metadata for the HabitType
    private static ArrayList<HabitTypeMetadata> htMetadata = new ArrayList<HabitTypeMetadata>();


    private HabitTypeStateManager(){}

    public static HabitTypeStateManager getHTStateManager(){
        return htManager;
    }

    public static void calculateHabitsForToday(){
        // If no habit types defined, then return empty list for today
        if(ALL_HABITTYPES.isEmpty()){
            HABITTYPES_FOR_TODAY = new ArrayList<HabitType>();
        } else {
            // Get today
            Calendar cal = Calendar.getInstance();
            // Otherwise
            HABITTYPES_FOR_TODAY.clear();       // clear the current list
            for (Integer count = 0; count < ALL_HABITTYPES.size(); count++) {
                // for all the habit types
                Calendar currCal = ALL_HABITTYPES.get(count).getStartDate();    // get date for habit type
                // If the date for the habit type is before the current day
                if (currCal.compareTo(cal) <= 0) {
                    // Then get the schedule for the habit, and compare each day to today
                    ArrayList<Integer> schedule = ALL_HABITTYPES.get(count).getSchedule();
                    for (Integer cnt = 0; cnt < schedule.size(); cnt++) {
                        if (schedule.get(cnt) == cal.get(Calendar.DAY_OF_WEEK)) {
                            // If today matches a day, then add to habit types for today
                            HABITTYPES_FOR_TODAY.add(ALL_HABITTYPES.get(count));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void addMetadata(HabitType ht){
        this.htMetadata.add(ht.getMyData());
    }

    public static ArrayList<HabitTypeMetadata> getHtMetadata() {
        return htMetadata;
    }

    public static void setHtMetadata(ArrayList<HabitTypeMetadata> htMetadata) {
        HabitTypeStateManager.htMetadata = htMetadata;
    }

    public ArrayList<HabitType> getAllHabitTypes(){
        return ALL_HABITTYPES;
    }

    public void setAllHabittypes(ArrayList<HabitType> allHabittypes){
        this.ALL_HABITTYPES = allHabittypes;
    }

    public ArrayList<HabitType> getHabitTypesForToday(){
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
    }

    public HabitType getHabitType(Integer requestedID){
        for(Integer count = 0; count < HABITTYPES_FOR_TODAY.size(); count++){
            if(HABITTYPES_FOR_TODAY.get(count).getID() == requestedID){
                return HABITTYPES_FOR_TODAY.get(count);
            }
        }
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

    public void addHabitTypeForToday(HabitType habitType){
        HABITTYPES_FOR_TODAY.add(habitType);
    }

    public Calendar getHabitTypeDate() {
        return habitTypeDate;
    }

    public void setHabitTypeDate(Calendar habitTypeDate) {
        HabitTypeStateManager.habitTypeDate = habitTypeDate;
    }

    public void setHabitTypesForToday(ArrayList<HabitType> htForToday){
        HABITTYPES_FOR_TODAY = htForToday;
    }

    public void setID(Integer savedID){
        HabitTypeStateManager.habitTypeID = savedID;
    }

    public Integer getIDToSave(){
        return HabitTypeStateManager.habitTypeID;
    }

    public Integer getHabitTypeID(){
        if(habitTypeID >= 0){
            HabitTypeStateManager.habitTypeID++;
            return HabitTypeStateManager.habitTypeID;
        } else {
            return 0;
        }
    }
}
