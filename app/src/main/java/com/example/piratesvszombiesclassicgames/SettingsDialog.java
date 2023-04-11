package com.example.piratesvszombiesclassicgames;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsDialog extends Dialog {

    public TextView welcome;
    private ImageView closer;
    private ImageView music;
    private ImageView sound;
    private Button logout;
    private TextView tv1;
    private TextView tv2;
    private SharedPreferences sp;
    private Context context;

    public SettingsDialog(@NonNull final Context context) {
        super(context);
        this.context = context;
        constructor();
        welcome.setText("Welcome "+pubFun.user.user);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(context);
                FirebaseAuth.getInstance().signOut();
                SettingsDialog.super.dismiss();
                Intent in = new Intent();
                in.putExtra("logout",true);
                ((Activity)context).setResult(Activity.RESULT_OK,in);
                ((Activity)context).finish();
            }
        });
    }

    public SettingsDialog(@NonNull final Context context,String x) {
        super(context);
        this.context = context;
        constructor();
        welcome.setText("Welcome "+x);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(context);
                FirebaseAuth.getInstance().signOut();
                SettingsDialog.super.dismiss();
                Intent in = new Intent();
                in.putExtra("logout",true);
                ((Activity)context).setResult(Activity.RESULT_OK,in);
                ((Activity)context).finish();
                ((Activity)context).startActivity(((Activity)context).getIntent());
            }
        });
    }

    private void constructor(){
        super.setContentView(R.layout.settings);
        super.setCancelable(false);
        welcome = (TextView) super.findViewById(R.id.welcome);
        closer = (ImageView) super.findViewById(R.id.closer);
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(context);
                SettingsDialog.super.cancel();
            }
        });
        super.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ARCENA.ttf");
        welcome.setTypeface(custom_font);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        tv1 = (TextView) super.findViewById(R.id.tv1);
        tv2 = (TextView) super.findViewById(R.id.tv2);
        tv1.setTypeface(custom_font);
        tv2.setTypeface(custom_font);
        music = (ImageView) super.findViewById(R.id.music);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BackgroundMusic.isRun){
                    BackgroundMusic.stopMusic();
                    music.setImageResource(R.drawable.notmusic);
                }
                else{
                    BackgroundMusic.startMusic();
                    music.setImageResource(R.drawable.music);
                }
            }
        });
        sp = ((Activity)context).getSharedPreferences("details",0);
        if(!sp.getBoolean("music",true)){
            music.setImageResource(R.drawable.notmusic);
        }
        sound = (ImageView) super.findViewById(R.id.sound);
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getBoolean("sound",true)){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("sound",false);
                    editor.commit();
                    pubFun.sound=false;
                    sound.setImageResource(R.drawable.mute);
                }
                else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("sound",true);
                    editor.commit();
                    pubFun.sound=true;
                    sound.setImageResource(R.drawable.speaker);
                }
            }
        });
        if(!sp.getBoolean("sound",true)){
            sound.setImageResource(R.drawable.mute);
        }
        logout = (Button) super.findViewById(R.id.logout);
        logout.setTypeface(custom_font);
    }
}
