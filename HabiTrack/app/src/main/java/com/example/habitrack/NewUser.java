package com.example.habitrack;

import io.searchbox.annotations.JestId;

/**
 * Created by Abdul on 2017-11-29.
 */

public class NewUser {

    private String userName;
    @JestId
    private String userID;



    public NewUser(String userName){
        this.userName = userName;

    }

    public String getTitle(){
        return userName;
    }

    public String getId(){
        return userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
