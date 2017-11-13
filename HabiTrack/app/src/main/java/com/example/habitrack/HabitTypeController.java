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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;

import java.lang.reflect.Type;
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
     * @param title
     * @param reason
     * @param startDate
     * @param schedule
     */
    public void createNewHabitType(String title, String reason,
                                   Calendar startDate, ArrayList<Integer> schedule) {
        // Generate the new habit type
        HabitType ht = new HabitType(HabitTypeStateManager.getHTStateManager().getHabitTypeID());
        // Save updated htID
        saveHTID();
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
        loadHTDate();
        HabitTypeStateManager.getHTStateManager().calculateHabitsForToday();
        Calendar today = Calendar.getInstance();
        Calendar htDate = HabitTypeStateManager.getHTStateManager().getHabitTypeDate();
        //if(htDate.compareTo(today) < 0){
        if(htDate.get(Calendar.DATE) < today.get(Calendar.DATE)
                && htDate.get(Calendar.MONTH) <= today.get(Calendar.MONTH)
                && htDate.get(Calendar.YEAR) <= today.get(Calendar.YEAR)){
            saveHTDate();
            ArrayList<HabitType> recent = HabitTypeStateManager.getHTStateManager().getHabitTypesForToday();
            for(Integer count = 0; count < recent.size(); count++){
                incrementHTMaxCounter(recent.get(count).getID());
            }
        }
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
            loadedDate = Calendar.getInstance();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        HabitTypeStateManager.getHTStateManager().setHabitTypeDate(loadedDate);
    }

}
