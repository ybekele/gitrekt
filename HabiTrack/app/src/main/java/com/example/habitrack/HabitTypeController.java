package com.example.habitrack;

import android.content.Context;
import android.provider.CalendarContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.runner.TestRunListener;

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
    private FileManager fileManager;
    private final String FILE_NAME = "habitTypes.sav";
    private final String ID_FILE_NAME = "htid.sav";
    private final String DATE_FILE_NAME = "htdate.sav";

    public HabitTypeController(Context givenContext){
        this.ctx = givenContext;
        this.fileManager = new FileManager(givenContext);
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
                                   Calendar startDate, ArrayList<Integer> schedule, Boolean isConnected, String userID) {
        // Generate the new habit type
        HabitType ht = new HabitType(HabitTypeStateManager.getHTStateManager().getHabitTypeID());
        saveHTID();                     // Save updated htID
        ht.setUserID(userID);           // Set userID
        ht.setTitle(title);             // Set title
        ht.setReason(reason);           // Set reason
        ht.setStartDate(startDate);     // Set start date
        ht.setSchedule(schedule);       // Set schedule
        // Save the metadata of the HT
        HabitTypeStateManager.getHTStateManager().addMetadata(ht);
        // If connected to internet, then add the ht to es
        if(isConnected) {
            ElasticSearchController.AddHabitType addHabitType = new ElasticSearchController.AddHabitType();
            addHabitType.execute(ht);
        }
        // save the metadata
        fileManager.save(fileManager.HT_METADATA_MODE);
        // Check if an event needs to be created
        Integer today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(schedule.contains(today)){
            HabitTypeStateManager.getHTStateManager().addHabitTypeForToday(ht);
            HabitEventController hec = new HabitEventController(ctx);
            hec.createNewHabitEvent(ht.getID(), isConnected, userID);
        }
        // Add the habit type to the event state manager
        HabitTypeStateManager.getHTStateManager().storeHabitType(ht);
        // Save to local
        saveToFile();
    }

    public void getElasticSearchIDs(){

    }


    /**
     * This function returns the list of all habit types
     * @return the list of all habit types
     */
    public ArrayList<HabitType> getAllHabitTypes(){
        return HabitTypeStateManager.getHTStateManager().getAllHabitTypes();
    }

    /**
     * This function is needed
     */
    public void generateHabitsForToday(Boolean isConnected, String userID){
        // load previous date
        loadHTDate();
        // get today's date, and previous date
        Calendar today = Calendar.getInstance();
        Calendar htDate = HabitTypeStateManager.getHTStateManager().getHabitTypeDate();
        //TEMP --- ONLY FOR TESTING AND ENSURING IT WORKS
        // htDate.add(Calendar.DAY_OF_MONTH, -1);
        // if loaded date is behind today's date
        if(htDate.get(Calendar.YEAR) < today.get(Calendar.YEAR)
                || (htDate.get(Calendar.YEAR) <= today.get(Calendar.YEAR)
                && htDate.get(Calendar.MONTH) < today.get(Calendar.MONTH))
                || (htDate.get(Calendar.MONTH) <= today.get(Calendar.MONTH)
                && htDate.get(Calendar.YEAR) <= today.get(Calendar.YEAR)
                && htDate.get(Calendar.DATE) < today.get(Calendar.DATE))){
            // Calculate the list for today
            // HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
            // Save the new date
            HabitTypeStateManager.getHTStateManager().setHabitTypeDate(today);
            saveHTDate();
            // get he controller
            HabitEventController hec = new HabitEventController(ctx);
            ArrayList<HabitType> recent;
            recent = HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
            for(HabitType ht : recent){
                hec.createNewHabitEvent(ht.getID(), isConnected, userID);
            }
        }
    }

    public void setHabitTypeMostRecentEvent(Integer requestedID, HabitEvent he){
        HabitType ht = this.getHabitType(requestedID);
        // If the habit exists
        if(!ht.getID().equals(-1)){
            ht.setMostRecentEvent(he);
        }
        saveToFile();
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
//                    String query = "{\n" +
//                    "  \"query\": { \"match_all\": {} },\n" +
//                    "  \"sort\": { \"date\": { \"order\": \"desc\" } },\n" +
//                    "  \"size\": 10\n" +
//                    "}";
        String text = "ssh200";
        String query = "{\n" + " \"query\": { \"term\": {\"title\":\"" + text + "\"} }\n" + "}";
        ElasticSearchController.GetHabitType getHabitType = new ElasticSearchController.GetHabitType();
        getHabitType.execute(text);
        try {
            ht = getHabitType.get();
        } catch (Exception e) {
            Log.i("Error", "Failed to get the tweets from the async object");
        }
        return ht;
    }

    public void addHabitTypeToElasticSearch(HabitType ht){
        ElasticSearchController.AddHabitType addHabitType = new ElasticSearchController.AddHabitType();
        addHabitType.execute(ht);
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
        ArrayList<HabitType> htForToday = new ArrayList<HabitType>();
        Integer today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        for(HabitType ht : habits){
            ArrayList<Integer> schedule = ht.getSchedule();
            if(schedule.contains(Calendar.getInstance().get(Calendar.DAY_OF_WEEK))){
                htForToday.add(ht);
            }
        }
        HabitTypeStateManager.getHTStateManager().setHabitTypesForToday(htForToday);
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
