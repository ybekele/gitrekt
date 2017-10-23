package com.example.habitrack;


import java.util.ArrayList;

/**
 * Created by sshussai on 10/21/17.
 */

public class HabitEventController {
    String third_word = "hello";

    private static final String FILENAME = "file.sav";
    private ArrayList<HabitEvent> eventList= new ArrayList<HabitEvent>();
    private ArrayList<HabitEvent> todayList = new ArrayList<HabitEvent>();

    public void createHabitEvent(String title){
        HabitEvent event = new HabitEvent(title);
        eventList.add(event);
    }

    public int getTotalEvents(){
        return eventList.size();
    }

    public void deleteEvent(){
        eventList.remove(0);
    }

    public ArrayList<HabitEvent> getAllEvents(){
        return eventList;
    }

    public ArrayList<HabitEvent> getEventsForToday(){
        return todayList;
    }

    public String get_third_Word() {
        return third_word;
    }
}
