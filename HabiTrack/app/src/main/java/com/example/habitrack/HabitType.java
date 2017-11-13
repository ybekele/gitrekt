package com.example.habitrack;

import java.util.ArrayList;
import java.util.Calendar;

import io.searchbox.annotations.JestId;

/**
 *
 * HabitType
 *
 * Version 1.01
 *
 * Created by sshussai on 10/21/17.
 *
 */

public class HabitType {
    /**
     * This class is the template for a habit type entity.
     *
     */

    private final Integer ID;
    private String title;                   // title of the habit type
    private String reason;                  // reason for the habit type
    private Calendar startDate;             // Calendar object representing the start date
    private ArrayList<Integer> schedule;    // ArrayList of ints representing days of the week for the plan
    private Integer completedCounter;       // habit events currently completed
    private Integer currentMaxCounter;      // total number of habits events possible so far
    @JestId
    private String id;                      // Elastic Search ID for the habit

    public HabitType(Integer id) {
        this.ID = id;
        this.completedCounter = 0;
        this.currentMaxCounter = 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public ArrayList<Integer> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<Integer> plan) {
        this.schedule = plan;
    }

    public Double getStatus(){
        return (completedCounter.doubleValue()/currentMaxCounter.doubleValue());}

    public Integer getID() {
        return ID;
    }

    public Integer getCompletedCounter() {
        return completedCounter;
    }

    public Integer getCurrentMaxCounter() {
        return currentMaxCounter;
    }

    public String getElasticSearchId() {
        return id;
    }

    public void incrementCompletedCounter(){
        completedCounter++;
    }

    public void incrementMaxCounter(){
        currentMaxCounter++;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString(){return getID().toString() + " | " + getTitle();}
}
