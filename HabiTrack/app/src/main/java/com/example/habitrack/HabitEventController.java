package com.example.habitrack;


import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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

    private Context hectx;
    private final String FILE_NAME = "habitEvents.sav";
    private final String ID_FILE_NAME = "heid.sav";

    public HabitEventController(Context ctx){
        this.hectx = ctx;
    }

    public void createNewHabitEvent(Integer habitTypeID){
        HabitTypeController htc = new HabitTypeController(hectx);
        HabitEvent he = new
                HabitEvent(HabitEventStateManager.getHEStateManager().getHabitEventID(), habitTypeID);
        he.setTitle(htc.getHabitTitle(habitTypeID));
        HabitEventStateManager.getHEStateManager().storeHabitEvent(he);

        // Save event on elastic search
        ElasticSearchController.AddHabitEvent addHabitEvent = new ElasticSearchController.AddHabitEvent();
        addHabitEvent.execute(he);
        // Save event locally
        saveToFile();

    }

    public void createNewHabitEvent(Integer habitTypeID, String comment){
        HabitTypeController htc = new HabitTypeController(hectx);
        HabitEvent he = new
                HabitEvent(HabitEventStateManager.getHEStateManager().getHabitEventID(), habitTypeID);
        he.setTitle(htc.getHabitTitle(habitTypeID));
        he.setComment(comment);
        HabitEventStateManager.getHEStateManager().storeHabitEvent(he);
        // Save event on elastic search
        ElasticSearchController.AddHabitEvent addHabitEvent = new ElasticSearchController.AddHabitEvent();
        addHabitEvent.execute(he);
        // Save event locally
        saveToFile();
    }

    public ArrayList<HabitEvent> getAllHabitEvent(){
        return HabitEventStateManager.getHEStateManager().getAllHabitEvents();
    }

    public void updateRecentHabitEvents(){
        HabitEventStateManager.getHEStateManager().updateRecentHabitEvents();
    }

    /**
     * This function returns the list of recent events
     * @return
     */
    public ArrayList<HabitEvent> getRecentHabitEvents(){
        return HabitEventStateManager.getHEStateManager().getRecentHabitevents();
    }

    /**
     * This function deletes all habit events
     */
    public void deleteAllHabitTypes(){
        HabitEventStateManager.getHEStateManager().removeRecentHabitEvents();
        HabitEventStateManager.getHEStateManager().removeAllHabitEvents();
        // Save event locally
        saveToFile();
    }

    /**
     * this function deletes all the habit types scheduled for today
     */
    public void deleteHabitTypesForToday(){
        HabitTypeStateManager.getHTStateManager().removeHabitTypesForToday();
    }

    public void deleteHabitEvent(Integer requestedID) {
        HabitEventStateManager.getHEStateManager().removeHabitEvent(requestedID);
        // Save event locally
        saveToFile();
    }

    public HabitEvent getHabitEvent(Integer requestedID) {
        HabitEvent he = HabitEventStateManager.getHEStateManager().getHabitEvent(requestedID);
        return he;
    }

    public ArrayList<HabitEvent> getHabitEventElasticSearch(){

        ArrayList<HabitEvent> he = new ArrayList<>();
        ElasticSearchController.GetHabitEvent getHabitEvent = new ElasticSearchController.GetHabitEvent();
        getHabitEvent.execute("");
        try {
            he = getHabitEvent.get();
        }
        catch (Exception e)
        {
            Log.i("Error","Failed to get the tweets from the async object");
        }

        return he;
    }

    public void editHabitEventTitle(Integer requestedID, String newTitle){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            he.setTitle(newTitle);
            // Save event locally
            saveToFile();
        }
    }

    public void editHabitEventComment(Integer requestedID, String newComment){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            he.setComment(newComment);
            // Save event locally
            saveToFile();
        }
    }

    public void editHabitEventDate(Integer requestedID, Calendar newDate){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            he.setDate(newDate);
            // Save event locally
            saveToFile();
        }
    }

    public String getHabitEventTitle(Integer requestedID){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            return he.getTitle();
        } else {
            return "";
        }
    }

    public String getHabitEventComment(Integer requestedID){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            return he.getComment();
        } else {
            return "";
        }
    }

    public Calendar getHabitEventDate(Integer requestedID){
        HabitEvent he = this.getHabitEvent(requestedID);
        Calendar cal;
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            cal = he.getDate();
        } else {
            cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, -1);
        }
        return cal;
    }

    public Integer getHabitEventID(Integer requestedID){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            return he.getHabitEventID();
        } else {
            return -1;
        }
    }

    public Integer getCorrespondingHabitTypeID(Integer requestedID){
        HabitEvent he = this.getHabitEvent(requestedID);
        // If the habit event exists
        if(!he.getHabitEventID().equals(-1)){
            return he.getHabitTypeID();
        } else {
            return -1;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        ArrayList<HabitEvent> heList = new ArrayList<HabitEvent>();
        try {
            FileInputStream fis = hectx.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();

            if (o instanceof ArrayList) {
                heList = (ArrayList<HabitEvent>) o;
            } else {
                Log.i("HabiTrack HE:Load", "Error casting");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "File Not Found");
            HabitEventStateManager.getHEStateManager().setAllHabitEvents(heList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "IOException");
            HabitEventStateManager.getHEStateManager().setAllHabitEvents(heList);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "ClassNotFound");
            HabitEventStateManager.getHEStateManager().setAllHabitEvents(heList);
        }
        HabitEventStateManager.getHEStateManager().setAllHabitEvents(heList);
    }

    public void saveToFile() {
        ArrayList<HabitEvent> heList = getAllHabitEvent();
        try {
            FileOutputStream fos = hectx.openFileOutput(FILE_NAME, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(heList);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Save", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Save", "IO Exception");
        }
    }

    public void saveHEID(){
        Integer saveID = HabitEventStateManager.getHEStateManager().getIDToSave();
        try {
            FileOutputStream fos = hectx.openFileOutput(ID_FILE_NAME, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(saveID);

            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:SaveID", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:SaveID", "IO Exception");
        }
    }

    @SuppressWarnings("unchecked")
    public void loadHEID() {
        //ArrayList<HabitType> htList = new ArrayList<HabitType>();
        Integer loadedID = 0;
        try {
            FileInputStream fis = hectx.openFileInput(FILE_NAME);
            ObjectInputStream ois = new ObjectInputStream(fis);

            Object o = ois.readObject();

            if (o instanceof ArrayList) {
                //htList = (ArrayList<HabitType>) o;
                loadedID = (Integer) o;
            } else {
                Log.i("HabiTrack HE:", "Error casting");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "IOException");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("HabiTrack HE:Load", "ClassNotFound");
        }
        HabitEventStateManager.getHEStateManager().setID(loadedID);
    }

}
