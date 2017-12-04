package com.example.habitrack;

import android.support.annotation.ArrayRes;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * HabitEventStateManager
 *
 * Version 2.0
 *
 * Created by sshussai on 11/5/17.
 */

public class HabitEventStateManager {
    /**
     * Version 2.0
     * This class is the state manager for all Habit event related entities
     * Added load and save functions for events, and for habitevent ID
     *
     */

    private static Integer habitEventID;
    private static HabitEventStateManager heManager = new HabitEventStateManager();

    private static ArrayList<HabitEvent> ALL_HABITEVENTS = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> RECENT_HABITEVENTS = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> HABITEVENTS_FOR_TODAY = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> recentHabitEvents = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> heForToday = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> newOfflineHE = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> completedOfflineHE = new ArrayList<HabitEvent>();
    private static ArrayList<HabitEvent> editedOfflineHE = new ArrayList<HabitEvent>();


    public HabitEventStateManager(){}

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

    public void addCompletedOfflineHE(HabitEvent he){
        completedOfflineHE.add(he);
    }

    public ArrayList<HabitEvent> getCompletedOfflineHE() {
        return completedOfflineHE;
    }

    public void setCompletedOfflineHE(ArrayList<HabitEvent> completedOfflineHE) {
        HabitEventStateManager.completedOfflineHE = completedOfflineHE;
    }

    public void setNewOfflineHE(ArrayList<HabitEvent> newOfflineHE) {
        HabitEventStateManager.newOfflineHE = newOfflineHE;
    }

    public void setEditedOfflineHE(ArrayList<HabitEvent> editedOfflineHE) {
        HabitEventStateManager.editedOfflineHE = editedOfflineHE;
    }

    public ArrayList<HabitEvent> getNewOfflineHE() {
        return newOfflineHE;
    }

    public ArrayList<HabitEvent> getEditedOfflineHE() {
        return editedOfflineHE;
    }

    public void addNewOfflineHE(HabitEvent he){
        newOfflineHE.add(he);
    }

    public void addEditedOfflineHE(HabitEvent he){
        editedOfflineHE.add(he);
    }

    // add to today's habit event list
    public void addTodayHabitEvent(HabitEvent he){
        heForToday.add(he);
    }

    // add to recent events list
    public void addRecentEvent(HabitEvent he){
        recentHabitEvents.add(he);
    }

    // get all habit events for today
    public ArrayList<HabitEvent> getTodayHabitevents(){
        return heForToday;
    }

    // get all recent habit events
    public ArrayList<HabitEvent> getRecentHabitevents(){
        return recentHabitEvents;
    }

    // set all recent habit events
    public void setRecentHabitEvents(ArrayList<HabitEvent> recentEvents){
        this.recentHabitEvents = recentEvents;
    }

    // set all recent habit events
    public void setTodayHabitEvents(ArrayList<HabitEvent> todayEvents){
        this.heForToday = todayEvents;
    }

    // setter and getter for HABITEVENTS_FOR_TODAY
    public ArrayList<HabitEvent> getALlHabitEventsForToday() {
        return HABITEVENTS_FOR_TODAY;
    }

    public void setAllHabitEventsForToday(ArrayList<HabitEvent> habiteventsForToday) {
        HABITEVENTS_FOR_TODAY = habiteventsForToday;
    }

    public ArrayList<HabitEvent> getAllHabitEvents(){
        return ALL_HABITEVENTS;
    }

    public void setAllHabitEvents(ArrayList<HabitEvent> allEvents){
        this.ALL_HABITEVENTS = allEvents;
    }

    public void removeAllHabitEvents(){
        this.ALL_HABITEVENTS.clear();
    }

    public void removeRecentHabitEvents(){
        this.RECENT_HABITEVENTS.clear();;
    }

    public void storeHabitEvent(HabitEvent he){
        HABITEVENTS_FOR_TODAY.add(he);
        ALL_HABITEVENTS.add(he);
        Log.d("seen",ALL_HABITEVENTS.toString());
//        ALL_HABITEVENTS_ID.add(he.getHabitEventID());
    }

    public HabitEvent getHabitEvent(Integer requestedID){
        // Check today's habit events
        for(HabitEvent he : heForToday){
            if(he.getHabitEventID() == requestedID){
                return he;
            }
        }
        // Otherwise, check recent habit events
        for(HabitEvent he : recentHabitEvents){
            if(he.getHabitEventID() == requestedID){
                return he;
            }
        }
        // Otherwise, check all habit events on elastic search
        ArrayList<HabitEvent> heList = new ArrayList<HabitEvent>();
        ElasticSearchController.GetHabitEvent getHabitEvent = new ElasticSearchController.GetHabitEvent();
        getHabitEvent.execute();
        try {
            heList = getHabitEvent.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(heList.size() > 0){
            return heList.get(0);
        }
        // If habit event doesn't exist, return placeholder
        HabitEvent he = new HabitEvent(-1, -1, "");
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

    public void setID(Integer savedID){
        HabitEventStateManager.habitEventID = savedID;
        Log.d("mm","last test "+ savedID.toString());
    }

    public Integer getIDToSave(){
        return HabitEventStateManager.habitEventID;
    }

    public Integer getHabitEventID(){
        HabitEventStateManager.habitEventID++;
        Log.d("tag", habitEventID.toString());
        return HabitEventStateManager.habitEventID;
    }


}
