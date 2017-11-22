package com.example.habitrack;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

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

/**
 * This class is the main interface for the habit type entity. It can create a new habit type
 * and delete it. It can also edit the title, reason, start date or schedule for a given
 * habit type.
 * Added load and save functions
 * Added load and save functions for ID too
 */
public class HabitTypeController {
    private Context ctx;
    private final String FILE_NAME = "habitTypes.sav";
    private final String ID_FILE_NAME = "htid.sav";
    private final String DATE_FILE_NAME = "htdate.sav";

    public HabitTypeController(Context givenContext){
        this.ctx = givenContext;
    }

    /**
     *
     * This method creates a new habit type object, given its name, reason
     * start date, and ArrayList of Integers representing the days of the week for
     * its schedule
     *
     * @param title : Title of the habit
     * @param reason : Reason for making the Habit
     * @param startDate : the date that the Habit has been started
     * @param schedule : the days the user wants to be do the Habit
     */
    public void createNewHabitType(String title, String reason,
                                   Calendar startDate, ArrayList<Integer> schedule) {
        // Generate the new habit type
        HabitType ht = new HabitType(HabitTypeStateManager.getHTStateManager().getHabitTypeID());
        saveHTID();                     // Save updated htID
        ht.setTitle(title);             // Set title
        ht.setReason(reason);           // Set reason
        ht.setStartDate(startDate);     // Set start date
        ht.setSchedule(schedule);       // Set schedule
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
     * @return the list of all habit types
     */
    public ArrayList<HabitType> getAllHabitTypes(){
        return HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
    }

    /**
     * This function calculates all the habit types for the current day. It also
     * update the max occurrence counters for all habit types who have a new occurence
     * on the current day, by checking a saved date from when it last increased the
     * max occurrences.
     */
    public void generateHabitsForToday(){
        // Calculate the list for today
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        // Check if we need to update the occurrence counters for habit types
        // if it's a new day
        loadHTDate();
        Calendar today = Calendar.getInstance();
        Calendar htDate = HabitTypeStateManager.getHTStateManager().getHabitTypeDate();
        // If the stored date is less than current date, and stored month and year
        // are less than or equal to current month and year respectively, then increase)
        if(htDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)
                || (htDate.get(Calendar.YEAR) <= today.get(Calendar.YEAR)
                && htDate.get(Calendar.MONTH) < today.get(Calendar.MONTH))
                || (htDate.get(Calendar.MONTH) <= today.get(Calendar.MONTH)
                && htDate.get(Calendar.YEAR) <= today.get(Calendar.YEAR)
                && htDate.get(Calendar.DATE) < today.get(Calendar.DATE))){
            HabitTypeStateManager.getHTStateManager().setHabitTypeDate(today);
            saveHTDate();
            ArrayList<HabitType> recent = HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
            for(Integer count = 0; count < recent.size(); count++){
                incrementHTMaxCounter(recent.get(count).getID());
            }
        }
    }

    /**
     * This function returns the list of habit types for today
     * @return the list of habit types for today
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
     * @param requestedID the ID of the HabitType you wish to delete
     */
    public void deleteHabitType(Integer requestedID) {
        HabitTypeStateManager.getHTStateManager().removeHabitType(requestedID);
        saveToFile();
    }

    /**
     * Given an ID of a habit type, this method
     * gets it from the local storage
     * @param requestedID the ID of the habit type you wish to get
     * @return
     */
    public HabitType getHabitType(Integer requestedID) {
        HabitType ht = HabitTypeStateManager.getHTStateManager().getHabitType(requestedID);
        return ht;
    }

