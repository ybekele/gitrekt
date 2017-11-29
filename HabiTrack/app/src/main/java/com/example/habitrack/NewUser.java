package com.example.habitrack;

/**
 * Created by Abdul on 2017-11-29.
 */

public class NewUser {

    private String title;
    private Integer ID;


    public NewUser(String title, Integer ID){
        this.ID = ID;
        this.title = title;

    }

    public String getTitle(){
        return title;
    }

    public Integer getID(){
        return ID;
    }
}
