package com.example.habitrack;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.searchbox.annotations.JestId;

/**
 *
 * HabitTypeController
 *
 * Version 2.2
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
     * Added load and save functions
     * Added load and save functions for ID too
     */

    private Context ctx;
    private final String FILE_NAME = "habitTypes.sav";
    private final String ID_FILE_NAME = "hdid.sav";

    public HabitTypeController(Context givenContext){
        this.ctx = givenContext;
    }

    /**
     *
     * This method creates a new habit type object, given its name, reason
     * start date, and ArrayList of Integers representing the days of the week for
     * its schedule
     *
     * @param title
     * @param reason
     * @param startDate
     * @param schedule
     */
    public void createNewHabitType(String title, String reason,
                                   Calendar startDate, ArrayList<Integer> schedule) {
        // Generate the new habit type
        HabitType ht = new HabitType(HabitTypeStateManager.getHTStateManager().getHabitTypeID());
        // Set its attributes
        ht.setTitle(title);
        ht.setReason(reason);
        ht.setStartDate(startDate);
        ht.setSchedule(schedule);
        // Add the habit type to the event state manager
        HabitTypeStateManager.getHTStateManager().storeHabitType(ht);
        // Add the habit type to elastic search
        ElasticSearchController.AddHabitType addHabitType = new ElasticSearchController.AddHabitType();
        addHabitType.execute(ht);
        // Save to local
        saveToFile();
    }


    /**
     * This function returns the list of all habit types
     * @return
     */
    public ArrayList<HabitType> getAllHabitTypes(){
        return HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
    }

    public void generateHabitsForToday(){
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
    }

    /**
     * This function returns the list of habit types for today
     * @return
     */
    public ArrayList<HabitType> getHabitTypesForToday(){
        return HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
    }

    /**
     * This function deletes all habit types
     */
    public void deleteAllHabitTypes(){
        HabitTypeStateManager.getHTStateManager().removeAllHabitTypes();
        HabitTypeStateManager.getHTStateManager().removeHabitTypesForToday();
        saveToFile();
    }

    /**
     * this function deletes all the habit types scheduled for today
     */
    public void deleteHabitTypesForToday(){
        HabitTypeStateManager.getHTStateManager().removeHabitTypesForToday();
    }

    /**
     * Given an ID of a habit type, this method
     * deletes it from the local storage
     * @param requestedID
     */
    public void deleteHabitType(Integer requestedID) {
        HabitTypeStateManager.getHTStateManager().removeHabitType(requestedID);
        saveToFile();
    }

    /**
     * Given an ID of a habit type, this method
     * gets it from the local storage
     * @param requestedID
     * @return
     */
    public HabitType getHabitType(Integer requestedID) {
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        return ht;
    }

    /**
     * Given an ID of a habit type and a new title, this method
     * edits the title, if the habit exists
     * @param requestedID
     * @param newTitle
     */
    public void editHabitTypeTitle(Integer requestedID, String newTitle){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)){
            ht.setTitle(newTitle);
        }
        saveToFile();
    }

    /**
     * Given an ID of a habit type and a new reason, this method
     * edits the reason, if the habit exists
     * @param requestedID
     * @param newReason
     */
    public void editHabitTypeReason(Integer requestedID, String newReason){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)){
            ht.setReason(newReason);
        }
        saveToFile();
    }

    /**
     * Given an ID of a habit type and a new date, this method
     * edits the start date, if the habit exists
     * @param requestedID
     * @param newDate
     */
    public void editHabitTypeStartDate(Integer requestedID, Calendar newDate){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)){
            ht.setStartDate(newDate);
        }
        saveToFile();
    }

    /**
     * Given an ID of a habit type and a new list
     * representing the habit schedule, this method edits the schedule, if the habit exists
     * @param requestedID
     * @param newSchedule
     */
    public void editHabitTypeSchedule(Integer requestedID, ArrayList<Integer> newSchedule){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)){
            ht.setSchedule(newSchedule);
        }
        saveToFile();
    }

    /**
     * Given an ID of a habit type, this method return the habit's title, if it exists
     * @param requestedID
     * @return
     */
    public String getHabitTitle(Integer requestedID){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)) {
            return ht.getTitle();
        } else {
            // Otherwise return an empty string
            return "";
        }
    }

    /**
     * Given an ID of a habit type, this method return the habit's reason, if it exists
     * @param requestedID
     * @return
     */
    public String getHabitReason(Integer requestedID){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)) {
            return ht.getReason();
        } else {
            // Otherwise return an empty string
            return "";
        }
    }

    /**
     * Given an ID of a habit type, this method return the habit's start date, if it exists
     * Otherwise, it returns today's date with year = -1
     * @param requestedID
     * @return
     */
    public Calendar getStartDate (Integer requestedID){
        HabitType ht = this.getHabitType(requestedID);
        Calendar cal = Calendar.getInstance();
        // If the habit exists
        if(!ht.getID().equals(-1)) {
            cal = ht.getStartDate();
        } else {
            // Otherwise, return today's date with year = -1
            cal.set(Calendar.YEAR, -1);
        }
        return cal;
    }

    /**
     * Given an ID of a habit type, this method return the habit's schedule, if it exists
     * Otherwise, it returns an empty array
     * @param requestedID
     * @return
     */
    public ArrayList<Integer> getSchedule (Integer requestedID){
        HabitType ht = this.getHabitType(requestedID);
        ArrayList<Integer> schedule = new ArrayList<Integer>();
        // If the habit exists
        if(!ht.getID().equals(-1)) {
            schedule = ht.getSchedule();
        }
        return schedule;
    }

    /**
     * Given an ID of a habit type, this method return the habit's completed counter,
     * if it exists. Otherwise, it returns -1
     * @param requestedID
     * @return
     */
    public Integer getCompletedCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            return ht.getCompletedCounter();
        } else {
            // Otherwise return an empty string
            return -1;
        }
    }

    /**
     * Given an ID of a habit type, this method return the habit's max counter,
     * if it exists. Otherwise, it returns -1
     * @param requestedID
     * @return
     */
    public Integer getMaxCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            return ht.getCurrentMaxCounter();
        } else {
            // Otherwise return an empty string
            return -1;
        }
    }

    public void incrementHTCurrentCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            ht.incrementCompletedCounter();
        }
        saveToFile();
    }

    public void incrementHTMaxCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            ht.incrementMaxCounter();
        }
        saveToFile();
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        ArrayList<HabitType> htList = new ArrayList<HabitType>();
        try {
            FileInputStream fis = ctx.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();

            if (o instanceof ArrayList) {
                htList = (ArrayList<HabitType>) o;
            } else {
                Log.i("HabiTrack HT:", "Error casting");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "File Not Found");
            HabitTypeStateManager.getHTStateManager().setAllHabittypes(htList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "IOException");
            HabitTypeStateManager.getHTStateManager().setAllHabittypes(htList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "ClassNotFound");
            HabitTypeStateManager.getHTStateManager().setAllHabittypes(htList);
        }
        HabitTypeStateManager.getHTStateManager().setAllHabittypes(htList);
    }

    public void saveToFile() {
        ArrayList<HabitType> htList = getAllHabitTypes();
        try {
            FileOutputStream fos = ctx.openFileOutput(FILE_NAME, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(htList);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Save", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Save", "IO Exception");
        }
    }

    public void saveHTID(){
        Integer saveID = HabitTypeStateManager.getHTStateManager().getIDToSave();
        try {
            FileOutputStream fos = ctx.openFileOutput(ID_FILE_NAME, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(saveID);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:SaveID", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:SaveID", "IO Exception");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadHTID() {
        //ArrayList<HabitType> htList = new ArrayList<HabitType>();
        Integer loadedID = 0;
        try {
            FileInputStream fis = ctx.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();

            if (o instanceof ArrayList) {
                //htList = (ArrayList<HabitType>) o;
                loadedID = (Integer) o;
            } else {
                Log.i("HabiTrack HT:", "Error casting");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "IOException");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HT:Load", "ClassNotFound");
        }
        HabitTypeStateManager.getHTStateManager().setID(loadedID);
    }
}
