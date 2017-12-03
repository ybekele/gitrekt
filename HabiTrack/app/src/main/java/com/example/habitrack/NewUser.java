package com.example.habitrack;

import java.util.ArrayList;

import io.searchbox.annotations.JestId;

/**
 * Created by Abdul on 2017-11-29.
 */

public class NewUser {

    private String userName;
    @JestId
    private String userID;
    private ArrayList<NewUser> followRequests = new ArrayList<>();
    private ArrayList<NewUser> usersFollowed = new ArrayList<>();




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

    public void addUsersFollowed(NewUser follower) {
        usersFollowed.add(follower);
    }
    public void addRequest(NewUser requester) {
        followRequests.add(requester);
    }

    public ArrayList<NewUser> getUsersFollowed() {
        return usersFollowed;
    }

    public ArrayList<NewUser> getFollowRequests() {
        return followRequests;
    }
}
