package com.example.habitrack;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    /* I added these two extra lists to see if it's easier working with strings through ES*/
    private List<String> followRequestsList = new ArrayList<>();
    private List<String> usersFollowedList = new ArrayList<>();





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

    public void addUsersFollowedList(String followee) { usersFollowedList.add(followee); }
    public void addRequestList (String follower) { followRequestsList.add(follower); }


    public ArrayList<NewUser> getUsersFollowed() {
        return usersFollowed;
    }
    public List<String> getUsersFollowedList() { return usersFollowedList; }
    public ArrayList<NewUser> getFollowRequests() {
        return followRequests;
    }
    public List<String> getFollowRequestsList() { return followRequestsList; }
}
