package com.example.habitrack;

/**
 * Created by sshussai on 10/21/17.
 */


public class HabitEvent {
    String word = "hello";

    private String title;
    private boolean status;
    private int eventsDone;
    private int totalEventCount;

    public String getTitle(){
        return this.title;
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
