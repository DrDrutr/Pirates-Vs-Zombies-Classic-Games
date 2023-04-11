package com.example.piratesvszombiesclassicgames;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;


public class GameList extends AppCompatActivity {

    Button checkers;
    Button minesweeper;
    Button connect4;
    Button Mancala;
    Button tictactoe;
    Button mastermind;
    Dialog d;
    Button wheelluck;
    Random rnd;
    Typeface custom_font;
    TextView wheeltv;
    TimerChange t;
    Handler handler;
    FirebaseAuth firebaseAuth;
    Button chase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            Intent intent = new Intent(this,HomeScreen.class);
            startActivity(intent);
        }
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        getSupportActionBar().hide();
        rnd = new Random();
        checkers = (Button) findViewById(R.id.checkers);
        checkers.setTypeface(custom_font);
        checkers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeAlert("checkers");
                pubFun.clickSound(getBaseContext());
            }
        });
        minesweeper = (Button) findViewById(R.id.minesweeper);
        minesweeper.setTypeface(custom_font);
        minesweeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),preMinesweeper.class);
                in.putExtra("game",0);
                startActivityForResult(in,0);
                pubFun.clickSound(getBaseContext());
            }
        });
        connect4 = (Button) findViewById(R.id.connect4);
        connect4.setTypeface(custom_font);
        connect4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeAlert("connect4");
                pubFun.clickSound(getBaseContext());
            }
        });
        Mancala = (Button) findViewById(R.id.Mancala);
        Mancala.setTypeface(custom_font);
        Mancala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeAlert("mancala");
                pubFun.clickSound(getBaseContext());
            }
        });
        tictactoe = (Button) findViewById(R.id.tictactoe);
        tictactoe.setTypeface(custom_font);
        tictactoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeAlert("tictactoe");
                pubFun.clickSound(getBaseContext());
            }
        });
        mastermind = (Button) findViewById(R.id.mastermind);
        mastermind.setTypeface(custom_font);
        mastermind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),preMinesweeper.class);
                in.putExtra("game",1);
                startActivityForResult(in,0);
                pubFun.clickSound(getBaseContext());
            }
        });
        wheelluck = (Button) findViewById(R.id.wheelluck);
        wheelluck.setTypeface(custom_font);
        wheelluck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                luckwheel();
                pubFun.clickSound(getBaseContext());
            }
        });
        Intent intent = getIntent();
        if(intent!=null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                if (bundle.getBoolean("isStop")) {
                    luckwheel();
                }
            }
        }
        chase = (Button) findViewById(R.id.chase);
        chase.setTypeface(custom_font);
        chase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),Chase.class);
                startActivityForResult(in,0);
                pubFun.clickSound(getBaseContext());
            }
        });
    }

    public void luckwheel(){
        d = new Dialog(this);
        d.setContentView(R.layout.luckwheel);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        final ImageView closer = (ImageView) d.findViewById(R.id.closer);
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                t=null;
                pubFun.clickSound(getBaseContext());
            }
        });
        final ImageView wheel = (ImageView) d.findViewById(R.id.wheel);
        wheeltv = (TextView) d.findViewById(R.id.wheeltv);
        wheeltv.setTypeface(custom_font);
        handler=new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1==0) {
                    t=null;
                    d.dismiss();
                }
                else if(msg.arg1>0)
                    wheeltv.setText("The Wheel\nOf Furtune\n"+pubFun.timeFormat(msg.arg1));
                return true;
            }
        });
        if(!Reminder.isRun){
            Intent i = new Intent(this,Reminder.class);
            stopService(i);
            wheeltv.setText("The Wheel\nOf Furtune\nPress Me");
            wheeltv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(pubFun.sound) {
                        MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.wheel);
                        mp.start();
                    }
                    final int x = rnd.nextInt(361);
                    RotateAnimation rn = new RotateAnimation(0,x+1800,wheel.getWidth()/2,wheel.getHeight()/2);
                    rn.setFillAfter(true);
                    rn.setDuration(5000);
                    wheel.startAnimation(rn);
                    rn.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            closer.setClickable(false);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if((x>=0&&x<22.5)||(x<=360&&x>337.5))
                                winMoney(100);
                            else if(x>157.5&&x<202.5)
                                winMoney(75);
                            else if((x>67.5&&x<112.5)||(x<292.5&&x>247.5))
                                winMoney(50);
                            else winMoney(25);
                            wheeltv.setClickable(false);
                            closer.setClickable(true);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });
        }
        else{
            t = new TimerChange();
            t.start();
        }
        d.show();
    }

    public void winMoney(int x){
        if(pubFun.sound) {
            MediaPlayer mp = MediaPlayer.create(getBaseContext(), R.raw.moneywin);
            mp.start();
        }
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(GameList.this);
        builder.setCancelable(true);
        builder.setTitle("Congratulations!");
        builder.setMessage("You have won "+x+" coins!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.show();
        pubFun.addCoins(x);
        Intent i = new Intent(this,Reminder.class);
        startService(i);
        t = new TimerChange();
        t.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent in = new Intent();
            if(data.getExtras().getBoolean("logout")){
                setResult(RESULT_OK,in);
            }
            else setResult(RESULT_CANCELED,in);
            finish();
        }
    }

    public class TimerChange extends Thread{

        public TimerChange(){
        }
        @Override
        public void run() {
            super.run();
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1 = Reminder.sec;
                handler.sendMessage(msg);
            }
        }
    }

    public void modeAlert(final String game){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(GameList.this);
        builder.setCancelable(true);
        builder.setTitle("What mode do you want to play?");
        builder.setPositiveButton("Singleplayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IDoesNotKnow("single",game);
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.setNegativeButton("Multiplayer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IDoesNotKnow("multi",game);
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.show();
    }

    public void IDoesNotKnow(String mode,String game){
        Intent i = new Intent();
        switch (game){
            case "checkers":
                i= new Intent(getApplicationContext(), Checkers.class);
                break;
            case "connect4":
                i = new Intent(getApplicationContext(), Connect4.class);
                break;
            case "mancala":
                i = new Intent(getApplicationContext(), Mancala.class);
                break;
            case "tictactoe":
                i = new Intent(getApplicationContext(), TicTacToe.class);
                break;
        }
        i.putExtra("mode",mode);
        startActivityForResult(i,0);
    }
}
