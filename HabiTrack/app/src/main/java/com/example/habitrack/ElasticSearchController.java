package com.example.habitrack;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by austi_sjgf257 on 11/8/2017.
 */

public class ElasticSearchController {
    private static JestDroidClient client;

    public static class AddNewUser extends AsyncTask<NewUser, Void, Void> {

        @Override
        protected Void doInBackground(NewUser... newUsers) {
            verifySettings();
            for (NewUser usr : newUsers) {
                Index index = new Index.Builder(usr).index("gitrekt_htrack").type("htr_user").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        usr.setUserID(result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the user");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the user");
                }
            }
            return null;
        }
    }


    public static class GetUser extends AsyncTask<String, Void, ArrayList<NewUser>> {
        @Override
        protected ArrayList<NewUser> doInBackground(String... search_parameters) {

            verifySettings();

            ArrayList<NewUser> allUsers = new ArrayList<NewUser>();
//            String text = search_parameters[0];

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"userName\": \"" + text + "\"} }\n" + "}";

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"ID\": \"" + "100" + "\"} }\n" + "}";

            String query = "{\n" +
                    "  \"query\": { \"match_all\": {} }\n" + "}";


            Search search = new Search.Builder(query)
                    .addIndex("gitrekt_htrack")
                    .addType("htr_user")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<NewUser> foundUsers = result.getSourceAsObjectList(NewUser.class);
                    allUsers.addAll(foundUsers);
                } else {
                    Log.i("Error", "The search query failed to find any users that matched");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return allUsers;
        }
    }




    public static class AddHabitType extends AsyncTask<HabitType, Void, Void> {

        @Override
        protected Void doInBackground(HabitType... habitTypes) {
            verifySettings();
            for (HabitType habitType : habitTypes) {
                Index index = new Index.Builder(habitType).index("gitrekt_htrack").type("habit_type").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        habitType.setId(result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the HabitType");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the HabitType");
                }
            }
            return null;
        }
    }


    public static class AddHabitEvent extends AsyncTask<HabitEvent, Void, Void> {
        @Override
        protected Void doInBackground(HabitEvent... habitEvents) {
            verifySettings();
            for (HabitEvent habitEvent : habitEvents) {
                Index index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        habitEvent.setId(result.getId());
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the HabitEvent");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the HabitEvent");
                }
            }
            return null;
        }
    }

    public static class GetHabitType extends AsyncTask<String, Void, ArrayList<HabitType>> {
        @Override
        protected ArrayList<HabitType> doInBackground(String... search_parameters) {

            verifySettings();

            ArrayList<HabitType> habitTypes = new ArrayList<HabitType>();
            String text = search_parameters[0];

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"title\": \"" + text + "\"} }\n" + "}";

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"ID\": \"" + "100" + "\"} }\n" + "}";
            String query = "{\n" +
                    "  \"query\": { \"match_all\": {} }\n" + "}";


            Search search = new Search.Builder(query)
                    .addIndex("gitrekt_htrack")
                    .addType("habit_type")
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<HabitType> foundHabitType = result.getSourceAsObjectList(HabitType.class);
                    habitTypes.addAll(foundHabitType);
                } else {
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habitTypes;
        }
    }


    public static class GetHabitEvent extends AsyncTask<String, Void, ArrayList<HabitEvent>> {
        @Override
        protected ArrayList<HabitEvent> doInBackground(String... search_parameters) {
            verifySettings();

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();
            String text = search_parameters[0];

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"title\": \"" + text + "\"} }\n" + "}";

//            String query = "{\n" +
//                    "  \"query\": { \"term\": {\"ID\": \"" + "100" + "\"} }\n" + "}";
            String query = "{\n" +
                    "  \"query\": { \"match_all\": {} }\n" + "}";


            Search search = new Search.Builder(query)
                    .addIndex("gitrekt_htrack")
                    .addType("habit_event")
                    .build();

            try {

                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {
                    List<HabitEvent> foundHabitEvent = result.getSourceAsObjectList(HabitEvent.class);
                    habitEvents.addAll(foundHabitEvent);
                } else {
                    Log.i("Error", "The search query failed to find any tweets that matched");
                }
            } catch (Exception e) {
                Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
            }

            return habitEvents;
        }
    }

    // TODO Build the query

    public static void verifySettings() {
        if (client == null) {
            //DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080/gitrekt_habitrack");
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();
            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}