package com.example.piratesvszombiesclassicgames;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BackgroundMusic extends Service {

    private static MediaPlayer mediaPlayer;
    public static boolean isRun;
    private static SharedPreferences sp;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sp = getSharedPreferences("details",0);

        if(mediaPlayer==null){
            isRun=true;
            mediaPlayer = MediaPlayer.create(this,R.raw.backgroundmusic);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }

        if(!sp.getBoolean("music",true)){
            stopMusic();
            isRun = false;
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void stopMusic(){
        isRun=false;
        mediaPlayer.pause();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music",false);
        editor.commit();
    }

    public static void startMusic(){
        isRun=true;
        mediaPlayer.start();
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("music",true);
        editor.commit();
    }
}
