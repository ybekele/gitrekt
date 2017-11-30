package com.example.habitrack;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sshussai on 11/29/17.
 */

/**
 * Steps to add a new object:
 * 1. Declare a base object
 * 2. Declare a public final MODE value
 * 2. Declare a private final FILENAME value
 * 3. Add the if case to the save function. Modify as needed
 * 4. Add the if case to the load function. Modify as needed
 */

public class FileManager {

    private Context ctx;

    // 1. Base objects
    private ArrayList<HabitTypeMetadata> habitTypeMetadata;

    // 2. MODES
    public final Integer HT_METADATA_MODE = 100;

    // 3. FILENAMES
    private String filename;
    private final String HT_METADATA_FILE = "htmetadata.sav";

    // Constructor
    public FileManager(Context context) {
        this.ctx = context;
    }

    public void save(Integer mode){
        // 4. If cases for the save function
        if(mode == HT_METADATA_MODE){
            habitTypeMetadata = HabitTypeStateManager.getHTStateManager().getHtMetadata();
            filename = HT_METADATA_FILE;
        }
        try {
            FileOutputStream fos = ctx.openFileOutput(filename,0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            if(mode == HT_METADATA_MODE) {
                gson.toJson(habitTypeMetadata, writer);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    public void load(Integer mode) {
        // 5. If cases for load function
        if(mode == HT_METADATA_MODE){
            filename = HT_METADATA_FILE;
        }
        try {
            FileInputStream fis = ctx.openFileInput(filename);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
            if(mode == HT_METADATA_MODE) {
                Type calType = new TypeToken<ArrayList<HabitTypeMetadata>>() {}.getType();
                habitTypeMetadata = gson.fromJson(in, calType);
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            if(mode == HT_METADATA_MODE){
                habitTypeMetadata = new ArrayList<HabitTypeMetadata>();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
        if(mode == HT_METADATA_MODE){
            HabitTypeStateManager.setHtMetadata(habitTypeMetadata);
        }
    }

}

