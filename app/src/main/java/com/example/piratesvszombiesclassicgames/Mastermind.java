package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.media.Image;
import android.os.SystemClock;
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
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.END;

public class Mastermind extends AppCompatActivity {

    int colorsnum;
    int holes;
    int[] colors;
    LinearLayout board;
    ImageView[] vboard;
    int guesses=0;
    Typeface custom_font;
    LinearLayout colorsRow;
    int nowColor=0;
    ArrayList<Integer> answer;
    Random rnd;
    Chronometer cm;
    ImageView menu;
    insDialog instr;
    ArrayList<Row> rowList;
    RowAdapter ra;
    ListView lv;
    TextView ok;
    TextView tvCounter;
    int[] guess;
    User user;
    String level;
    MenuPlus mp;
    SettingsDialog sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mastermind);
        getSupportActionBar().hide();
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");
        rnd = new Random();

        user = pubFun.user;
        menu = (ImageView) findViewById(R.id.menu);
        sd = new SettingsDialog(this);
        instr = new insDialog(this);
        setInsText();
        mp = new MenuPlus(this,menu,instr,sd);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.show();
                pubFun.clickSound(getBaseContext());
            }
        });


        Bundle bundle = getIntent().getExtras();
        level = bundle.getString("level");
        colorsnum = bundle.getInt("colors");
        holes = bundle.getInt("holes");

        colors = new int[10];
        colors[0] = Color.RED;
        colors[1] = Color.BLUE;
        colors[2] = Color.YELLOW;
        colors[3] = 0xffFF8C00; //orange
        colors[4] = Color.GREEN;
        colors[5] = Color.MAGENTA;
        colors[6] = Color.CYAN;
        colors[7] = 0xff800000; //bordo
        colors[8] = 0xffCFA509;
        colors[9] = 0xff999966;
        board = (LinearLayout) findViewById(R.id.board);
        colorsRow = (LinearLayout) findViewById(R.id.colors);
        int x = pubFun.dpToPx(50, this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(x, x);
        lp.setMargins(0,0,x/5,0);
        for (int i = 0; i < colorsnum; i++) {
            final int h = i;
            ImageView b = new ImageView(this);
            b.setLayoutParams(lp);
            b.setImageResource(R.drawable.roundbutton);
            b.setColorFilter(colors[i]);
            b.setTag(""+i);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nowColor=h;;
                }
            });
            colorsRow.addView(b);
        }
        vboard = new ImageView[holes];
        for (int i = 0; i < holes; i++) {
            final int h = i;
            ImageView b = new ImageView(this);
            b.setLayoutParams(new TableRow.LayoutParams(x, x));
            b.setTag("n");
            b.setBackgroundResource(R.drawable.hole);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked(h, (ImageView) view);
                }
            });
            vboard[i] = b;
            board.addView(b);
        }
        ok = (TextView) findViewById(R.id.tvOK);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
                pubFun.clickSound(getBaseContext());
            }
        });
        ok.setTypeface(custom_font);
        tvCounter = (TextView) findViewById(R.id.tvCounter);
        tvCounter.setTypeface(custom_font);
        rowList = new ArrayList<>();
        ra = new RowAdapter(this,0,0,rowList);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(ra);
        createPuzzle();
        cm = (Chronometer) findViewById(R.id.cm);
        cm.setTypeface(custom_font);
        cm.start();
        guess = new int[holes];
    }

    public void createRow(int bulls, int cows){
        Row r = new Row(guesses,bulls,cows,guess);
        ra.insert(r,0);
        ra.notifyDataSetChanged();
    }

    public void clicked(int i, ImageView iv){
        pubFun.clickSound(getBaseContext());
        iv.setImageResource(R.drawable.white);
        iv.setColorFilter(colors[nowColor]);
        iv.setTag(""+nowColor);
    }

    public void check(){
        if(isFull()) {
            for (int i = 0; i < holes; i++) {
                guess[i]=colors[Integer.parseInt(vboard[i].getTag().toString())];
            }
            guesses++;
            countering();
            int bulls = 0;
            int cows = 0;
            ArrayList<Integer> answerc = new ArrayList<>();
            for (int i = 0; i < holes; i++) {
                answerc.add(answer.get(i));
            }
            for (int i = 0; i < holes; i++) {
                if (Integer.parseInt(vboard[i].getTag().toString()) == answerc.get(i)) {
                    bulls++;
                    vboard[i].setTag("100");
                    answerc.set(i, -1);
                }
            }
            for (int i = 0; i < holes; i++) {
                for (int j = 0; j < holes; j++) {
                    if (Integer.parseInt(vboard[i].getTag().toString()) == answerc.get(j)) {
                        cows++;
                        vboard[i].setTag("100");
                        answerc.set(j, -1);
                    }
                }
            }
            if (bulls == holes) {
                winner(bulls,cows);
            } else if (guesses == 12) {
                islose(bulls,cows);
            } else {
                createRow(bulls,cows);
                clear();
            }
        }
    }

    public void createPuzzle(){
        answer = new ArrayList<>();
        for (int i = 0; i < holes; i++) {
            int num = rnd.nextInt(colorsnum);
            answer.add(num);
        }
    }

    public void winner(int b, int c){
        cm.stop();
        createRow(b,c);
        clear();
        falseCancle();
        tvCounter.setText("");
        switch (level){
            case "easy":
                pubFun.addCoins(25);
                pubFun.setRecord("eaMaster",guesses);
                setInsText();
                break;
            case "medium":
                pubFun.addCoins(50);
                pubFun.setRecord("mMaster",guesses);
                setInsText();
                break;
            case "expert":
                pubFun.addCoins(100);
                pubFun.setRecord("exMaster",guesses);
                setInsText();
                break;
        }
        long sec = (SystemClock.elapsedRealtime()-cm.getBase())/1000;
        String time = pubFun.timeFormat(sec);
        AlertDialog.Builder builder = new AlertDialog.Builder(Mastermind.this);
        builder.setCancelable(true);
        builder.setTitle("You Win!");
        builder.setMessage("Congratulations! You have done it in "+time+" and "+guesses+" guesses!");
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
                trueCancle();
                guesses=0;
                countering();
                ra.clear();
                ra.notifyDataSetChanged();
                createPuzzle();
                cm.setBase(SystemClock.elapsedRealtime());
                cm.start();
            }
        });
        builder.show();
    }
    public void islose(int b, int c){
        cm.stop();
        createRow(b,c);
        falseCancle();
        for (int i = 0; i < holes; i++) {
            vboard[i].setColorFilter(colors[answer.get(i)]);
        }
        tvCounter.setText("");
        AlertDialog.Builder builder = new AlertDialog.Builder(Mastermind.this);
        builder.setCancelable(true);
        builder.setTitle("You Lose!");
        builder.setMessage("I am deeply sorry, but you are a failure!");
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
                guesses = 0;
                ra.clear();
                ra.notifyDataSetChanged();
                clear();
                countering();
                createPuzzle();
                cm.setBase(SystemClock.elapsedRealtime());
                cm.start();
                trueCancle();
            }
        });
        builder.show();
    }

    public boolean isFull(){
        for (int i = 0; i < holes; i++) {
            if(vboard[i].getTag().toString()=="n")
                return false;
        }
        return true;
    }

    public void clear(){
        for (int i = 0; i < holes; i++) {
            vboard[i].setImageResource(R.drawable.blank);
            vboard[i].setTag("n");
        }
    }
    public void countering(){
        if(guesses<9)
            tvCounter.setText("0"+(guesses+1));
        else tvCounter.setText(""+(guesses+1));
    }
    public void falseCancle(){
        for (int k = 0; k < holes; k++) {
            vboard[k].setClickable(false);
        }
    }
    public void trueCancle(){
        for (int k = 0; k < holes; k++) {
            vboard[k].setClickable(true);
        }
    }

    public void setInsText(){
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

}
