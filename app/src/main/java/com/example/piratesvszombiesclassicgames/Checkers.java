package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class Checkers extends AppCompatActivity implements View.OnClickListener {

    ImageView[][] board = new ImageView[8][8];
    ImageView isClick = null;
    boolean turn = true;
    TextView wc;
    TextView bc;
    boolean haveToEat = false;
    boolean isChainActivated=false;
    AlertDialog.Builder builder;
    int counter;
    ImageView wp;
    ImageView bp;
    ImageView menu;
    insDialog instr;
    SettingsDialog sd;
    MenuPlus mp;
    String mode;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkers);
        getSupportActionBar().hide();
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        instr = new insDialog(this);
        instr.setText("You are the captain of a crew of pirates. During search for a treasure on an island, you encounter a group of zombies. Your goal is to exterminate all the zombies.\n" +
                "\n\nEach player moves a figure diagonally to an adjacent unoccupied square. If the adjacent square contains an opponent's figure, and the square immediately beyond it is vacant, the figure must be captured and removed from the game by jumping over it. Multiple enemy figures can be captured in a single turn provided this is done by successive jumps made by a single piece. When a figure reaches the final line, it becomes a king and can move also backwards. The player without pieces remaining, or who cannot move due to being blocked, loses the game.\n");
        sd = new SettingsDialog(this);

        menu = (ImageView) findViewById(R.id.menu);
        mp = new MenuPlus(this,menu,instr,sd);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.show();
                pubFun.clickSound(getBaseContext());
            }
        });

        wc = (TextView) findViewById(R.id.wc);
        bc = (TextView) findViewById(R.id.bc);
        wp = (ImageView) findViewById(R.id.wcp);
        pubFun.figure(true,wp);
        bp = (ImageView) findViewById(R.id.bcp);
        pubFun.figure(false,bp);
        defineBoard();
        start();
        builder = new AlertDialog.Builder(Checkers.this);
        builder.setCancelable(true);
        builder.setTitle("You have to eat!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        wc.setTypeface(custom_font);
        bc.setTypeface(custom_font);
        Bundle bundle = getIntent().getExtras();
        mode = bundle.getString("mode");
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case 0:
                        int i = message.arg1 / 10;
                        int j = message.arg1 % 10;
                        onClick(board[i][j]);
                        break;
                    case 1:
                        int i1 = message.arg1 / 10;
                        int j1 = message.arg1 % 10;
                        theSecondComputer(message.arg2,i1,j1);
                        break;
                    case 2:
                        TheRealClickable(true);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        pubFun.clickSound(getBaseContext());
        ImageView current = (ImageView) view;
        int i = findImage(board,current);
        int j = i % 10;
        i /= 10;
        if(isClick==null){
            if(board[i][j].getTag()!="n"){
                if(turn){
                    if(haveToEat){
                        if(whiteEat(i,j)){
                            isClick=board[i][j];
                        }
                        else{
                            builder.show();
                        }
                    }
                    else whiteTurn(i,j);
                }
                else {
                    if(haveToEat){
                        if(blackEat(i,j))
                            isClick=board[i][j];
                        else{
                            builder.show();
                        }
                    }
                    else {
                        blackTurn(i,j);
                    }
                }
            }
        }
        else{
            switch (board[i][j].getTag().toString()) {
                case "c":
                    move(i,j);
                    break;
                case "w":
                    if (turn&&!isChainActivated) {
                        if(!haveToEat) {
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            whiteTurn(i, j);
                        }
                        else {
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            if(whiteEat(i,j)){
                                isClick=board[i][j];
                            }
                            else{
                                builder.show();
                            }
                        }
                    }
                    break;
                case "wk":
                    if (turn&&!isChainActivated) {
                        if(!haveToEat) {
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            whiteTurn(i, j);
                        }
                        else{
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            if(whiteEat(i,j)){
                                isClick=board[i][j];
                            }
                            else{
                                builder.show();
                            }
                        }
                    }
                    break;
                case "b":
                    if (!turn&&!isChainActivated) {
                        if(!haveToEat) {
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            blackTurn(i, j);
                        }
                        else{
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            if(blackEat(i,j)){
                                isClick=board[i][j];
                            }
                            else{
                                builder.show();
                            }
                        }
                    }
                    break;
                case "bk":
                    if (!turn&&!isChainActivated) {
                        if(!haveToEat) {
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            blackTurn(i, j);
                        }
                        else{
                            isClick.setBackgroundColor(0xff000000);
                            cleanCheck();
                            if(blackEat(i,j)){
                                isClick=board[i][j];
                            }
                            else{
                                builder.show();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void defineBoard(){
        board[0][0] = (ImageView) findViewById(R.id.I00);
        board[0][1] = (ImageView) findViewById(R.id.I01);
        board[0][2] = (ImageView) findViewById(R.id.I02);
        board[0][3] = (ImageView) findViewById(R.id.I03);
        board[0][4] = (ImageView) findViewById(R.id.I04);
        board[0][5] = (ImageView) findViewById(R.id.I05);
        board[0][6] = (ImageView) findViewById(R.id.I06);
        board[0][7] = (ImageView) findViewById(R.id.I07);
        board[1][0] = (ImageView) findViewById(R.id.I10);
        board[1][1] = (ImageView) findViewById(R.id.I11);
        board[1][2] = (ImageView) findViewById(R.id.I12);
        board[1][3] = (ImageView) findViewById(R.id.I13);
        board[1][4] = (ImageView) findViewById(R.id.I14);
        board[1][5] = (ImageView) findViewById(R.id.I15);
        board[1][6] = (ImageView) findViewById(R.id.I16);
        board[1][7] = (ImageView) findViewById(R.id.I17);
        board[2][0] = (ImageView) findViewById(R.id.I20);
        board[2][1] = (ImageView) findViewById(R.id.I21);
        board[2][2] = (ImageView) findViewById(R.id.I22);
        board[2][3] = (ImageView) findViewById(R.id.I23);
        board[2][4] = (ImageView) findViewById(R.id.I24);
        board[2][5] = (ImageView) findViewById(R.id.I25);
        board[2][6] = (ImageView) findViewById(R.id.I26);
        board[2][7] = (ImageView) findViewById(R.id.I27);
        board[3][0] = (ImageView) findViewById(R.id.I30);
        board[3][1] = (ImageView) findViewById(R.id.I31);
        board[3][2] = (ImageView) findViewById(R.id.I32);
        board[3][3] = (ImageView) findViewById(R.id.I33);
        board[3][4] = (ImageView) findViewById(R.id.I34);
        board[3][5] = (ImageView) findViewById(R.id.I35);
        board[3][6] = (ImageView) findViewById(R.id.I36);
        board[3][7] = (ImageView) findViewById(R.id.I37);
        board[4][0] = (ImageView) findViewById(R.id.I40);
        board[4][1] = (ImageView) findViewById(R.id.I41);
        board[4][2] = (ImageView) findViewById(R.id.I42);
        board[4][3] = (ImageView) findViewById(R.id.I43);
        board[4][4] = (ImageView) findViewById(R.id.I44);
        board[4][5] = (ImageView) findViewById(R.id.I45);
        board[4][6] = (ImageView) findViewById(R.id.I46);
        board[4][7] = (ImageView) findViewById(R.id.I47);
        board[5][0] = (ImageView) findViewById(R.id.I50);
        board[5][1] = (ImageView) findViewById(R.id.I51);
        board[5][2] = (ImageView) findViewById(R.id.I52);
        board[5][3] = (ImageView) findViewById(R.id.I53);
        board[5][4] = (ImageView) findViewById(R.id.I54);
        board[5][5] = (ImageView) findViewById(R.id.I55);
        board[5][6] = (ImageView) findViewById(R.id.I56);
        board[5][7] = (ImageView) findViewById(R.id.I57);
        board[6][0] = (ImageView) findViewById(R.id.I60);
        board[6][1] = (ImageView) findViewById(R.id.I61);
        board[6][2] = (ImageView) findViewById(R.id.I62);
        board[6][3] = (ImageView) findViewById(R.id.I63);
        board[6][4] = (ImageView) findViewById(R.id.I64);
        board[6][5] = (ImageView) findViewById(R.id.I65);
        board[6][6] = (ImageView) findViewById(R.id.I66);
        board[6][7] = (ImageView) findViewById(R.id.I67);
        board[7][0] = (ImageView) findViewById(R.id.I70);
        board[7][1] = (ImageView) findViewById(R.id.I71);
        board[7][2] = (ImageView) findViewById(R.id.I72);
        board[7][3] = (ImageView) findViewById(R.id.I73);
        board[7][4] = (ImageView) findViewById(R.id.I74);
        board[7][5] = (ImageView) findViewById(R.id.I75);
        board[7][6] = (ImageView) findViewById(R.id.I76);
        board[7][7] = (ImageView) findViewById(R.id.I77);
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                board[i][j].setOnClickListener(this);
                board[i][j].setTag("n");
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                board[i][j].setOnClickListener(this);
                board[i][j].setTag("n");
            }
        }
    }

    public void start(){
        for (int i = 0; i < 3; i+=2){
            for (int j = 1; j < 8; j+=2){
                pubFun.figure(false,board[i][j]);
                board[i][j].setTag("b");
            }
        }
        for (int j = 0; j < 7; j+=2){
            pubFun.figure(false,board[1][j]);
            board[1][j].setTag("b");
        }
        for (int i = 5; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                pubFun.figure(true,board[i][j]);
                board[i][j].setTag("w");
            }
        }
        for (int j = 1; j < 8; j+=2){
            pubFun.figure(true,board[6][j]);
            board[6][j].setTag("w");
        }
        for (int j = 0; j < 7; j+=2){
            board[3][j].setImageResource(R.drawable.blank);
            board[3][j].setTag("n");
        }
        for (int j = 1; j < 8; j+=2){
            board[4][j].setImageResource(R.drawable.blank);
            board[4][j].setTag("n");
        }
        bc.setText("12");
        wc.setText("12");
    }

    public int findImage(ImageView[][] board, ImageView iv){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if (board[i][j]==iv)
                    return i*10+j;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if (board[i][j]==iv)
                    return i*10+j;
            }
        }
        return -1;
    }

    public void whiteTurn(int i, int j){
        switch (board[i][j].getTag().toString()){
            case "w":
                switch (j){
                    case 0:
                        northEast(i,j,"w","b","wk","bk");
                        break;
                    case 7:
                        northWest(i,j,"w","b","wk","bk");
                        break;
                    default:
                        northEast(i,j,"w","b","wk","bk");
                        northWest(i,j,"w","b","wk","bk");
                        break;
                }
                break;
            case "wk":
                if(i==0&&j==7)
                    southWest(i,j,"w","b","wk","bk");
                else if(i==7&&j==0)
                    northEast(i,j,"w","b","wk","bk");
                else if(i==0){
                    southWest(i,j,"w","b","wk","bk");
                    southEast(i,j,"w","b","wk","bk");
                }
                else if(i==7){
                    northWest(i,j,"w","b","wk","bk");
                    northEast(i,j,"w","b","wk","bk");
                }
                else if(j==0){
                    northEast(i,j,"w","b","wk","bk");
                    southEast(i,j,"w","b","wk","bk");
                }
                else if(j==7){
                    northWest(i,j,"w","b","wk","bk");
                    southWest(i,j,"w","b","wk","bk");
                }
                else {
                    southWest(i,j,"w","b","wk","bk");
                    southEast(i,j,"w","b","wk","bk");
                    northWest(i,j,"w","b","wk","bk");
                    northEast(i,j,"w","b","wk","bk");
                }
                break;
        }
    }
    public void blackTurn(int i, int j){
        switch (board[i][j].getTag().toString()){
            case "b":
                switch (j){
                    case 0:
                        southEast(i,j,"b","w","bk","wk");
                        break;
                    case 7:
                        southWest(i,j,"b","w","bk","wk");
                        break;
                    default:
                        southEast(i,j,"b","w","bk","wk");
                        southWest(i,j,"b","w","bk","wk");
                        break;
                }
                break;
            case "bk":
                if(i==0&&j==7)
                    southWest(i,j,"b","w","bk","wk");
                else if(i==7&&j==0)
                    northEast(i,j,"b","w","bk","wk");
                else if(i==0){
                    southWest(i,j,"b","w","bk","wk");
                    southEast(i,j,"b","w","bk","wk");
                }
                else if(i==7){
                    northWest(i,j,"b","w","bk","wk");
                    northEast(i,j,"b","w","bk","wk");
                }
                else if(j==0){
                    northEast(i,j,"b","w","bk","wk");
                    southEast(i,j,"b","w","bk","wk");
                }
                else if(j==7){
                    northWest(i,j,"b","w","bk","wk");
                    southWest(i,j,"b","w","bk","wk");
                }
                else {
                    southWest(i,j,"b","w","bk","wk");
                    southEast(i,j,"b","w","bk","wk");
                    northWest(i,j,"b","w","bk","wk");
                    northEast(i,j,"b","w","bk","wk");
                }
                break;
        }
    }

    public boolean northWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j-1].getTag()==a||board[i-1][j-1].getTag()==ak)){
            if(!((board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk)&&(i-2<0||j-2<0))){
                if(!((board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk)&&(board[i-2][j-2].getTag()==b||board[i-2][j-2].getTag()==bk||board[i-2][j-2].getTag()==a||board[i-2][j-2].getTag()==ak))){
                    if(board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk){
                        board[i-2][j-2].setBackgroundColor(0xffcc7a00);
                        board[i-2][j-2].setTag("c");
                    }
                    else {
                        board[i-1][j-1].setBackgroundColor(0xffcc7a00);
                        board[i-1][j-1].setTag("c");
                    }
                    isClick = board[i][j];
                    isClick.setBackgroundColor(0xffcc7a00);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean northEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j+1].getTag().toString()==a||board[i-1][j+1].getTag().toString()==ak)){
            if(!((board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk)&&(i-2<0||j+2>7))){
                if(!((board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk)&&(board[i-2][j+2].getTag().toString()==b||board[i-2][j+2].getTag().toString()==bk||board[i-2][j+2].getTag().toString()==a||board[i-2][j+2].getTag().toString()==ak))){
                    if(board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk){
                        board[i-2][j+2].setBackgroundColor(0xffcc7a00);
                        board[i-2][j+2].setTag("c");
                    }
                    else {
                        board[i-1][j+1].setBackgroundColor(0xffcc7a00);
                        board[i-1][j+1].setTag("c");
                    }
                    isClick = board[i][j];
                    isClick.setBackgroundColor(0xffcc7a00);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean southWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j-1].getTag()==a||board[i+1][j-1].getTag()==ak)){
            if(!((board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk)&&(i+2>7||j-2<0))){
                if(!((board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk)&&(board[i+2][j-2].getTag()==b||board[i+2][j-2].getTag()==bk||board[i+2][j-2].getTag()==a||board[i+2][j-2].getTag()==ak))){
                    if(board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk){
                        board[i+2][j-2].setBackgroundColor(0xffcc7a00);
                        board[i+2][j-2].setTag("c");
                    }
                    else {
                        board[i+1][j-1].setBackgroundColor(0xffcc7a00);
                        board[i+1][j-1].setTag("c");
                    }
                    isClick = board[i][j];
                    isClick.setBackgroundColor(0xffcc7a00);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean southEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j+1].getTag()==a||board[i+1][j+1].getTag()==ak)){
            if(!((board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk)&&(i+2>7||j+2>7))){
                if(!((board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk)&&(board[i+2][j+2].getTag()==b||board[i+2][j+2].getTag()==bk||board[i+2][j+2].getTag()==a||board[i+2][j+2].getTag()==ak))){
                    if(board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk){
                        board[i+2][j+2].setBackgroundColor(0xffcc7a00);
                        board[i+2][j+2].setTag("c");
                    }
                    else {
                        board[i+1][j+1].setBackgroundColor(0xffcc7a00);
                        board[i+1][j+1].setTag("c");
                    }
                    isClick = board[i][j];
                    isClick.setBackgroundColor(0xffcc7a00);
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public void move(int i, int j){
        isClick.setBackgroundColor(0xff000000);
        isClick.setImageResource(R.drawable.blank);
        board[i][j].setTag(isClick.getTag());
        switch (isClick.getTag().toString()) {
            case "b":
                pubFun.figure(false,board[i][j]);
                break;
            case "bk":
                board[i][j].setImageResource(R.drawable.blackking);
                break;
            case "w":
                pubFun.figure(true,board[i][j]);
                break;
            case "wk":
                board[i][j].setImageResource(R.drawable.whiteking);
                break;
        }
        isClick.setTag("n");
        int ic = findImage(board, isClick);
        int jc = ic % 10;
        ic /= 10;
        boolean isEaten = false;
        if (Math.abs(ic - i) == 2) {
            int imax = Math.max(ic, i);
            int jmax = Math.max(jc, j);
            board[imax - 1][jmax - 1].setImageResource(R.drawable.blank);
            switch (board[imax - 1][jmax - 1].getTag().toString()) {
                case "b":
                case "bk":
                    reduceCounter(bc);
                    break;
                case "w":
                case "wk":
                    reduceCounter(wc);
                    break;
            }
            board[imax - 1][jmax - 1].setTag("n");
            isEaten = true;
        }
        board[i][j].setBackgroundColor(0xff000000);
        isClick = null;
        cleanCheck();
        if(("wk"==board[i][j].getTag().toString()||"bk"==board[i][j].getTag().toString())&&!isEaten)
            counter++;
        else counter=0;
        if (turn&&i == 0&&board[i][j].getTag().toString()!="wk") {
            board[i][j].setTag("wk");
            board[i][j].setImageResource(R.drawable.whiteking);
        }
        if (!turn&&i == 7&&board[i][j].getTag().toString()!="bk") {
            board[i][j].setTag("bk");
            board[i][j].setImageResource(R.drawable.blackking);
        }
        if(!isWin()) {
            cleanCheck();
            isClick = null;
            isChainActivated = false;
            haveToEat = false;
            if (turn) {
                if (!isEaten) {
                    turn = false;
                    if (mustEatBlack()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                    }
                    if(mode.equals("single"))
                        computer();
                } else if (!isChain(i, j, "w", "b", "wk", "bk")) {
                    turn = false;
                    if (mustEatBlack()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                    }
                    if(mode.equals("single"))
                        computer();
                } else isChainActivated = true;
            } else {
                if (!isEaten) {
                    turn = true;
                    if (mustEatWhite()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                    }
                } else if (!isChain(i, j, "b", "w", "bk", "wk")) {
                    turn = true;
                    if (mustEatWhite()) {
                        cleanCheck();
                        haveToEat = true;
                        isClick = null;
                    }
                } else isChainActivated = true;
            }
        }
        else {
            cleanCheck();
            isClick = null;
            isChainActivated=false;
        }
    }

    public void reduceCounter(TextView tv){
        int x = Integer.parseInt(tv.getText().toString());
        x--;
        if(x>=10)
            tv.setText(""+x);
        else tv.setText("0"+x);
    }

    public void cleanCheck(){
        for (int a = 0; a < 7; a+=2){
            for (int b = 1; b < 8; b+=2){
                if(board[a][b].getTag().toString()=="c"){
                    board[a][b].setTag("n");
                }
                board[a][b].setBackgroundColor(0xff000000);
            }
        }
        for (int a = 1; a < 8; a+=2){
            for (int b = 0; b < 7; b+=2){
                if(board[a][b].getTag().toString()=="c"){
                    board[a][b].setBackgroundColor(0xff000000);
                    board[a][b].setTag("n");
                }
                else board[a][b].setBackgroundColor(0xff000000);
            }
        }
    }

    public boolean isChain(int i, int j, String a, String b, String ak, String bk){
        if(i==0&&j==7)
            return eatSouthWest(i, j, a, b, ak, bk);
        else if(i==7&&j==0)
            return  eatNorthEast(i, j, a, b, ak, bk);
        else if(i==0){
            boolean h= eatSouthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            return h||w;
        }
        else if(i==7){
            boolean h =eatNorthWest(i, j, a, b, ak, bk);
            boolean w=eatNorthEast(i, j, a, b, ak, bk);
            return h||w;

        }
        else if(j==0){
            boolean h=eatNorthEast(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            return h||w;
        }
        else if(j==7){
            boolean h=eatNorthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthWest(i, j, a, b, ak, bk);
            return h||w;
        }
        else {
            boolean h=eatSouthWest(i, j, a, b, ak, bk);
            boolean w=eatSouthEast(i, j, a, b, ak, bk);
            boolean q=eatNorthWest(i, j, a, b, ak, bk);
            boolean r=eatNorthEast(i, j, a, b, ak, bk);
            return h||w||q||r;
        }
    }

    public boolean eatNorthWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j-1].getTag()==a||board[i-1][j-1].getTag()==ak)){
            if(!((board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk)&&(i-2<0||j-2<0))){
                if(!((board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk)&&(board[i-2][j-2].getTag()==b||board[i-2][j-2].getTag()==bk||board[i-2][j-2].getTag()==a||board[i-2][j-2].getTag()==ak))){
                    if(board[i-1][j-1].getTag()==b||board[i-1][j-1].getTag()==bk){
                        board[i-2][j-2].setBackgroundColor(0xffcc7a00);
                        board[i-2][j-2].setTag("c");
                        isClick = board[i][j];
                        isClick.setBackgroundColor(0xffcc7a00);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatNorthEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i-1][j+1].getTag().toString()==a||board[i-1][j+1].getTag().toString()==ak)){
            if(!((board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk)&&(i-2<0||j+2>7))){
                if(!((board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk)&&(board[i-2][j+2].getTag().toString()==b||board[i-2][j+2].getTag().toString()==bk||board[i-2][j+2].getTag().toString()==a||board[i-2][j+2].getTag().toString()==ak))){
                    if(board[i-1][j+1].getTag().toString()==b||board[i-1][j+1].getTag().toString()==bk){
                        board[i-2][j+2].setBackgroundColor(0xffcc7a00);
                        board[i-2][j+2].setTag("c");
                        isClick = board[i][j];
                        isClick.setBackgroundColor(0xffcc7a00);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatSouthWest(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j-1].getTag()==a||board[i+1][j-1].getTag()==ak)){
            if(!((board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk)&&(i+2>7||j-2<0))){
                if(!((board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk)&&(board[i+2][j-2].getTag()==b||board[i+2][j-2].getTag()==bk||board[i+2][j-2].getTag()==a||board[i+2][j-2].getTag()==ak))){
                    if(board[i+1][j-1].getTag()==b||board[i+1][j-1].getTag()==bk){
                        board[i+2][j-2].setBackgroundColor(0xffcc7a00);
                        board[i+2][j-2].setTag("c");
                        isClick = board[i][j];
                        isClick.setBackgroundColor(0xffcc7a00);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean eatSouthEast(int i, int j, String a, String b, String ak, String bk){
        if(!(board[i+1][j+1].getTag()==a||board[i+1][j+1].getTag()==ak)){
            if(!((board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk)&&(i+2>7||j+2>7))){
                if(!((board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk)&&(board[i+2][j+2].getTag()==b||board[i+2][j+2].getTag()==bk||board[i+2][j+2].getTag()==a||board[i+2][j+2].getTag()==ak))){
                    if(board[i+1][j+1].getTag()==b||board[i+1][j+1].getTag()==bk){
                        board[i+2][j+2].setBackgroundColor(0xffcc7a00);
                        board[i+2][j+2].setTag("c");
                        isClick = board[i][j];
                        isClick.setBackgroundColor(0xffcc7a00);
                        return true;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean mustEatWhite(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if (whiteEat(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if (whiteEat(i,j))
                    return true;
            }
        }
        return false;
    }
    public boolean mustEatBlack(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if(blackEat(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if(blackEat(i,j))
                    return true;
            }
        }
        return false;
    }

    public boolean whiteEat(int i ,int j){
        switch (board[i][j].getTag().toString()){
            case "w":
                switch (j){
                    case 0:
                        if(eatNorthEast(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    case 7:
                        if(eatNorthWest(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    default:
                        boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                        boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                        return h||w;
                }
                break;
            case "wk":
                if(i==0&&j==7) {
                    if (eatSouthWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else {if(i==7&&j==0) {
                    if (eatNorthEast(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else if (i == 0) {
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    boolean w = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else if (i == 7) {
                    boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    return h||w;
                } else if (j == 0) {
                    boolean h =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else if (j == 7) {
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    return h||w;
                } else {
                    boolean w =eatNorthWest(i,j,"w","b","wk","bk");
                    boolean h =eatSouthWest(i, j, "w", "b", "wk", "bk");
                    boolean h1 =eatNorthEast(i,j,"w","b","wk","bk");
                    boolean w1 = eatSouthEast(i, j, "w", "b", "wk", "bk");
                    return h||w||h1||w1;
                }
                }
                break;
        }
        return false;
    }
    public boolean blackEat(int i ,int j){
        switch (board[i][j].getTag().toString()){
            case "b":
                switch (j){
                    case 0:
                        if(eatSouthEast(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    case 7:
                        if(eatSouthWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    default:
                        boolean h =eatSouthEast(i,j,"b","w","bk","wk");
                        boolean w =eatSouthWest(i,j,"b","w","bk","wk");
                        return h||w;
                }
                break;
            case "bk":
                if(i==0&&j==7) {
                    if (eatSouthWest(i, j, "b","w","bk","wk"))
                        return true;
                }
                else {if(i==7&&j==0) {
                    if (eatNorthEast(i, j, "b","w","bk","wk"))
                        return true;
                }
                else if (i == 0) {
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    boolean w = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w;
                } else if (i == 7) {
                    boolean h =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    return h||w;
                } else if (j == 0) {
                    boolean h =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w;
                } else if (j == 7) {
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    return h||w;
                } else {
                    boolean w =eatNorthWest(i,j,"b","w","bk","wk");
                    boolean h =eatSouthWest(i, j, "b","w","bk","wk");
                    boolean h1 =eatNorthEast(i,j,"b","w","bk","wk");
                    boolean w1 = eatSouthEast(i, j, "b","w","bk","wk");
                    return h||w||h1||w1;
                }
                }
                break;
        }
        return false;
    }

    public boolean isWin(){
        int x = Integer.parseInt(wc.getText().toString());
        if(!turn){
            if((x==0)||!canMoveWhite())
            {
                winAlert("The Zombies Win!");
                return true;
            }
        }
        else { x = Integer.parseInt(bc.getText().toString());
            if((x==0)||!canMoveBlack())
            {
                if(mode.equals("single"))
                    pubFun.addCoins(100);
                winAlert("The Pirates Win!");
                return true;
            }
        }
        if(counter==30) {
            winAlert("It's a Tie!");
            return true;
        }
        return false;
    }
    public void winAlert(String s){
        clickable();
        AlertDialog.Builder tie = new AlertDialog.Builder(Checkers.this);
        tie.setCancelable(true);
        tie.setTitle(s);
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
                cleanWhite();
                start();
                haveToEat=false;
                counter=0;
                isChainActivated=false;
                isClick=null;
                turn = true;
            }
        });
        tie.show();
    }

    public boolean canMoveWhite(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if (whiteMove(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if (whiteMove(i,j))
                    return true;
            }
        }
        return false;
    }
    public boolean canMoveBlack(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                if(blackMove(i,j))
                    return true;
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                if(blackMove(i,j))
                    return true;
            }
        }
        return false;
    }

    public boolean whiteMove(int i ,int j){
        switch (board[i][j].getTag().toString()){
            case "w":
                switch (j){
                    case 0:
                        if(northEast(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    case 7:
                        if(northWest(i,j,"w","b","wk","bk"))
                            return true;
                        break;
                    default:
                        if(northEast(i,j,"w","b","wk","bk")||eatNorthWest(i,j,"w","b","wk","bk")){
                            return true;
                        }
                        break;
                }
                break;
            case "wk":
                if(i==0&&j==7) {
                    if (southWest(i, j, "w", "b", "wk", "bk"))
                        return true;
                }
                else {
                    if (i == 7 && j == 0) {
                        if (northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    }
                    else if (i == 0) {
                        if (southWest(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (i == 7) {
                        if (northWest(i, j, "w", "b", "wk", "bk") || northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (j == 0) {
                        if (northEast(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else if (j == 7) {
                        if (northWest(i, j, "w", "b", "wk", "bk") || southWest(i, j, "w", "b", "wk", "bk"))
                            return true;
                    } else {
                        if (southWest(i, j, "w", "b", "wk", "bk") || southEast(i, j, "w", "b", "wk", "bk") || northWest(i, j, "w", "b", "wk", "bk") || northEast(i, j, "w", "b", "wk", "bk"))
                            return true;
                    }
                }
                break;
        }
        return false;
    }
    public boolean blackMove(int i,int j){
        switch (board[i][j].getTag().toString()){
            case "b":
                switch (j){
                    case 0:
                        if(southEast(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    case 7:
                        if(southWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                    default:
                        if(southEast(i,j,"b","w","bk","wk")||southWest(i,j,"b","w","bk","wk"))
                            return true;
                        break;
                }
                break;
            case "bk":
                if(i==0&&j==7) {
                    if (southWest(i, j, "b", "w", "bk", "wk"))
                        return true;
                }
                else {
                    if (i == 7 && j == 0) {
                        if (northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    }
                    else if (i == 0) {
                        if (southWest(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (i == 7) {
                        if (northWest(i, j, "b", "w", "bk", "wk") || northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (j == 0) {
                        if (northEast(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else if (j == 7) {
                        if (northWest(i, j, "b", "w", "bk", "wk") || southWest(i, j, "b", "w", "bk", "wk"))
                            return true;
                    } else {
                        if (southWest(i, j, "b", "w", "bk", "wk") || southEast(i, j, "b", "w", "bk", "wk") || northWest(i, j, "b", "w", "bk", "wk") || northEast(i, j, "b", "w", "bk", "wk"))
                            return true;
                    }
                }
                break;
        }
        return false;
    }

    public void clickable(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                board[i][j].setTag("n");
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                board[i][j].setTag("n");
            }
        }
    }
    public void TheRealClickable(boolean b){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                board[i][j].setClickable(b);
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                board[i][j].setClickable(b);
            }
        }
    }

    public void cleanWhite(){
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                board[i][j].setImageResource(R.drawable.blank);
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                board[i][j].setImageResource(R.drawable.blank);
            }
        }
    }

    public void computer(){
        String[][] currentBoard = new String[8][8];
        ArrayList<Integer> black = new ArrayList<>();
        ArrayList<Integer> white = new ArrayList<>();
        for (int i = 0; i < 7; i+=2){
            for (int j = 1; j < 8; j+=2){
                switch (board[i][j].getTag().toString()){
                    case "b":
                        black.add(i*10+j);
                        break;
                    case "bk":
                        black.add(i*10+j);
                        break;
                    case "w":
                        white.add(i*10+j);
                        break;
                    case "wk":
                        white.add(i*10+j);
                        break;
                }
                currentBoard[i][j] = board[i][j].getTag().toString();
                board[i][j].setClickable(false);
            }
        }
        for (int i = 1; i < 8; i+=2){
            for (int j = 0; j < 7; j+=2){
                switch (board[i][j].getTag().toString()){
                    case "b":
                        black.add(i*10+j);
                        break;
                    case "bk":
                        black.add(i*10+j);
                        break;
                    case "w":
                        white.add(i*10+j);
                        break;
                    case "wk":
                        white.add(i*10+j);;
                        break;
                }
                currentBoard[i][j] = board[i][j].getTag().toString();
                board[i][j].setClickable(false);
            }
        }
        int x = Integer.parseInt(wc.getText().toString());
        int xx = Integer.parseInt(bc.getText().toString());
        TreeNode<computerFunctions> t = new TreeNode(new computerFunctions(currentBoard,turn,haveToEat,x,xx,counter,white,black,0,3));
        buildTree(t,1);
        maxmin(t);
        TreeNode<computerFunctions> mt = theChosenOne(t);
        int z =mt.getBoard().getIc();
        int i = black.get(z)/10;
        int j = black.get(z)%10;
        TheThread tt = new TheThread(i,j,mt.getBoard().getC());
        tt.start();
    }
    public void theSecondComputer(int c, int i, int j){
        switch (c){
            case 1:
                if(board[i-1][j+1].getTag()=="w"||board[i-1][j+1].getTag()=="wk"){
                    onClick(board[i-2][j+2]);
                }
                else onClick(board[i-1][j+1]);
                break;
            case 2:
                if(board[i-1][j-1].getTag()=="w"||board[i-1][j-1].getTag()=="wk"){
                    onClick(board[i-2][j-2]);
                }
                else onClick(board[i-1][j-1]);
                break;
            case 3:
                if(board[i+1][j-1].getTag()=="w"||board[i+1][j-1].getTag()=="wk"){
                    onClick(board[i+2][j-2]);
                }
                else onClick(board[i+1][j-1]);
                break;
            case 4:
                if(board[i+1][j+1].getTag()=="w"||board[i+1][j+1].getTag()=="wk"){
                    onClick(board[i+2][j+2]);
                }
                else onClick(board[i+1][j+1]);
                break;
        }
        TheChainThread tct = new TheChainThread();
        tct.start();
    }

    public void buildTree(TreeNode<computerFunctions> t, int levelCounter){
        if(levelCounter<5&&!t.getBoard().isOver)
        {
            if(t.getBoard().isTurn()){
                for(int i =0;i<t.getBoard().getWhite().size();i++){
                    switch (t.getBoard().getBoard()[t.getBoard().getWhite().get(i)/10][t.getBoard().getWhite().get(i)%10]){
                        case "w":
                            for(int j =1;j<3;j++){
                                betweenTheLoops(t,levelCounter,i,j);
                            }
                            break;
                        case "wk":
                            for(int j =1;j<5;j++){
                                betweenTheLoops(t,levelCounter,i,j);
                            }
                            break;
                    }
                }
            }
            else{
                for(int i =0;i<t.getBoard().getBlack().size();i++){
                    switch (t.getBoard().getBoard()[t.getBoard().getBlack().get(i)/10][t.getBoard().getBlack().get(i)%10]){
                        case "b":
                            for(int j =3;j<5;j++){
                                betweenTheLoops(t,levelCounter,i,j);
                            }
                            break;
                        case "bk":
                            for(int j =1;j<5;j++){
                                betweenTheLoops(t,levelCounter,i,j);
                            }
                            break;
                    }
                }
            }
        }
    }
    public void betweenTheLoops(TreeNode<computerFunctions> t, int levelCounter,int i, int j){
        TreeNode<computerFunctions> temp = new TreeNode(new computerFunctions(t.getBoard().getBoard(),t.getBoard().isTurn(),t.getBoard().isHaveToEat(),t.getBoard().getWc(),t.getBoard().getBc(),t.getBoard().getCounter(),t.getBoard().getWhite(),t.getBoard().getBlack(),i,j));
        if(temp.getBoard().canMove()){
            temp.getBoard().move();
            while(temp.getBoard().isChainActivated()){
                temp.getBoard().move();
            }
            t.getSons().add(temp);
            int x = levelCounter+1;
            buildTree(temp,x);
        }
    }

    public void maxmin(TreeNode<computerFunctions> t){
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
        else{
            t.getBoard().difference = t.getBoard().getBc()-t.getBoard().getWc();
        }
    }

    public TreeNode<computerFunctions> theChosenOne(TreeNode<computerFunctions> t){
        ArrayList<TreeNode<computerFunctions>> equals = new ArrayList<>();
        for (int i = 0; i < t.getSons().size(); i++) {
            if(t.getBoard().difference==t.getSons().get(i).getBoard().difference)
                equals.add(t.getSons().get(i));
        }
        Random r = new Random();
        int num = r.nextInt(equals.size());
        return equals.get(num);
    }

    public class TheThread extends Thread{
        private int i;
        private int j;
        private int c;

        public TheThread(int i, int j, int c) {
            this.i = i;
            this.j = j;
            this.c = c;
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.arg1 = i*10+j;
            msg.what = 0;
            handler.sendMessage(msg);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message msg1 = new Message();
            msg1.arg1 = i*10+j;
            msg1.arg2 = c;
            msg1.what = 1;
            handler.sendMessage(msg1);
        }
    }

    public class TheChainThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(isChainActivated){
                int q=-1;
                int w=-1;
                for (int a = 0; a < 7; a+=2){
                    for (int b = 1; b < 8; b+=2){
                        if(board[a][b].getTag()=="c"){
                            q=a;
                            w=b;
                        }
                    }
                }
                if(q==-1&&w==-1){
                    for (int a = 1; a < 8; a+=2){
                        for (int b = 0; b < 7; b+=2){
                            if(board[a][b].getTag()=="c"){
                                q=a;
                                w=b;
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.arg1 = q * 10 + w;
                msg.what = 0;
                handler.sendMessage(msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message msg1 = new Message();
            msg1.what=2;
            handler.sendMessage(msg1);
        }
    }
}
