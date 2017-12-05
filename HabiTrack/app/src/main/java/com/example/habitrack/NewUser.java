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
    public ArrayList<NewUser> followRequests = new ArrayList<>();
    public ArrayList<NewUser> usersFollowed = new ArrayList<>();

    /**
     * ignore this
     */
    public ArrayList<String> requestsEID = new ArrayList<>();

    /* I added these two extra lists to see if it's easier working with strings through ES*/
    //private List<String> followRequestsList = new ArrayList<>();
    //private List<String> usersFollowedList = new ArrayList<>();




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

    public void setUserID(String userID) { this.userID = userID; }

    public String getUserID(String userName) {
        return userID;
    }

    public void addUsersFollowed(NewUser follower) {
        usersFollowed.add(follower);
    }

    public void addRequest(NewUser requester) {
        followRequests.add(requester);
    }

    public void addUsersFollowedList(NewUser followee) {
        usersFollowed.add(followee); }

    public void addRequestList (NewUser follower) {
        followRequests.add(follower);
    }


    public ArrayList<NewUser> getUsersFollowed() {
        return usersFollowed;
    }

    public ArrayList<NewUser> getFollowRequests() {
        return followRequests;
    }

    public ArrayList<String> getRequestsEID() {
        return requestsEID;
    }




}

