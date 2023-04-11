package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Connect4 extends AppCompatActivity {

    RelativeLayout theBack;
    TableLayout board;
    ImageView[][] vboard;
    int wc = 0;
    int bc =0;
    int turn = 0;
    ImageView menu;
    TextView title;
    insDialog instr;
    MenuPlus mp;
    SettingsDialog sd;
    String mode;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect4);
        getSupportActionBar().hide();
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        instr = new insDialog(this);
        instr.setText("You are the captain of a crew of pirates. During search for a treasure on an island, you face in front of a cliff. Your goal is to make a chain of four pirates in order to climb on the cliff. Be careful there are zombies who try to sabotage your mission.\n" +
                "\nEach player chooses which column to put his figure in. The first to get 4 figures in a row horizontal, vertical or diagonal is the winner.\n");

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

        vboard= new ImageView[7][7];
        board = (TableLayout) findViewById(R.id.board);
        TableRow tr1 = new TableRow(this);
        board.addView(tr1);
        int x = pubFun.dpToPx(50,this);
        for(int j = 0; j<7;j++){
            final int w = j;
            final int h = 0;
            ImageView b = new ImageView(this);
            b.setLayoutParams(new TableRow.LayoutParams(x,x));
            b.setBackgroundResource(R.drawable.arrow);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                        clicked(w);
                }
            });
            vboard[0][j]=b;
            tr1.addView(b);

        }
        for(int i =1; i<7;i++){
            TableRow tr = new TableRow(this);
            board.addView(tr);
            for(int j = 0; j<7;j++){
                final int w = j;
                final int h = i;
                ImageView b = new ImageView(this);
                b.setLayoutParams(new TableRow.LayoutParams(x,x));
                b.setTag("n");
                b.setBackgroundResource(R.drawable.hole);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            clicked(w);
                    }
                });
                vboard[i][j]=b;
                tr.addView(b);

            }
        }
        title = (TextView) findViewById(R.id.title);
        title.setTypeface(custom_font);
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getString("mode");
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                clicked(message.arg1);
                clickable(true);
                return true;
            }
        });
        theBack = (RelativeLayout)findViewById(R.id.theBack);
    }

    public void clicked(int j) {
        pubFun.clickSound(getBaseContext());
        int x =getLow(j);
        if(x!=-1){
            if(turn%2==0){
                pubFun.figure(true,vboard[x][j]);
                vboard[x][j].setTag("w");
                wc++;
                turn++;
                title.setText("Zombies Turn");
                if(isConnect(x,j,"w"))
                    winAlert(0);
                else if( turn ==41) winAlert(3);
                else if(mode.equals("single"))
                    computer();
            }
            else{
                pubFun.figure(false,vboard[x][j]);
                vboard[x][j].setTag("b");
                bc++;
                turn++;
                title.setText("Pirates Turn");
                if(isConnect(x,j,"b"))
                    winAlert(1);
                else if( turn ==41) winAlert(3);
            }
        }
    }

    public int getLow(int j){
        for (int i = 6; i > 0; i--) {
            if(vboard[i][j].getTag()=="n")
                return i;
        }
        return -1;
    }

    public boolean isConnect(int i, int j, String a){
        int counter =0;
        for (int k = i; k < 7&&vboard[k][j].getTag()==a; k++) {
            counter++;
        }
        if(counter>=4)
            return true;
        counter=0;
        for (int k = j; k < 7&&vboard[i][k].getTag()==a; k++) {
            counter++;
        }
        if(counter>=4)
            return true;
        for (int k = j-1; k >-1 &&vboard[i][k].getTag()==a; k--) {
            counter++;
        }
        if(counter>=4)
            return true;
        counter=0;
        int l = j;
        for (int k = i; k < 7&&l<7&&vboard[k][l].getTag()==a; k++) {
            counter++;
            l++;
        }
        if(counter>=4)
            return true;
        l=j-1;
        for (int k = i-1; k >0&&l>-1&&vboard[k][l].getTag()==a; k--) {
            counter++;
            l--;
        }
        if(counter>=4)
            return true;
        counter=0;
        l = j;
        for (int k = i; k < 7&&l>-1&&vboard[k][l].getTag()==a; k++) {
            counter++;
            l--;
        }
        if(counter>=4)
            return true;
        l=j+1;
        for (int k = i-1; k >0&&l<7&&vboard[k][l].getTag()==a; k--) {
            counter++;
            l++;
        }
        if(counter>=4)
            return true;
        return false;
    }

    public void winAlert(int x){
        clickable(false);
        AlertDialog.Builder tie = new AlertDialog.Builder(Connect4.this);
        tie.setCancelable(true);
        if(x==0){
            tie.setTitle("The Pirates Win!");
            if(mode.equals("single"))
                pubFun.addCoins(50);
        }
        else if(x==1){
            tie.setTitle("The Zombies Win!");
        }
        else{
            tie.setTitle("It is a Tie!");
        }
        tie.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
            }
        });
        tie.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
                title.setText("Pirates Turn");
                turn = 0;
                for(int j = 0; j<7;j++){
                    vboard[0][j].setClickable(true);
                }
                for (int a = 1; a < 7; a++) {
                    for (int j = 0; j < 7; j++) {
                        vboard[a][j].setClickable(true);
                        vboard[a][j].setImageResource(R.drawable.blank);
                        vboard[a][j].setTag("n");
                    }
                }
            }
        });
        tie.show();
    }

    public void clickable(boolean b){
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                vboard[i][j].setClickable(b);
            }
        }
    }

    public void computer(){
        clickable(false);
        int x = Put("b");
        if(x!=-1){
            myDelay md = new myDelay(x);
            md.start();
        }
        else{
            x = Put("w");
            if(x!=-1){
                myDelay md = new myDelay(x);
                md.start();
            }
            else{
                Random r = new Random();
                int n1 = r.nextInt(7);
                while (getLow(n1)==-1){
                    n1 = r.nextInt(7);;
                }
                myDelay md = new myDelay(n1);
                md.start();
            }
        }
    }

    public int Put(String a){
        for (int k = 0; k < 7; k++) {
            int i = getLow(k);
            if(i!=-1){
                vboard[i][k].setTag(a);
                if(isConnect(i,k,a)) {
                    vboard[i][k].setTag("n");
                    return k;
                }
                vboard[i][k].setTag("n");
            }
        }
        return -1;
    }

    public class myDelay extends Thread{
        private int x;

        public myDelay(int x){
            this.x=x;
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.arg1=x;
            handler.sendMessage(msg);
        }
    }
}
