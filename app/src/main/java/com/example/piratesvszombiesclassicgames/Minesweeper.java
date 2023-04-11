package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Display;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class Minesweeper extends AppCompatActivity {

    int width;
    int height;
    int bombs; //num of bombs
    TextView grave; //num of graves which have not been found
    String level;
    String mode;
    TableLayout board;
    int[][] answer = null;
    ImageView[][] vboard;
    Random rnd;
    boolean putGrave = false;
    ImageView pg;
    HorizontalScrollView hi;
    Chronometer cm;
    ImageView menu;
    SharedPreferences sp;
    insDialog instr;
    User user;
    MenuPlus mp;
    SettingsDialog sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minesweeper);
        getSupportActionBar().hide();
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        user = pubFun.user;

        instr = new insDialog(this);
        setInsText();

        Bundle bundle = getIntent().getExtras();
        width = bundle.getInt("width");
        height = bundle.getInt("height");
        bombs = bundle.getInt("bombs");
        level = bundle.getString("level");
        mode = bundle.getString("mode");
        rnd = new Random();
        grave = (TextView) findViewById(R.id.grave);
        grave.setText(""+bombs);
        vboard= new ImageView[height][width];
        board = (TableLayout) findViewById(R.id.board);
        for(int i =0; i<height;i++){
            TableRow tr = new TableRow(this);
            board.addView(tr);
            for(int j = 0; j<width;j++){
                final int w = j;
                final int h = i;
                ImageView b = new ImageView(this);
                b.setLayoutParams(new TableRow.LayoutParams(pubFun.dpToPx(50,this),pubFun.dpToPx(50,this)));
                b.setTag("not");
                b.setBackgroundResource(R.drawable.button);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked(h,w,(ImageView) view);
                    }
                });
                b.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        pubFun.clickSound(getBaseContext());
                        ImageView iv = (ImageView) view;
                        if(answer==null && putGrave) {
                            creatingBoard(h, w);
                            cm.setBase(SystemClock.elapsedRealtime());
                            cm.start();
                        }
                        if(iv.getTag().toString()=="found")
                            expose(h,w);
                        else if(!putGrave){
                            switch (iv.getTag().toString()){
                                case "not":
                                    iv.setImageResource(R.drawable.gravestone);
                                    reduceCounter(grave);
                                    iv.setTag("graved");
                                    break;
                                case "graved":
                                    iv.setImageResource(R.drawable.blank);
                                    riseCounter(grave);
                                    iv.setTag("not");
                                    break;
                            }
                        }
                        else{
                            checkSquare(h,w);
                        }
                        return true;
                    }
                });
                vboard[i][j]=b;
                tr.addView(b);

            }
        }
        pg = (ImageView) findViewById(R.id.putGrave);
        pg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(putGrave) {
                    putGrave = false;
                    pg.setImageResource(R.drawable.zero);
                }
                else {
                    putGrave = true;
                    pg.setImageResource(R.drawable.gravestone);
                }
            }
        });
        cm = (Chronometer) findViewById(R.id.cm);
        cm.setTypeface(custom_font);
        grave.setTypeface(custom_font);
        menu = (ImageView) findViewById(R.id.menu);
        sd = new SettingsDialog(this);
        mp = new MenuPlus(this,menu,instr,sd);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.show();
                pubFun.clickSound(getBaseContext());
            }
        });
        pubFun.figure(false,(ImageView)findViewById(R.id.cp));
    }

    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int he = size.y - findViewById(R.id.imageView7).getHeight() - pg.getHeight()-pg.getHeight();
        hi = (HorizontalScrollView) findViewById(R.id.hi);
        hi.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, he));
    }

    public void clicked(int i, int j, ImageView iv){
        pubFun.clickSound(getBaseContext());
        if(answer==null && !putGrave) {
            creatingBoard(i, j);
            cm.setBase(SystemClock.elapsedRealtime());
            cm.start();
        }
        if(iv.getTag().toString()=="found")
            expose(i,j);
        else if(putGrave){
            switch (iv.getTag().toString()){
                case "not":
                    iv.setImageResource(R.drawable.gravestone);
                    reduceCounter(grave);
                    iv.setTag("graved");
                    break;
                case "graved":
                    iv.setImageResource(R.drawable.blank);
                    riseCounter(grave);
                    iv.setTag("not");
                    break;
            }
        }
        else{
            checkSquare(i,j);
        }
    }
    public void checkSquare(int i,int j){
        if(vboard[i][j].getTag().toString()!="found")
        switch (answer[i][j]){
            case -2:
                vboard[i][j].setBackgroundResource(R.drawable.treasure);
                isWin();
                break;
            case -1:
                if(!vboard[i][j].getTag().toString().equals("graved"))
                    isBomb();
                break;
            case 0:
                isZero(i,j);
                break;
            default:
                num(i,j);
        }
    }

    public void creatingBoard(int i, int j){
        answer = new int[height][width];
        int counter = bombs;
        if(mode.equals("treasure")){
            int x=rnd.nextInt(height);
            int y =rnd.nextInt(width);
            while(x==0||y==0||x==height-1||y==width-1||(x==i&&y==j)||(x==i-1&&y==j)||(x==i+1&&y==j)||(x==i&&y==j-1)||(x==i&&y==j+1)||(x==i+1&&y==j+1)||(x==i-1&&y==j+1)||(x==i+1&&y==j-1)||(x==i-1&&y==j-1)||(x==i-2&&y==j)||(x==i+2&&y==j)||(x==i&&y==j-2)||(x==i&&y==j+2)||(x==i+2&&y==j+2)||(x==i-2&&y==j+2)||(x==i+2&&y==j-2)||(x==i-2&&y==j-2)){
                x = rnd.nextInt(height);
                y = rnd.nextInt(width);
            }
            answer[x][y] = -2;
            answer[x-1][y] = -1;
            answer[x][y-1] = -1;
            answer[x+1][y] = -1;
            answer[x][y+1] = -1;
            answer[x-1][y-1] = -1;
            answer[x+1][y+1] = -1;
            answer[x+1][y-1] = -1;
            answer[x-1][y+1] = -1;
            counter-=8;
        }

        for(;counter>0;counter--){
            int x=rnd.nextInt(height);
            int y =rnd.nextInt(width);
            while(answer[x][y]!=0||(x==i&&y==j)||(x==i-1&&y==j)||(x==i+1&&y==j)||(x==i&&y==j-1)||(x==i&&y==j+1)||(x==i+1&&y==j+1)||(x==i-1&&y==j+1)||(x==i+1&&y==j-1)||(x==i-1&&y==j-1)){
                x = rnd.nextInt(height);
                y = rnd.nextInt(width);
            }
            answer[x][y]=-1;
        }
        for(int a =1; a<height-1;a++){
            for(int b = 1;b<width-1;b++){
                if(answer[a][b]==0){
                    int bc =0;
                    if(answer[a-1][b]==-1)
                        bc++;
                    if(answer[a][b-1]==-1)
                        bc++;
                    if(answer[a+1][b]==-1)
                        bc++;
                    if(answer[a][b+1]==-1)
                        bc++;
                    if(answer[a+1][b+1]==-1)
                        bc++;
                    if(answer[a-1][b-1]==-1)
                        bc++;
                    if(answer[a+1][b-1]==-1)
                        bc++;
                    if(answer[a-1][b+1]==-1)
                        bc++;
                    answer[a][b]=bc;
                }
            }
        }
        for(int b = 1; b<width-1;b++){
            if(answer[0][b]==0){
                int bc =0;
                if(answer[0][b-1]==-1)
                    bc++;
                if(answer[1][b]==-1)
                    bc++;
                if(answer[0][b+1]==-1)
                    bc++;
                if(answer[1][b+1]==-1)
                    bc++;
                if(answer[1][b-1]==-1)
                    bc++;
                answer[0][b]=bc;
            }
        }
        for(int b = 1; b<width-1;b++){
            if(answer[height-1][b]==0){
                int bc =0;
                if(answer[height-2][b]==-1)
                    bc++;
                if(answer[height-1][b-1]==-1)
                    bc++;
                if(answer[height-1][b+1]==-1)
                    bc++;
                if(answer[height-2][b-1]==-1)
                    bc++;
                if(answer[height-2][b+1]==-1)
                    bc++;
                answer[height-1][b]=bc;
            }
        }
        for(int a = 1;a<height-1;a++){
            if(answer[a][0]==0){
                int bc =0;
                if(answer[a-1][0]==-1)
                    bc++;
                if(answer[a+1][0]==-1)
                    bc++;
                if(answer[a][1]==-1)
                    bc++;
                if(answer[a+1][1]==-1)
                    bc++;
                if(answer[a-1][1]==-1)
                    bc++;
                answer[a][0]=bc;
            }
        }
        for(int a = 1; a<height-1;a++){
            if(answer[a][width-1]==0){
                int bc =0;
                if(answer[a-1][width-1]==-1)
                    bc++;
                if(answer[a][width-2]==-1)
                    bc++;
                if(answer[a+1][width-1]==-1)
                    bc++;
                if(answer[a-1][width-2]==-1)
                    bc++;
                if(answer[a+1][width-2]==-1)
                    bc++;
                answer[a][width-1]=bc;
            }
        }
        if(answer[0][0]==0){
            int bc=0;
            if (answer[0][1] == -1)
                bc++;
            if (answer[1][0] == -1)
                bc++;
            if (answer[1][1] == -1)
                bc++;
            answer[0][0] = bc;
        }
        if(answer[0][width-1]==0){
            int bc=0;
            if (answer[0][width-2] == -1)
                bc++;
            if (answer[1][width-1] == -1)
                bc++;
            if (answer[1][width-2] == -1)
                bc++;
            answer[0][width-1] = bc;
        }
        if(answer[height-1][0]==0){
            int bc=0;
            if (answer[height-1][1] == -1)
                bc++;
            if (answer[height-2][0] == -1)
                bc++;
            if (answer[height-2][1] == -1)
                bc++;
            answer[height-1][0] = bc;
        }
        if(answer[height-1][width-1]==0){
            int bc=0;
            if (answer[height-1][width-2] == -1)
                bc++;
            if (answer[height-2][width-1] == -1)
                bc++;
            if (answer[height-2][width-2] == -1)
                bc++;
            answer[height-1][width-1] = bc;
        }
    }

    public void num(int a, int b){
        vboard[a][b].setTag("found");
        vboard[a][b].setLongClickable(false);
        switch(answer[a][b]){
            case 0:
                vboard[a][b].setImageResource(R.drawable.zero);
                break;
            case 1:
                vboard[a][b].setImageResource(R.drawable.one);
                break;
            case 2:
                vboard[a][b].setImageResource(R.drawable.two);
                break;
            case 3:
                vboard[a][b].setImageResource(R.drawable.three);
                break;
            case 4:
                vboard[a][b].setImageResource(R.drawable.four);
                break;
            case 5:
                vboard[a][b].setImageResource(R.drawable.five);
                break;
            case 6:
                vboard[a][b].setImageResource(R.drawable.six);
                break;
            case 7:
                vboard[a][b].setImageResource(R.drawable.seven);
                break;
            case 8:
                vboard[a][b].setImageResource(R.drawable.eight);
                break;
        }
        checkWinning();
    }

    public void isZero(int i, int j){
        if(!vboard[i][j].getTag().toString().equals("found")){
            num(i,j);
            if(answer[i][j]==0){
                if(i>0&&i<height-1&&j>0&&j<width-1){
                    isZero(i-1,j);
                    isZero(i+1,j);
                    isZero(i,j-1);
                    isZero(i,j+1);
                    isZero(i+1,j-1);
                    isZero(i-1,j+1);
                    isZero(i+1,j+1);
                    isZero(i-1,j-1);
                }
                else if(i==0&&j==0){
                    isZero(i+1,j);
                    isZero(i,j+1);
                    isZero(i+1,j+1);
                }
                else if(i==0&&j==width-1){
                    isZero(i+1,j);
                    isZero(i,j-1);
                    isZero(i+1,j-1);
                }
                else if(i==height-1&&j==0){
                    isZero(i-1,j);
                    isZero(i,j+1);
                    isZero(i-1,j+1);
                }
                else if(i==height-1&&j==width-1){
                    isZero(i-1,j);
                    isZero(i,j-1);
                    isZero(i-1,j-1);
                }
                else if(i==0){
                    isZero(i+1,j);
                    isZero(i,j-1);
                    isZero(i,j+1);
                    isZero(i+1,j-1);
                    isZero(i+1,j+1);
                }
                else if(i==height-1){
                    isZero(i-1,j);
                    isZero(i,j-1);
                    isZero(i,j+1);
                    isZero(i-1,j+1);
                    isZero(i-1,j-1);
                }
                else if(j==0){
                    isZero(i-1,j);
                    isZero(i+1,j);
                    isZero(i,j+1);
                    isZero(i-1,j+1);
                    isZero(i+1,j+1);
                }
                else if(j==width-1){
                    isZero(i-1,j);
                    isZero(i+1,j);
                    isZero(i,j-1);
                    isZero(i+1,j-1);
                    isZero(i-1,j-1);
                }
            }
        }
    }

    public void isBomb(){
        for(int i = 0; i<height;i++){
            for(int j =0; j<width;j++){
                vboard[i][j].setClickable(false);
                vboard[i][j].setLongClickable(false);
                switch(answer[i][j]){
                    case -1:
                        if(vboard[i][j].getTag()!="graved"){
                            pubFun.figure(false,vboard[i][j]);
                            vboard[i][j].setBackgroundResource(R.drawable.blank);
                        }
                        break;
                    case -2:
                        vboard[i][j].setBackgroundResource(R.drawable.treasure);
                        break;
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Minesweeper.this);
        builder.setCancelable(true);
        builder.setTitle("You Lose!");
        builder.setMessage("I am so sorry, but you are a failure.");
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
                answer = null;
                grave.setText(""+bombs);
                cm.setBase(SystemClock.elapsedRealtime());
                for(int a = 0; a<height;a++){
                    for(int b =0; b<width;b++){
                        vboard[a][b].setBackgroundResource(R.drawable.button);
                        vboard[a][b].setImageResource(R.drawable.blank);
                        vboard[a][b].setClickable(true);
                        vboard[a][b].setLongClickable(true);
                        vboard[a][b].setTag("not");
                    }
                }
            }
        });
        builder.show();
        cm.stop();
    }

    public void checkWinning(){
        int count = 0;
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if (!vboard[i][j].getTag().toString().equals("found"))
                {
                    count++;
                }
            }
        }
        if(count==bombs)
            isWin();
    }

    public void isWin(){
        cm.stop();
        long sec = (SystemClock.elapsedRealtime()-cm.getBase())/1000;
        switch(mode){
            case "classic":
                switch (level){
                    case "easy":
                        pubFun.addCoins(25);
                        pubFun.setRecord("eac",sec);
                        setInsText();
                        break;
                    case "medium":
                        pubFun.addCoins(50);
                        pubFun.setRecord("mc",sec);
                        setInsText();
                        break;
                    case "expert":
                        pubFun.addCoins(100);
                        pubFun.setRecord("exc",sec);
                        setInsText();
                        break;
                }
                break;
            case "treasure":
                switch (level){
                    case "easy":
                        pubFun.addCoins(25);
                        pubFun.setRecord("eat",sec);
                        setInsText();
                        break;
                    case "medium":
                        pubFun.addCoins(50);
                        pubFun.setRecord("mt",sec);
                        setInsText();
                        break;
                    case "expert":
                        pubFun.addCoins(100);
                        pubFun.setRecord("ext",sec);
                        setInsText();
                        break;
                }
                break;
        }
        String time = pubFun.timeFormat(sec);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                vboard[i][j].setClickable(false);
                vboard[i][j].setLongClickable(false);
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(Minesweeper.this);
        builder.setCancelable(true);
        builder.setTitle("You Win!");
        builder.setMessage("Congratulations! You have done it in "+time+"!");
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
                answer = null;
                grave.setText(""+bombs);
                cm.setBase(SystemClock.elapsedRealtime());
                for(int a = 0; a<height;a++){
                    for(int b =0; b<width;b++){
                        vboard[a][b].setBackgroundResource(R.drawable.button);
                        vboard[a][b].setImageResource(R.drawable.blank);
                        vboard[a][b].setClickable(true);
                        vboard[a][b].setLongClickable(true);
                        vboard[a][b].setTag("not");
                    }
                }
            }
        });
        builder.show();
    }

    public void reduceCounter(TextView tv){
        int x = Integer.parseInt(tv.getText().toString());
        x--;
        if(x>=10)
            tv.setText(""+x);
        else tv.setText("0"+x);
    }
    public void riseCounter(TextView tv){
        int x = Integer.parseInt(tv.getText().toString());
        x++;
        if(x>=10)
            tv.setText(""+x);
        else tv.setText("0"+x);
    }

    public void setInsText(){
        String inst ="You are a pirate. You search for a treasure on an island, but it is full of zombies. Your goal is to uncover the island without coming across a zombie.\n" +
                "\n" +
                "Click on a square in order to expose him. Each number tells you how many zombies are surrounding the square. If you suspect a square that it is a zombie, change the mode to gravestone mode and mark the square or long click on the square. If all the zombies that touch the number are marked with gravestone, you can expose all the other squares by pressing again the number. You will win if you uncover all the safe squares and lose if you click on a zombie. If you play find the treasure, your goal is to reveal the treasure, which is surrounding with zombies.\n";

        for (int i = 0; i < user.records.size(); i++) {
            switch (user.records.get(i).game){
                case "eac":
                    inst+="\n\nYour best score in easy classic is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "eat":
                    inst+="\n\nYour best score in easy treasure is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "mc":
                    inst+="\n\nYour best score in medium classic is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "mt":
                    inst+="\n\nYour best score in medium treasure is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "exc":
                    inst+="\n\nYour best score in expert classic is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
                case "ext":
                    inst+="\n\nYour best score in expert treasure is "+pubFun.timeFormat(user.records.get(i).sec);
                    break;
            }
        }
        instr.setText(inst);
    }

    public int isGraved(int i,int j){
        if(vboard[i][j].getTag().toString().equals("graved"))
            return 1;
        return 0;
    }
    public void expose(int i, int j){
        int counter =0;
        if(i>0&&i<height-1&&j>0&&j<width-1){
            counter += isGraved(i-1,j);
            counter += isGraved(i+1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i,j+1);
            counter += isGraved(i+1,j-1);
            counter += isGraved(i-1,j+1);
            counter += isGraved(i+1,j+1);
            counter += isGraved(i-1,j-1);
        }
        else if(i==0&&j==0){
            counter += isGraved(i+1,j);
            counter += isGraved(i,j+1);
            counter += isGraved(i+1,j+1);
        }
        else if(i==0&&j==width-1){
            counter += isGraved(i+1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i+1,j-1);
        }
        else if(i==height-1&&j==0){
            counter += isGraved(i-1,j);
            counter += isGraved(i,j+1);
            counter += isGraved(i-1,j+1);
        }
        else if(i==height-1&&j==width-1){
            counter += isGraved(i-1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i-1,j-1);
        }
        else if(i==0){
            counter += isGraved(i+1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i,j+1);
            counter += isGraved(i+1,j-1);
            counter += isGraved(i+1,j+1);
        }
        else if(i==height-1){
            counter += isGraved(i-1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i,j+1);
            counter += isGraved(i-1,j+1);
            counter += isGraved(i-1,j-1);
        }
        else if(j==0){
            counter += isGraved(i-1,j);
            counter += isGraved(i+1,j);
            counter += isGraved(i,j+1);
            counter += isGraved(i-1,j+1);
            counter += isGraved(i+1,j+1);
        }
        else if(j==width-1){
            counter += isGraved(i-1,j);
            counter += isGraved(i+1,j);
            counter += isGraved(i,j-1);
            counter += isGraved(i+1,j-1);
            counter += isGraved(i-1,j-1);
        }
        if(counter==answer[i][j]){
            if(i>0&&i<height-1&&j>0&&j<width-1){
                checkSquare(i-1,j);
                checkSquare(i+1,j);
                checkSquare(i,j-1);
                checkSquare(i,j+1);
                checkSquare(i+1,j-1);
                checkSquare(i-1,j+1);
                checkSquare(i+1,j+1);
                checkSquare(i-1,j-1);
            }
            else if(i==0&&j==0){
                checkSquare(i+1,j);
                checkSquare(i,j+1);
                checkSquare(i+1,j+1);
            }
            else if(i==0&&j==width-1){
                checkSquare(i+1,j);
                checkSquare(i,j-1);
                checkSquare(i+1,j-1);
            }
            else if(i==height-1&&j==0){
                checkSquare(i-1,j);
                checkSquare(i,j+1);
                checkSquare(i-1,j+1);
            }
            else if(i==height-1&&j==width-1){
                checkSquare(i-1,j);
                checkSquare(i,j-1);
                checkSquare(i-1,j-1);
            }
            else if(i==0){
                checkSquare(i+1,j);
                checkSquare(i,j-1);
                checkSquare(i,j+1);
                checkSquare(i+1,j-1);
                checkSquare(i+1,j+1);
            }
            else if(i==height-1){
                checkSquare(i-1,j);
                checkSquare(i,j-1);
                checkSquare(i,j+1);
                checkSquare(i-1,j+1);
                checkSquare(i-1,j-1);
            }
            else if(j==0){
                checkSquare(i-1,j);
                checkSquare(i+1,j);
                checkSquare(i,j+1);
                checkSquare(i-1,j+1);
                checkSquare(i+1,j+1);
            }
            else if(j==width-1){
                checkSquare(i-1,j);
                checkSquare(i+1,j);
                checkSquare(i,j-1);
                checkSquare(i+1,j-1);
                checkSquare(i-1,j-1);
            }
        }
    }
}
