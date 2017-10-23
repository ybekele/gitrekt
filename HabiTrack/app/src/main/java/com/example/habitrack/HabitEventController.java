package com.example.habitrack;
import com.example.HabitEvent;

/**
 * Created by sshussai on 10/21/17.
 */

public class HabitEventController {
    String third_word = "hello";

    private static final String FILENAME = "file.sav";
    private ArrayList<HabitEvent> eventList= new ArrayList<HabitEvent>();
    private ArrayList<HabitEvent> todayList = new ArrayList<HabitEvent>();

    public createHabitEvent(String title){
        HabitEvent event = new HabitEvent(title);
        eventList.add(event);
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
