package com.example.piratesvszombiesclassicgames;

import android.content.Intent;
import android.graphics.Point;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    ImageView pirate;
    Animation a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        pirate = (ImageView) findViewById(R.id.pirate);
        a = AnimationUtils.loadAnimation(this,R.anim.show_layout);
        a.setDuration(1000);
        pirate.startAnimation(a);
        Thread t = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getBaseContext(),HomeScreen.class);
                startActivity(i);
                finish();
            }
        };
        t.start();
    }
}
