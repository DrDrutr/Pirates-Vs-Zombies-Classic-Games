package com.example.piratesvszombiesclassicgames;

import android.widget.ImageView;

import java.util.ArrayList;

public class Row {
    private int counter;
    private int bulls;
    private int cows;
    private int[] iv;

    public Row(int counter, int bulls, int cows, int[] iv) {
        this.counter = counter;
        this.bulls = bulls;
        this.cows = cows;
        this.iv = new int[iv.length];
        for (int i = 0; i < iv.length; i++) {
            this.iv[i] = iv[i];
        }
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getBulls() {
        return bulls;
    }

    public void setBulls(int bulls) {
        this.bulls = bulls;
    }

    public int getCows() {
        return cows;
    }

    public void setCows(int cows) {
        this.cows = cows;
    }

    public int[] getIv() {
        return iv;
    }

    public void setIv(int[] iv) {
        this.iv = iv;
    }
}
