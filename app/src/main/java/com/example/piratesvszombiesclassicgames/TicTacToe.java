package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Random;

public class TicTacToe extends AppCompatActivity {

    ImageView[][] vboard;
    TableLayout board;
    int counter=0;
    TextView title;
    ImageView menu;
    insDialog instr;
    MenuPlus mp;
    SettingsDialog sd;
    String mode;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        instr = new insDialog(this);
        instr.setText("You are the captain of crew of pirates. During search for a treasure on an island, you face in front of an abyss. Your goal is to make a chain of three pirates in order to cross the abyss. Be careful there are zombies who try to sabotage your mission.\n" +
                "\nEach player choose which square to put his figure on. The first to get three figures in a horizontal, vertical or diagonal is the winner\n");

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

        vboard= new ImageView[3][3];
        board = (TableLayout) findViewById(R.id.board);
        int x = pubFun.dpToPx(100,this);
        int y = pubFun.dpToPx(15,this);
        for(int i =0; i<3;i++){
            TableRow tr = new TableRow(this);
            board.addView(tr);
            for(int j = 0; j<3;j++){
                final int w = j;
                final int h = i;
                ImageView b = new ImageView(this);
                b.setLayoutParams(new TableRow.LayoutParams(x,x));
                b.setBackgroundResource(R.drawable.button);
                b.setTag("n");
                b.setPadding(y,y,y,y);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked((ImageView) view);
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
                clicked((ImageView)message.obj);
                clickable(true);
                return true;
            }
        });
    }

    public void clicked(ImageView iv) {
        pubFun.clickSound(getBaseContext());
            if(counter%2==0){
                pubFun.figure(true,iv);
                iv.setTag("X");
                title.setText("Zombies Turn");
                counter++;
                if(Check("X")) winAlert(0);
                else if(counter==9)  winAlert(2);
                else {
                    if(mode.equals("single"))
                        computer();
                }
            }
            else{
                pubFun.figure(false,iv);
                iv.setTag("O");
                title.setText("Pirates Turn");
                counter++;
                if(Check("O")) {
                    winAlert(1);
                }
                else if(counter==9) {
                    winAlert(2);
                }
            }
        iv.setClickable(false);
    }

    public boolean Check(String a){
        int c =0;
        for(int i =0;i<3;i++){
            for(int j=0;j<3;j++){
                if(vboard[i][j].getTag()==a)
                    c++;
            }
            if(c==3)
                return true;
            c=0;
        }
        c=0;
        for(int j =0;j<3;j++){
            for(int i=0;i<3;i++){
                if(vboard[i][j].getTag()==a)
                    c++;
            }
            if(c==3)
                return true;
            c=0;
        }
        c=0;
        int j =0;
        for(int i =0;i<3;i++){
            if(vboard[i][j].getTag()==a)
                c++;
            j++;
        }
        if(c==3)
            return true;
        j =2;
        c=0;
        for(int i =0;i<3;i++){
            if(vboard[i][j].getTag()==a)
                c++;
            j--;
        }
        if(c==3)
            return true;
        return false;
    }

    public void winAlert(int x){
        clickable(false);
        AlertDialog.Builder tie = new AlertDialog.Builder(TicTacToe.this);
        tie.setCancelable(true);
        switch (x){
            case 0:
                tie.setTitle("The Pirates Win!");
                if(mode.equals("single"))
                    pubFun.addCoins(25);
                break;
            case 1:
                tie.setTitle("The Zombies Win!");
                break;
            case 2:
                tie.setTitle("It is a Tie!");
                break;
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
                for(int z =0;z<3;z++)
                    for(int j = 0;j<3;j++)
                    {vboard[z][j].setClickable(true);
                        vboard[z][j].setImageResource(R.drawable.blank);
                        vboard[z][j].setTag("n");
                    }
                counter=0;
                title.setText("Pirates Turn");
            }
        });
        tie.show();
    }

    public void clickable(boolean b){
        for(int i =0;i<3;i++)
            for(int j = 0;j<3;j++)
                vboard[i][j].setClickable(b);
    }

    public void computer(){
        clickable(false);
        ImageView b= Put("O");
        if(b!=null)
        {
            myDelay md = new myDelay(b);
            md.start();
        }
        else{
            b=Put("X");
            if(b!=null)
            {
                myDelay md = new myDelay(b);
                md.start();
            }
            else{
                Random r = new Random();
                int n1 = 1;
                int n2 = 1;
                while (vboard[n1][n2].getTag()!="n"){
                    n1 = r.nextInt(3);
                    n2 = r.nextInt(3);
                }
                myDelay md = new myDelay(vboard[n1][n2]);
                md.start();
            }
        }
    }

    public ImageView Put(String a){
        int c =0;
        for(int i =0;i<3;i++){
            for(int j=0;j<3;j++){
                if(vboard[i][j].getTag()==a)
                    c++;
            }
            if(c==2)
            {
                if(vboard[i][0].getTag()=="n")
                    return vboard[i][0];
                if(vboard[i][1].getTag()=="n")
                    return vboard[i][1];
                if(vboard[i][2].getTag()=="n")
                    return vboard[i][2];
            }
            c=0;
        }
        c=0;
        for(int j =0;j<3;j++){
            for(int i=0;i<3;i++){
                if(vboard[i][j].getTag()==a)
                    c++;
            }
            if(c==2)
            {
                if(vboard[0][j].getTag()=="n")
                    return vboard[0][j];
                if(vboard[1][j].getTag()=="n")
                    return vboard[1][j];
                if(vboard[2][j].getTag()=="n")
                    return vboard[2][j];
            }
            c=0;
        }
        c=0;
        int j =0;
        for(int i =0;i<3;i++){
            if(vboard[i][j].getTag()==a)
                c++;
            j++;
        }
        if(c==2)
        {
            if(vboard[0][0].getTag()=="n")
                return vboard[0][0];
            if(vboard[1][1].getTag()=="n")
                return vboard[1][1];
            if(vboard[2][2].getTag()=="n")
                return vboard[2][2];
        }
        j =2;
        c=0;
        for(int i =0;i<3;i++){
            if(vboard[i][j].getTag()==a)
                c++;
            j--;
        }
        if(c==2)
        {
            if(vboard[2][0].getTag()=="n")
                return vboard[2][0];
            if(vboard[1][1].getTag()=="n")
                return vboard[1][0];
            if(vboard[0][2].getTag()=="n")
                return vboard[0][2];
        }
        return null;
    }

    public class myDelay extends Thread{
        private Object x;

        public myDelay(Object x){
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
            msg.obj =x;
            handler.sendMessage(msg);
        }
    }
}
