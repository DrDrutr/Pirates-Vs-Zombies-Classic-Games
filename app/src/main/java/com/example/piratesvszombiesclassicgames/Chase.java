package com.example.piratesvszombiesclassicgames;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class Chase extends AppCompatActivity {

    chaseBoard cb;
    ImageView menu;
    insDialog instr;
    Handler handler;
    Chronometer cm;
    FrameLayout fl;
    boolean isLosing=false;
    long timeWhenStopped = 0;
    MenuPlus mp;
    SettingsDialog sd;
    CallIdentify callIdentify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chase);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        instr = new insDialog(this);
        setInsText();
        instr.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                stopingGame();
            }
        });
        instr.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resumeGame();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(message.what==0) {
                    Losing();
                    isLosing = true;
                }
                else{
                    resumeGame();
                }
                return true;
            }
        });

        cb = new chaseBoard(this,handler);
        fl = (FrameLayout) findViewById(R.id.fl);
        fl.addView(cb);

        menu = (ImageView) findViewById(R.id.menu);
        sd = new SettingsDialog(this);
        sd.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                stopingGame();
            }
        });
        sd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                resumeGame();
            }
        });
        mp = new MenuPlus(this,menu,instr,sd);
        mp.setOnDismissListener(new android.widget.PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(android.widget.PopupMenu popupMenu) {
                resumeGame();
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopingGame();
                mp.show();
                pubFun.clickSound(getBaseContext());
            }
        });

        cm = (Chronometer) findViewById(R.id.cm);
        cm.setTypeface(custom_font);
        cm.start();

        callIdentify = new CallIdentify();
        callIdentify.start();
    }

    public void Losing(){
        cm.stop();
        long sec = (SystemClock.elapsedRealtime()-cm.getBase())/1000;
        int x= pubFun.getPlace("chase");
        if(x!=-1){
            if(pubFun.user.records.get(x).sec<sec){
                pubFun.user.records.get(x).sec =sec;
                pubFun.dref.setValue(pubFun.user);
            }
        }
        else{
            pubFun.user.records.add(new Record("chase",sec));
            pubFun.dref.setValue(pubFun.user);
        }
        setInsText();
        long coins = Math.round(sec*0.5);
        pubFun.addCoins((int)coins);
        String time = pubFun.timeFormat(sec);
        AlertDialog.Builder builder = new AlertDialog.Builder(Chase.this);
        builder.setCancelable(true);
        builder.setTitle("You Lose!");
        builder.setMessage("You succeeded to hold on for "+time+" and for that you get "+coins+" coins!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
                cm.setBase(SystemClock.elapsedRealtime());
                cm.start();
                cb = new chaseBoard(getBaseContext(),handler);
                fl.removeAllViews();
                fl.addView(cb);
                isLosing=false;
            }
        });
        builder.show();
    }

    public void setInsText(){
        String in ="You are a pirate. During search for treasure on an island, you are being exposed to a group of zombies and now they are chasing you. Your goal is to keep running without colliding the zombies.\n" +
                "\nClick on the screen in order to jump. You lose if you collide a zombie. You will get more coins as long as you keep playing.\n";
        for (int i = 0; i < pubFun.user.records.size(); i++) {
            if (pubFun.user.records.get(i).game.equals("chase")){
                in+="\nYour best score is "+pubFun.timeFormat(pubFun.user.records.get(i).sec);
            }
        }
        instr.setText(in);
    }

    public void stopingGame(){
        cb.isPaused=true;
        timeWhenStopped = SystemClock.elapsedRealtime() - cm.getBase();
        cm.stop();
    }
    public void resumeGame(){
        if(!isLosing) {
            cb.isPaused = false;
            cm.setBase(SystemClock.elapsedRealtime() - timeWhenStopped);
            cm.start();
        }
    }

    public class CallIdentify extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                if(PhoneCallReceiver.is){
                    stopingGame();
                    while(PhoneCallReceiver.is){

                    }
                    handler.sendEmptyMessage(1);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