    /**
     * get the habit type from elastic search
     * @return ht, the habit type
     */
    public ArrayList<HabitType> getHabitTypeElasticSearch() {
        ArrayList<HabitType> ht = new ArrayList<>();
        ElasticSearchController.GetHabitType getHabitType = new ElasticSearchController.GetHabitType();
        getHabitType.execute("");
        try {
            ht = getHabitType.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets from the async object");
        }
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
     * edits the title, if the habit exists
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
     * edits the start date
     * @param requestedID the habit type id
     * @param newDate the new date you wish to insert
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
     * Given an ID of a habit type and a new schedule, this method
     * edits the schedule, if the habit exists
     * @param requestedID ID of the habit type you wish to edit
     * @param newSchedule the new schedule you'd like the habit type to follow
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
     * @param requestedID ID of the habit type you wish to get the title for
     * @return the title of the Habit Type given
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
     * @param requestedID ID of the habit type you wish to get the reason for
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
     * @param requestedID ID of the habit type you wish to get the start date for
     * @return the start date
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
     * @param requestedID the habit type you wish to get the scheudle for
     * @return the schedule
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
     * @param requestedID ID of the habit type you wish to get
     * @return Integer, times completed
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
     * @param requestedID ID of the habit type you wish to get max counter for
     * @return integer of the max counter
     */
    public Integer getMaxCounter(Integer requestedID) {
        Integer ctr = -1;
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            ctr = ht.getCurrentMaxCounter();
        }
        return ctr;
    }

    /**
     * increments the current counter
     * @param requestedID ID of the habit type you wish to get
     */
    public void incrementHTCurrentCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            ht.incrementCompletedCounter();
        }
        saveToFile();
    }

    /**
     * increment the max counter
     * @param requestedID ID of the habit type you wish to get
     */
    public void incrementHTMaxCounter(Integer requestedID) {
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if (!ht.getID().equals(-1)) {
            ht.incrementMaxCounter();
        }
        saveToFile();
    }

    /**
     * loads from the file
     */
    public void loadFromFile() {
        ArrayList<HabitType> habits;
        try {
            FileInputStream fis = ctx.openFileInput(FILE_NAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            Type listType = new TypeToken<ArrayList<HabitType>>(){}.getType();
            habits = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            habits = new ArrayList<HabitType>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        HabitTypeStateManager.getHTStateManager().setAllHabittypes(habits);
    }

    public void saveToFile() {
        ArrayList<HabitType> habits = getAllHabitTypes();
        try {
            FileOutputStream fos = ctx.openFileOutput(FILE_NAME,0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(habits, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void saveHTID(){
        Integer saveID = HabitTypeStateManager.getHTStateManager().getIDToSave();

        try {
            FileOutputStream fos = ctx.openFileOutput(ID_FILE_NAME,0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(saveID, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void loadHTID() {
        Integer loadedID;
        try {
            FileInputStream fis = ctx.openFileInput(ID_FILE_NAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            //Type intType = new TypeToken<ArrayList<Integer>>(){}.getType();
            //loadedArray = gson.fromJson(in, intType);
            loadedID = gson.fromJson(in, Integer.class);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            loadedID = 0;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //throw new RuntimeException();
            loadedID = 0;
        }
        HabitTypeStateManager.getHTStateManager().setID(loadedID);
    }

    public void saveHTDate(){
        Calendar saveDate = HabitTypeStateManager.getHTStateManager().getHabitTypeDate();

        try {
            FileOutputStream fos = ctx.openFileOutput(DATE_FILE_NAME,0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(saveDate, writer);
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void loadHTDate() {
        Calendar loadedDate;
        try {
            FileInputStream fis = ctx.openFileInput(DATE_FILE_NAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            Type calType = new TypeToken<Calendar>(){}.getType();
            loadedDate = gson.fromJson(in, calType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            Calendar newDate = Calendar.getInstance();
            newDate.set(Calendar.DATE, 1);
            newDate.set(Calendar.MONTH, 1);
            newDate.set(Calendar.YEAR, 1);
            loadedDate = newDate;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        HabitTypeStateManager.getHTStateManager().setHabitTypeDate(loadedDate);
    }

}
