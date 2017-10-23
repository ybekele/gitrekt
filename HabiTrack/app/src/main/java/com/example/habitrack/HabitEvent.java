package com.example.habitrack;

import java.util.Date;

/**
 * Created by sshussai on 10/21/17.
 */


public class HabitEvent {
    String word = "hello";

    private String title;
    private String comment;
    private Date date;
    private boolean status;
    private int eventsDone;
    private int totalEventCount;

    public HabitEvent(String title) {
        this.title = title;
        this.date = new Date();
    }

    public String getTitle(){
        return this.title;
    }

    public String getComment(){
        return this.comment;
    }

    public float getProgress(){
        return 100*(eventsDone/totalEventCount);
    }

    public boolean getStatus(){
        return false;
    }

    public String get_my_Word() {
        return word;
    }
}
