package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

public class preMinesweeper extends AppCompatActivity {

    SeekBar width;
    SeekBar height;
    SeekBar bombs;
    SeekBar holes;
    SeekBar colors;
    TextView widthnum;
    TextView heightnum;
    TextView bombnum;
    TextView holesnum;
    TextView colorsnum;
    int w=10;
    int h=10;
    int b=10;
    int c = 3;
    int ho = 3;
    Button easy;
    Button medium;
    Button expert;
    Button custom;
    Button but;
    TableLayout change;
    TableLayout changeMM;
    ImageView menu;
    insDialog instr;
    User user;
    int game;
    MenuPlus mp;
    SettingsDialog sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_minesweeper);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");
        getSupportActionBar().hide();

        user = pubFun.user;
        game = getIntent().getExtras().getInt("game");

        menu = (ImageView) findViewById(R.id.menu);
        sd = new SettingsDialog(this);
        instr = new insDialog(this);
        mp = new MenuPlus(this,menu,instr,sd);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.show();
                pubFun.clickSound(getBaseContext());
            }
        });

        if(game ==1){
            ImageView iv = (ImageView) findViewById(R.id.imageView7);
            iv.setImageResource(R.drawable.mastermindtitle);
            setInsTextMM();
        }
        else {
            setInsTextMS();
        }

        easy = (Button) findViewById(R.id.easy);
        medium = (Button)findViewById(R.id.medium);
        expert = (Button)findViewById(R.id.expert);
        custom = (Button)findViewById(R.id.Custom);
        width = (SeekBar) findViewById(R.id.width);
        height = (SeekBar) findViewById(R.id.height);
        bombs = (SeekBar) findViewById(R.id.bombs);
        widthnum = (TextView) findViewById(R.id.widthnum);
        heightnum = (TextView) findViewById(R.id.heightnum);
        bombnum = (TextView) findViewById(R.id.bombnum);
        but = (Button) findViewById(R.id.button);
        change = (TableLayout) findViewById(R.id.change);
        changeMM = (TableLayout) findViewById(R.id.changeMM);
        colorsnum = (TextView) findViewById(R.id.colorsnum);
        holesnum = (TextView) findViewById(R.id.holesnum);
        colors = (SeekBar) findViewById(R.id.colors);
        holes = (SeekBar) findViewById(R.id.holes);

        TextView x = (TextView) findViewById(R.id.tv1);
        x.setTypeface(custom_font);
        x = (TextView) findViewById(R.id.tv2);
        x.setTypeface(custom_font);
        x = (TextView) findViewById(R.id.tv3);
        x.setTypeface(custom_font);
        x = (TextView) findViewById(R.id.tv1MM);
        x.setTypeface(custom_font);
        x = (TextView) findViewById(R.id.tv2MM);
        x.setTypeface(custom_font);
        but.setTypeface(custom_font);
        heightnum.setTypeface(custom_font);
        widthnum.setTypeface(custom_font);
        bombnum.setTypeface(custom_font);
        holesnum.setTypeface(custom_font);
        colorsnum.setTypeface(custom_font);
        easy.setTypeface(custom_font);
        medium.setTypeface(custom_font);
        expert.setTypeface(custom_font);
        custom.setTypeface(custom_font);

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(game==0) {
                    w = 10;
                    h = 10;
                    b = 15;
                    modeAlert("easy");
                }
                else{
                    ho=4;
                    c=6;
                    IDoesNotKnow("easy");
                }
            }
        });
        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(game==0) {
                    w = 16;
                    h = 16;
                    b = 40;
                    modeAlert("medium");
                }
                else{
                    ho=6;
                    c=8;
                    IDoesNotKnow("medium");
                }
            }
        });
        expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(game==0) {
                    w = 20;
                    h = 30;
                    b = 100;
                    modeAlert("expert");
                }
                else{
                    ho=8;
                    c=10;
                    IDoesNotKnow("expert");
                }
            }
        });
        custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(game ==0) {
                    if (change.getVisibility() == View.GONE) {
                        change.setVisibility(View.VISIBLE);
                        but.setVisibility(View.VISIBLE);
                    } else {
                        change.setVisibility(View.GONE);
                        but.setVisibility(View.GONE);
                    }
                }
                else{
                    if (changeMM.getVisibility() == View.GONE) {
                        changeMM.setVisibility(View.VISIBLE);
                        but.setVisibility(View.VISIBLE);
                    } else {
                        changeMM.setVisibility(View.GONE);
                        but.setVisibility(View.GONE);
                    }
                }
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(game==0)
                    modeAlert("custom");
                else IDoesNotKnow("custom");
            }
        });

        width.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                w=i+10;
                widthnum.setText(""+w);
                bombs.setMax((w-1)*(h-1)-10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                widthnum.setText(""+w);
                bombs.setMax((w-1)*(h-1)-10);
            }
        });
        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                h=i+10;
                heightnum.setText(""+h);
                bombs.setMax((w-1)*(h-1)-10);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                heightnum.setText(""+h);
                bombs.setMax((w-1)*(h-1)-10);
            }
        });
        bombs.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean bool) {
                b=i+10;
                bombnum.setText(""+b);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bombnum.setText(""+b);
            }
        });
        holes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ho=i+3;
                setTextNum(holesnum,ho);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setTextNum(holesnum,ho);
            }
        });
        colors.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                c=i+3;
                setTextNum(colorsnum,c);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setTextNum(colorsnum,c);
            }
        });
    }

    public void modeAlert(final String level){
        AlertDialog.Builder builder = new AlertDialog.Builder(preMinesweeper.this);
        builder.setCancelable(true);
        builder.setTitle("What mode do you want to play?");
        builder.setPositiveButton("Classic Minesweeper", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IDoesNotKnow("classic",level);
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.setNegativeButton("Find the Treasure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                IDoesNotKnow("treasure",level);
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.show();
    }

    public void IDoesNotKnow(String mode,String level){
        Intent i = new Intent(getApplicationContext(), Minesweeper.class);
        i.putExtra("width",w);
        i.putExtra("height",h);
        i.putExtra("bombs",b);
        i.putExtra("mode",mode);
        i.putExtra("level",level);
        startActivityForResult(i,0);
    }

    public void IDoesNotKnow(String level){
        Intent i = new Intent(getApplicationContext(), Mastermind.class);
        i.putExtra("colors",c);
        i.putExtra("holes",ho);
        i.putExtra("level",level);
        startActivityForResult(i,0);
    }


    public void setInsTextMS(){
        String inst ="You are a pirate. You search for a treasure on an island, but it is full of zombies. Your goal is to uncover the island without coming across a zombie.\n" +
                "\n" +
                "Click on a square in order to expose him. Each number tells you how many zombies are surrounding the square. If you suspect a square that it is a zombie, change the mode to gravestone mode and mark the square or long click on the square. If all the zombies that touch the number are marked with gravestone, you can expose all the other squares by pressing again the number. You will win if you uncover all the safe squares and lose if you click on a zombie. If you play find the treasure, your goal is to reveal the treasure, which is surrounding with zombies.\n";

        for (int i = 0; i < user.records.size(); i++) {
            switch (user.records.get(i).game){
                case "eac":
                    inst+="\n\nYour best score in easy classic "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "eat":
                    inst+="\n\nYour best score in easy treasure "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "mc":
                    inst+="\n\nYour best score in medium classic "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "mt":
                    inst+="\n\nYour best score in medium treasure "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "exc":
                    inst+="\n\nYour best score in expert classic "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "ext":
                    inst+="\n\nYour best score in expert treasure "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
            }
        }
        instr.setText(inst);
    }
    public void setInsTextMM(){
        String inst="You are a pirate. During search for a treasure on an island, you find an ancient temple. In order to enter the temple you need to break a code. Your goal is to find out what is the right order of colors.\n" +
                "\nEach turn guess the pattern of the colors. After pressing OK, a clue will be uncovered. The number in black is how many are corrected in both color and position. The number in white is how many colors are not in the right positions. You win if you guess the right pattern and lose if you do not succeed to guess in twelve turns.\n";

        for (int i = 0; i < user.records.size(); i++) {
            switch (user.records.get(i).game){
                case "eaMaster":
                    inst+="\n\nYour best score in easy is "+user.records.get(i).sec+" guesses";
                    break;
                case "mMaster":
                    inst+="\n\nYour best score in medium is "+user.records.get(i).sec+" guesses";
                    break;
                case "exMaster":
                    inst+="\n\nYour best score in expert is "+user.records.get(i).sec+" guesses";
                    break;
            }
        }
        instr.setText(inst);
    }

    public void setTextNum(TextView tv, int num){
        if(num<10){
            tv.setText("0"+num);
        }
        else{
            tv.setText(""+num);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent in = new Intent();
            if(data.getExtras().getBoolean("logout")){
                in.putExtra("logout",true);
            }
            else in.putExtra("logout",false);
            setResult(RESULT_OK,in);
            finish();
        }
        else if(resultCode==RESULT_CANCELED){
            Intent in = new Intent();
            setResult(RESULT_CANCELED,in);
            finish();
        }
    }
}
