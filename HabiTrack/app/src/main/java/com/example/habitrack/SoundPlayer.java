package com.example.habitrack;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by Abdul on 2017-11-24.
 */

public class SoundPlayer {


    private static SoundPool soundPool;
    private static int habitSound;
    Boolean soundON = false;

    public SoundPlayer(Context context){


       //SoundPool (int maxStreams,int streamType, int srcQuality)

        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC,0);

        habitSound = soundPool.load(context, R.raw.applause7,1);
    }

    public void playHabitSound(){

        //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)

        soundPool.play(habitSound,1.0f,1.0f,1,0,1.0f);

    }
}
