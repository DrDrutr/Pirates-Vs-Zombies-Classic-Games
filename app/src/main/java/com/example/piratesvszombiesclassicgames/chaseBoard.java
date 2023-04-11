package com.example.piratesvszombiesclassicgames;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class chaseBoard extends View {

    Context context;
    int counter=0;
    Bitmap back;
    Handler handler;
    GameThread gameThread;
    rndTime rndTime;
    speed speed;
    static final long FPS=20;
    static int size;
    int xz;
    int yz;
    int angle =0;
    int Rsize =0;
    int angle2=0;
    int y;
    int vy=-50;
    int num = 0;
    boolean isJump = false;
    Random rnd;
    ArrayList<RollingZombie> zombies;
    boolean isPaused;
    Handler h;
    int v=30;

    public chaseBoard(Context context,Handler h) {
        super(context);
        this.context = context;
        back = BitmapFactory.decodeResource(getResources(),R.drawable.backgroundchase);
        gameThread = new GameThread();
        gameThread.start();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                if(!isPaused) {
                    if (message.what == 0)
                        invalidate();
                    else
                        zombiada();
                }
                return true;
            }
        });
        size =pubFun.dpToPx(70,context);
        rnd = new Random();
        zombies = new ArrayList<>();
        rndTime = new rndTime();
        rndTime.start();
        isPaused=false;
        this.h=h;
        speed = new speed();
        speed.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(num==0){
            y=2*getHeight()/3-size;
            xz = getWidth();
            yz=2*getHeight()/3-(size/2);
        }
        num =1;
        jump();

        drawBackground(canvas,back,-counter,0,getWidth(),getHeight());
        drawBackground(canvas,back,-counter+getWidth(),0,getWidth(),getHeight());
        counter%=getWidth();
        counter+=10;

        Bitmap pirate = figure(true);
        pirate = Bitmap.createScaledBitmap(pirate,size,size,true);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        pirate = Bitmap.createBitmap(pirate,0,0,pirate.getWidth(),pirate.getHeight(),matrix,true);
        canvas.drawBitmap(pirate,20-Rsize,y-Rsize,null);
        angle%=360;
        angle+=10;
        double angle2 = Math.toRadians(angle);
        Rsize= (int)((Math.abs(Math.sin(angle2)*Math.cos(angle2)))*(Math.sqrt(2)-1)*size);

        for (int i = 0; i < zombies.size(); i++) {
            zombies.get(i).spinning(360-angle,canvas,Rsize/2);
            if(isCollision(zombies.get(i))) {
                isPaused=true;
                h.sendEmptyMessage(0);
            }
            if(zombies.get(i).moving(v))
                zombies.remove(i);
        }
    }

    public void zombiada(){
        RollingZombie temp = new RollingZombie(xz,yz,figure(false),this,size/2);
        zombies.add(temp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pubFun.clickSound(context);
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!isJump) {
                vy=-pubFun.dpToPx(40,context);
                isJump = true;
                jump();
            }
        }
        return true;
    }

    public void jump(){
        if(isJump){
            y+=vy;
            vy+=pubFun.dpToPx(7,context);
        }
        if(y>=2*getHeight()/3-size){
            isJump=false;
            vy=-pubFun.dpToPx(40,context);
            y=2*getHeight()/3-size;
        }
    }

    public boolean isCollision(RollingZombie rz){
        if(20-Rsize<(rz.getX()-Rsize/2)&&20+size-Rsize>(rz.getX()-Rsize/2)&&(y-Rsize)<(rz.getY()-Rsize/2)&&(y-Rsize)+size>(rz.getY()-Rsize/2))
            return true;
        return false;
    }

    public Bitmap figure(boolean b){
        if(b) {
            switch (pubFun.user.currentPirate) {
                case 0:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.white);
                case 1:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.white1);
                case 2:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.white2);
                case 3:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.white3);
                case 4:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.white4);
            }
        }
        else{
            switch (pubFun.user.currentZombie) {
                case 0:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.black);
                case 1:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.black1);
                case 2:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.black2);
                case 3:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.black3);
                case 4:
                    return BitmapFactory.decodeResource(getResources(), R.drawable.black4);
            }
        }
        return null;
    }

    public static synchronized void drawBackground(Canvas canvas, Bitmap bitmap, int x, int y, int width, int height){
        Rect source = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        Rect target = new Rect(x,y,x+width,y+height);
        canvas.drawBitmap(bitmap,source,target,null);
    }

    public class GameThread extends Thread{
        long stepersecond = 1000/FPS;
        long starttime;
        long sleeptime;
        @Override
        public void run() {
            super.run();
            while (true){
                starttime = System.currentTimeMillis();
                sleeptime = stepersecond-(System.currentTimeMillis()-starttime);
                if(!(sleeptime>0))
                    sleeptime=10;
                try {
                    Thread.sleep(sleeptime);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class rndTime extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                int num=1;
                switch (v){
                    case 30:
                    case 40:
                        num = rnd.nextInt(3)+2;
                        break;
                    case 50:
                    case 60:
                        num = rnd.nextInt(2)+2;
                        break;
                    case 70:
                    case 80:
                        num = rnd.nextInt(2)+1;
                        break;
                        default:
                            num = 1;
                }
                try {
                    Thread.sleep(num*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        }
    }

    public class speed extends Thread{
        @Override
        public void run() {
            super.run();
            while(true){
                int num=0;
                while(num<30) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(!isPaused)
                        num++;
                }
                v+=10;
            }
        }
    }
}
