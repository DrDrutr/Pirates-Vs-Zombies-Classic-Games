package com.example.piratesvszombiesclassicgames;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

public class pubFun {

    public static User user;
    public static DatabaseReference dref;
    public static boolean sound;

    public static int dpToPx(int dp,Context c) {
        float density = c.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    public static String timeFormat(long sec){
        long min = sec/60;
        sec %=60;
        String time ="";
        if(min<10)
            time+="0"+min;
        else time += min;
        time+=":";
        if(sec<10)
            time+="0"+sec;
        else time+=sec;
        return time;
    }

    public static int getPlace(String str){
        for (int i = 0; i < user.records.size(); i++) {
            if(user.records.get(i).game.equals(str))
                return i;
        }
        return -1;
    }
    public static void setRecord(String str, long sec){
        int x= getPlace(str);
        if(x!=-1){
            if(user.records.get(x).sec>sec){
                user.records.get(x).sec =sec;
                dref.setValue(user);
            }
        }
        else{
            user.records.add(new Record(str,sec));
            dref.setValue(user);
        }
    }

    public static void addCoins(int coins){
        user.coins+=coins;
        dref.setValue(user);
    }

    public static void figure(boolean b, int x, ImageView iv){
        if(b){
            switch (x){
                case 0:
                    iv.setImageResource(R.drawable.white);
                    break;
                case 1:
                    iv.setImageResource(R.drawable.white1);
                    break;
                case 2:
                    iv.setImageResource(R.drawable.white2);
                    break;
                case 3:
                    iv.setImageResource(R.drawable.white3);
                    break;
                case 4:
                    iv.setImageResource(R.drawable.white4);
                    break;
            }
        }
        else{
            switch (x){
                case 0:
                    iv.setImageResource(R.drawable.black);
                    break;
                case 1:
                    iv.setImageResource(R.drawable.black1);
                    break;
                case 2:
                    iv.setImageResource(R.drawable.black2);
                    break;
                case 3:
                    iv.setImageResource(R.drawable.black3);
                    break;
                case 4:
                    iv.setImageResource(R.drawable.black4);
                    break;
            }
        }
    }
    public static void figure(boolean b, ImageView iv){
        if(b){
            figure(b,user.currentPirate,iv);
        }
        else figure(b,user.currentZombie,iv);
    }

    public static void clickSound(Context context){
        if(sound) {
            MediaPlayer mp = MediaPlayer.create(context, R.raw.blop);
            mp.start();
        }
    }
}
