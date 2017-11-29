package com.example.habitrack;

/**
 * Created by Abdul on 2017-11-29.
 */

public class NewUser {

    private String userName;
    private String userID = null;



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
