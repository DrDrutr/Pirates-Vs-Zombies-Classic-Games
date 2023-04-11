package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Mancala extends AppCompatActivity {

    TableLayout board;
    RelativeLayout[][] vboard;
    TextView[][] tvboard;
    boolean turn = true;
    ImageView menu;
    Random rnd = new Random();
    insDialog instr;
    MenuPlus mp;
    SettingsDialog sd;
    int[][] theboard;
    String mode;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mancala);
        getSupportActionBar().hide();
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        instr = new insDialog(this);
        instr.setText("You are the pirate. You are stuck on an island and need to collect resources in order to build a cover for the night. You find stacks of stones, but a zombie guards the stones. Your goal is to collect many stones as you can.\n" +
                "\n" +
                "Every stack of stones contains 6 stones. Each player takes all the stones from ant stack on his side of the board and spreads them one by one anti clockwise in each stack, in his side and his opponent's side (including his bank but not the opponent bank). The stack in which the last stone falls determines the 2 rules of the game:\n" +
                "1. Last stone in a player's bank entitles the player to play another turn.\n" +
                "2. Last stone in an empty stack on the player's side, the player wins the stone and any other stones that are on the opponent opposite stack.\n" +
                "The game ends when there are no stones left on one side of the board. The rest of the stones are moved to the last player's bank. The player with the most stones in his bank is the winner.\n");

        vboard= new RelativeLayout[7][2];
        tvboard = new TextView[7][2];
        theboard = new int[7][2];
        board = (TableLayout) findViewById(R.id.board);
        LinearLayout tr1 = new LinearLayout(this);
        board.addView(tr1);
        LinearLayout.LayoutParams lp = new TableRow.LayoutParams(pubFun.dpToPx(50,getBaseContext()),pubFun.dpToPx(50,getBaseContext()));
        LinearLayout.LayoutParams lp1 = new TableRow.LayoutParams(pubFun.dpToPx(100,getBaseContext()),pubFun.dpToPx(50,getBaseContext()));
        ImageView ivv = new ImageView(this);
        ivv.setLayoutParams(lp);
        ivv.setImageResource(R.drawable.blank);
        tr1.addView(ivv);
        RelativeLayout b1 = new RelativeLayout(this);
        b1.setLayoutParams(lp1);
        b1.setBackgroundResource(R.drawable.hole);
        vboard[0][1]=b1;
        tr1.addView(b1);
        ImageView iv = new ImageView(this);
        iv.setLayoutParams(lp);
        pubFun.figure(true,iv);
        tr1.addView(iv);
        for(int i =1; i<7;i++){
            TableRow tr = new TableRow(this);
            board.addView(tr);
            for(int j = 0; j<2;j++){
                TextView tv = new TextView(this);
                tv.setText("06");
                tv.setTypeface(custom_font);
                tv.setBackgroundResource(R.drawable.button);
                tv.setLayoutParams(lp);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,40);
                tv.setShadowLayer(5,0,0,0xff672918);
                tv.setGravity(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(0xffffffff);
                tvboard[i][j]=tv;
                theboard[i][j]=6;
                if(j==0)
                    tr.addView(tv);
                final int w = j;
                final int h = i;
                RelativeLayout b = new RelativeLayout(this);
                b.setLayoutParams(lp);
                b.setBackgroundResource(R.drawable.hole);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clicked(h,w);
                    }
                });
                vboard[i][j]=b;
                tr.addView(b);
                for (int k = 0; k < 6; k++) {
                    addStone(i,j);
                }
                if(j==1)
                    tr.addView(tv);
            }
        }
        LinearLayout tr2 = new LinearLayout(this);
        board.addView(tr2);
        ImageView iv1 = new ImageView(this);
        iv1.setLayoutParams(lp);
        pubFun.figure(false,iv1);
        tr2.addView(iv1);
        b1 = new RelativeLayout(this);
        b1.setLayoutParams(lp1);
        b1.setBackgroundResource(R.drawable.hole);
        vboard[0][0]=b1;
        tr2.addView(b1);
        tvboard[0][1] = (TextView) findViewById(R.id.wc);
        tvboard[0][0] = (TextView) findViewById(R.id.bc);
        tvboard[0][1].setTypeface(custom_font);
        tvboard[0][0].setTypeface(custom_font);
        theboard[0][1]=0;
        theboard[0][1]=0;
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
        pubFun.figure(true,(ImageView)findViewById(R.id.wp));
        pubFun.figure(false,(ImageView)findViewById(R.id.zp));
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getString("mode");
        blockSingle();
        changeColor(1);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                computer();
                if(!turn){
                    delay d = new delay();
                    d.start();
                }
                return true;
            }
        });
    }

    public void clicked(int i, int j){
        pubFun.clickSound(getBaseContext());
        int x = Integer.parseInt(tvboard[i][j].getText().toString());
        if(turn&&j==1&&x!=0){
            tvboard[i][j].setText("00");
            theboard[i][j]=0;
            vboard[i][j].removeAllViews();
            int k =i-1;
            boolean addTurn = false;
            while(x!=0) {
                x = wside(k, x);
                if (x != 0) {
                    rising(0, 1,1);
                    addStone(0,1);
                    x--;
                    if (x != 0) {
                        x = bside(1, x);
                        k=6;
                    }
                    else addTurn = true;
                }
            }
            if(!isWinner()) {
                if (!addTurn) {
                    turn = false;
                    changeColor(0);
                    if (mode.equals("single")) {
                        delay d = new delay();
                        d.start();
                    }
                }
            }
        }
        else if(!turn&&j==0&&x!=0){
            tvboard[i][j].setText("00");
            theboard[i][j]=0;
            vboard[i][j].removeAllViews();
            int k =i+1;
            boolean addTurn = false;
            while(x!=0) {
                x = bside(k, x);
                if (x != 0) {
                    rising(0, 0,1);
                    addStone(0,0);
                    x--;
                    if (x != 0) {
                        x = wside(6, x);
                        k=1;
                    }
                    else addTurn = true;
                }
            }
            if(!isWinner()) {
                if (!addTurn) {
                    turn = true;
                    changeColor(1);
                }
            }
        }
    }

    public void rising(int i, int j,int sum){
        int x = Integer.parseInt(tvboard[i][j].getText().toString());
        x+=sum;
        if(x<10){
            tvboard[i][j].setText("0"+x);
            theboard[i][j]=x;
        }
        else {
            tvboard[i][j].setText(""+x);
            theboard[i][j]=x;
        }
    }

    public int wside(int i, int x){
        for (; i >0 && x>0 ; i--) {
            rising(i,1,1);
            addStone(i,1);
            x--;
        }
        if(turn&&x==0){
            i++;
            int a = Integer.parseInt(tvboard[i][1].getText().toString());
            if(a==1) {
                int b = Integer.parseInt(tvboard[i][0].getText().toString());
                tvboard[i][0].setText("00");
                tvboard[i][1].setText("00");
                theboard[i][0]=0;
                theboard[i][1]=0;
                vboard[i][0].removeAllViews();
                vboard[i][1].removeAllViews();
                for (int j = 0; j < (a+b); j++) {
                    addStone(0,1);
                }
                rising(0, 1, a + b);
            }
        }
        return x;
    }

    public int bside(int i, int x){
        for (; i <7 && x>0 ; i++) {
            rising(i,0,1);
            addStone(i,0);
            x--;
        }
        if(!turn&&x==0){
            i--;
            int b = Integer.parseInt(tvboard[i][0].getText().toString());
            if(b==1) {
                int a = Integer.parseInt(tvboard[i][1].getText().toString());
                tvboard[i][0].setText("00");
                tvboard[i][1].setText("00");
                theboard[i][0]=0;
                theboard[i][1]=0;
                vboard[i][0].removeAllViews();
                vboard[i][1].removeAllViews();
                for (int j = 0; j < (a+b); j++) {
                    addStone(0,0);
                }
                rising(0, 0, a + b);
            }
        }
        return x;
    }

    public boolean isWinner(){
        if(checkEmpty(0)){
            clearWinner(0);
            winAlert();
            return true;
        }
        else if (checkEmpty(1)){
            clearWinner(1);
            winAlert();
            return true;
        }
        return false;
    }

    public boolean checkEmpty(int j){
        for (int i = 1; i < 7; i++) {
            if(Integer.parseInt(tvboard[i][j].getText().toString())!=0)
                return false;
        }
        return true;
    }

    public void clearWinner(int j){
        int sum =0;
        if(j==1){
            for (int i = 1; i < 7; i++) {
                sum+=Integer.parseInt(tvboard[i][0].getText().toString());
                tvboard[i][0].setText("00");
                vboard[i][0].removeAllViews();
            }
        }
        else{
            for (int i = 1; i < 7; i++) {
                sum+=Integer.parseInt(tvboard[i][1].getText().toString());
                tvboard[i][1].setText("00");
                vboard[i][1].removeAllViews();
            }
        }
        if(turn)
            rising(0,1,sum);
        else rising(0,0,sum);
    }

    public void winAlert(){
        for (int i = 1; i < 7; i++) {
            for (int j = 0; j < 2; j++) {
                vboard[i][j].setClickable(false);
            }
        }
        AlertDialog.Builder tie = new AlertDialog.Builder(Mancala.this);
        tie.setCancelable(true);
        int a = Integer.parseInt(tvboard[0][1].getText().toString());
        int b = Integer.parseInt(tvboard[0][0].getText().toString());
        if(a>b){
            tie.setTitle("The Pirates Win!");
            if(mode.equals("single"))
                pubFun.addCoins(50);
        }
        else if(b>a){
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
                turn = true;
                for (int a = 1; a < 7; a++) {
                    for (int j = 0; j < 2; j++) {
                        vboard[a][j].setClickable(true);
                        tvboard[a][j].setText("06");
                        theboard[a][j]=6;
                    }
                }
                changeColor(1);
                tvboard[0][0].setText("00");
                tvboard[0][1].setText("00");
                theboard[0][0]=0;
                theboard[0][1]=0;
                for (int p = 1; p < 7; p++) {
                    for (int j = 0; j < 2; j++) {
                        for (int k = 0; k < 6; k++) {
                            addStone(p,j);
                        }
                    }
                }
                vboard[0][0].removeAllViews();
                vboard[0][1].removeAllViews();
                blockSingle();
            }
        });
        tie.show();
    }

    public void addStone(int i,int j){
        ImageView iv = new ImageView(this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(pubFun.dpToPx(20,this),pubFun.dpToPx(20,this));
        iv.setImageResource(R.drawable.stone);
        int x =0;
        int y= 0;
        if(i!=0) {
            x = rnd.nextInt(20) + 5;
            y = rnd.nextInt(20) + 10;
        }
        else{
            x = rnd.nextInt(70) + 5;
            y = rnd.nextInt(20) + 10;
        }
        iv.setX(pubFun.dpToPx(x,this));
        iv.setY(pubFun.dpToPx(y,this));
        iv.setLayoutParams(lp);
        vboard[i][j].addView(iv);
    }

    public void changeColor(int j){
        for (int i = 1; i < 7; i++) {
            vboard[i][j].setBackgroundResource(R.drawable.orangehole);
        }
        j=Math.abs(j-1);
        for (int i = 1; i < 7; i++) {
            vboard[i][j].setBackgroundResource(R.drawable.hole);
        }
    }

    public void computer(){
        MancalaAI ai = new MancalaAI(theboard,turn,0);
        TreeNode<MancalaAI> t = new TreeNode<>(ai);
        buildtree(t,0);
        if(!t.getSons().isEmpty()) {
            maxmin(t);
            t = theChosenOne(t);
            clicked(t.getBoard().getI(), 0);
        }
        else turn = true;
    }
    public void buildtree(TreeNode<MancalaAI> t, int level){
        if(level<6&&!t.getBoard().isOver){
            int j;
            if(t.getBoard().isTurn())
                j=1;
            else j=0;
            for (int k = 1; k < 7; k++) {
                if(t.getBoard().getBoard()[k][j]!=0){
                    MancalaAI ai = new MancalaAI(t.getBoard().getBoard(),t.getBoard().isTurn(),k);
                    ai.clicked();
                    TreeNode<MancalaAI> t1 = new TreeNode<>(ai);
                    t.getSons().add(t1);
                    int x = level+1;
                    buildtree(t1,x);
                }
            }
        }
    }

    public void maxmin(TreeNode<MancalaAI> t){
        if(!t.getSons().isEmpty()) {
            for (int i = 0; i < t.getSons().size(); i++) {
                maxmin(t.getSons().get(i));
            }
            if(t.getBoard().isTurn()){
                int maxDiffer = t.getSons().get(0).getBoard().difference;
                for (int i = 1; i < t.getSons().size(); i++) {
                    int hi = t.getSons().get(i).getBoard().difference;
                    if (hi < maxDiffer) {
                        maxDiffer = hi;
                    }
                }
                t.getBoard().difference = maxDiffer;
            }
            else {
                int maxDiffer = t.getSons().get(0).getBoard().difference;
                for (int i = 1; i < t.getSons().size(); i++) {
                    int hi = t.getSons().get(i).getBoard().difference;
                    if (hi > maxDiffer) {
                        maxDiffer = hi;
                    }
                }
                t.getBoard().difference = maxDiffer;
            }
        }
    }

    public TreeNode<MancalaAI> theChosenOne(TreeNode<MancalaAI> t){
        ArrayList<TreeNode<MancalaAI>> equals = new ArrayList<>();
        for (int i = 0; i < t.getSons().size(); i++) {
            if(t.getBoard().difference==t.getSons().get(i).getBoard().difference)
                equals.add(t.getSons().get(i));
        }
        Random r = new Random();
        int num = r.nextInt(equals.size());
        return equals.get(num);
    }

    public class delay extends Thread{
        public delay(){}
        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.sendEmptyMessage(0);
        }
    }

    public void blockSingle(){
        if(mode.equals("single")){
            for (int i = 1; i < 7; i++) {
                vboard[i][0].setClickable(false);
            }
        }
    }
}
