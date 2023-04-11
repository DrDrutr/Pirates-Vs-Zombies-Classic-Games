package com.example.piratesvszombiesclassicgames;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class RollingZombie {

    int x;
    int y;
    Bitmap bitmap;
    chaseBoard chaseBoard;
    int size;


    public RollingZombie(int x, int y, Bitmap bitmap, com.example.piratesvszombiesclassicgames.chaseBoard chaseBoard,int size) {
        this.x = x;
        this.y=y;
        this.bitmap = bitmap;
        this.chaseBoard = chaseBoard;
        this.size = size;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public com.example.piratesvszombiesclassicgames.chaseBoard getChaseBoard() {
        return chaseBoard;
    }

    public void setChaseBoard(com.example.piratesvszombiesclassicgames.chaseBoard chaseBoard) {
        this.chaseBoard = chaseBoard;
    }

    public synchronized void draw(Canvas canvas){

    }

    public boolean moving(int x){
        this.x-=x;
        return this.x<(0-bitmap.getWidth());
    }

    public void spinning(int angle,Canvas canvas, int Rsize){
        Bitmap bitmap = this.bitmap;
        bitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        bitmap = Bitmap.createBitmap(bitmap,0,0,size,size,matrix,true);
        canvas.drawBitmap(bitmap,x-Rsize,y-Rsize,null);
    }
}
