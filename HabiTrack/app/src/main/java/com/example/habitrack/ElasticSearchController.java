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

    public static class VerifyESId extends AsyncTask<ArrayList<HabitTypeMetadata>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(ArrayList<HabitTypeMetadata>... metadataLists) {
            verifySettings();
            ArrayList<HabitTypeMetadata> htMetadataList = metadataLists[0];
            String uid = "test";
            String query;
            ArrayList<HabitType> habitTypes = new ArrayList<HabitType>();
            query = "{\n" +
                    "  \"query\": { \"term\": {\"userID\": \"" + uid + "\"} }\n" + "}";

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

            for(HabitTypeMetadata htMetadata : htMetadataList) {
                if (htMetadata.getEsID() == "" || htMetadata.getEsID() == null) {
                    for (HabitType ht : habitTypes) {
                        if(ht.getID() == htMetadata.getLocalID()){
                            htMetadata.setEsID(ht.getElasticSearchId());
                        }
                    }
                }
            }
            return Boolean.FALSE;
        }
    }

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

        private FileManager fileManager;

        public AddHabitType(FileManager givenFM){
            fileManager = givenFM;
        }

        @Override
        protected Void doInBackground(HabitType... habitTypes) {
            verifySettings();
            for (HabitType habitType : habitTypes) {
                Index index = new Index.Builder(habitType).index("gitrekt_htrack").type("habit_type").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        habitType.setId(result.getId());
                        fileManager.save(fileManager.HT_METADATA_MODE);
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the HabitType");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the HabitType");
                }
                index = new Index.Builder(habitType).index("gitrekt_htrack").type("habit_type").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        Boolean status = Boolean.TRUE;
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

    public static class AddHabitEvent extends AsyncTask<HabitEvent, Void, Boolean> {
        @Override
        protected Boolean doInBackground(HabitEvent... habitEvents) {
            Boolean status = Boolean.FALSE;
            verifySettings();
            for (HabitEvent habitEvent : habitEvents) {
                Index index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        habitEvent.setId(result.getId());
                        status = Boolean.TRUE;
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the HabitEvent");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the HabitEvent");
                }
            }
            return status;
        }
    }

    public static class GetHabitType extends AsyncTask<String, Void, HabitType> {
        @Override
        protected HabitType doInBackground(String... esIDinList) {
            verifySettings();

            String esID = esIDinList[0];

            ArrayList<HabitType> habitTypes = new ArrayList<HabitType>();
            String query;

            query = "{\n" +
                    "  \"query\": { \"term\": {\"_id\": \"" + esID + "\"} }\n" + "}";


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

            return habitTypes.get(0);
        }
    }

    public static class EditHabitType extends AsyncTask<HabitType, Void, Void> {

        private FileManager fileManager;

        public EditHabitType(FileManager givenFM){
            fileManager = givenFM;
        }

        @Override
        protected Void doInBackground(HabitType... htList) {
            verifySettings();

            HabitType habitType = htList[0];
            String esID = habitType.getElasticSearchId();
            Boolean status = Boolean.FALSE;
            // Push the new habit type back
            Index index = new Index.Builder(habitType).index("gitrekt_htrack").type("habit_type").id(esID).build();
            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    status = Boolean.TRUE;
                } else {
                    Log.i("Error", "Elasticsearch was not able to add the HabitType");
                }
            } catch (IOException e) {
                Log.i("Error", "The application failed to build and send the HabitType");
            }
            return null;
        }
    }


    public static class EditUser extends AsyncTask<NewUser, Void, Void> {

        @Override
        protected Void doInBackground(NewUser... users) {
            verifySettings();

            NewUser usr = users[0];
            String esID = usr.getId();
            Boolean status = Boolean.FALSE;
            // Push the new habit type back
            Index index = new Index.Builder(usr).index("gitrekt_htrack").type("htr_usr").id(esID).build();
            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    status = Boolean.TRUE;
                } else {
                    Log.i("Error", "Elasticsearch was not able to add the HabitType");
                }
            } catch (IOException e) {
                Log.i("Error", "The application failed to build and send the HabitType");
            }
            return null;
        }
    }

    /**
     * In a new thread, this task will get a habit event from elastic search. The
     * query used will depend on the first search parameter given to the function.
     * If the first search parameter is :
     *   "" ---> query will result in all habit events
     *   "user" ---> query will result in all habit events for the current user
     *   "user_unique" ---> query will result in a specific habit event by the user
     */
    public static class GetHabitEvent extends AsyncTask<String, Void, ArrayList<HabitEvent>> {
        @Override
        protected ArrayList<HabitEvent> doInBackground(String... search_parameters) {
            verifySettings();
            String query;

            ArrayList<HabitEvent> habitEvents = new ArrayList<HabitEvent>();
            String key = search_parameters[0];
            if (key == "user"){
                String uid = search_parameters[1];
                query = "{\n" +
                    "  \"query\": { \"term\": {\"userID\": \"" + uid + "\"} }\n" + "}";
            } else {
                query = "{\n" +
                        "  \"query\": { \"match_all\": {} }\n" + "}";
            }

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

    public static class AddOfflineEvents extends AsyncTask<ArrayList<HabitEvent>, Void, Void> {

        private FileManager fileManager;

        public AddOfflineEvents(FileManager givenFM) {
            this.fileManager = givenFM;
        }

        @Override
        protected Void doInBackground(ArrayList<HabitEvent>... heList) {
            verifySettings();
            ArrayList<HabitEvent> offlineHEs = heList[0];
            for (HabitEvent habitEvent : offlineHEs) {
                if(habitEvent.getId() == null) {
                    Index index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").build();
                    try {
                        DocumentResult result = client.execute(index);
                        if (result.isSucceeded()) {
                            habitEvent.setId(result.getId());
                            fileManager.save(fileManager.RECENT_HE_MODE);
                        } else {
                            Log.i("Error", "Elasticsearch was not able to add the HabitEvent");
                        }
                    } catch (IOException e) {
                        Log.i("Error", "The application failed to build and send the HabitEvent");
                    }
                }
            }
            return null;
        }
    }

    public static class EditHabitEvent extends AsyncTask<HabitEvent, Void, Boolean> {

        private FileManager fileManager;

        public EditHabitEvent(FileManager givenFM){
            fileManager = givenFM;
        }

        @Override
        protected Boolean doInBackground(HabitEvent... heList) {
            verifySettings();

            HabitEvent habitEvent = heList[0];
            String esID = habitEvent.getId();
            Boolean status = Boolean.FALSE;
            // Push the new habit type back
            Index index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").id(esID).build();
            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    status = Boolean.TRUE;
                } else {
                    Log.i("Error", "Elasticsearch was not able to add the HabitType");
                }
            } catch (IOException e) {
                Log.i("Error", "The application failed to build and send the HabitType");
            }
            return status;
        }
    }

    public static class syncEditedOfflineHEs extends AsyncTask<Void, Void, Void> {

        private FileManager fileManager;

        public syncEditedOfflineHEs (FileManager givenFM){
            fileManager = givenFM;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            verifySettings();

            ArrayList<HabitEvent> editedOffLineHeList = HabitEventStateManager.getHEStateManager().getEditedOfflineHE();
            while(editedOffLineHeList.size() > 0){
//            for(HabitEvent habitEvent : editedOffLineHeList) {
                Index index;
                HabitEvent habitEvent = editedOffLineHeList.get(0);
                String esID = habitEvent.getId();
                Boolean status = Boolean.FALSE;
                // Push the new habit type back
                index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").id(esID).build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        status = Boolean.TRUE;
                        editedOffLineHeList.remove(habitEvent);
                    } else {
                        Log.i("Error", "Elasticsearch was not able to add the HabitType");
                    }
                } catch (IOException e) {
                    Log.i("Error", "The application failed to build and send the HabitType");
                }
                HabitEventStateManager.getHEStateManager().setEditedOfflineHE(editedOffLineHeList);
                fileManager.save(fileManager.EDITED_OFFLINE_HE_MODE);
            }
//            ArrayList<HabitEvent> newOffLineHeList = HabitEventStateManager.getHEStateManager().getNewOfflineHE();
//            while(newOffLineHeList.size() > 0){
////            for(HabitEvent habitEvent : newOffLineHeList) {
//                HabitEvent habitEvent = newOffLineHeList.get(0);
//                Index index;
//                String esID = habitEvent.getId();
//                Boolean status = Boolean.FALSE;
//                // Push the new habit type back
//                index = new Index.Builder(habitEvent).index("gitrekt_htrack").type("habit_event").build();
//
//                try {
//                    DocumentResult result = client.execute(index);
//                    if (result.isSucceeded()) {
//                        habitEvent.setId(result.getId());
//                        newOffLineHeList.remove(habitEvent);
//
//                    } else {
//                        Log.i("Error", "Elasticsearch was not able to add the HabitType");
//                    }
//                } catch (IOException e) {
//                    Log.i("Error", "The application failed to build and send the HabitType");
//                }
//            }
            return null;
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