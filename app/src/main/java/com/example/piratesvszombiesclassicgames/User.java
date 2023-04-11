package com.example.piratesvszombiesclassicgames;


import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;


@IgnoreExtraProperties
public class User {

    public String key;
    public String uid;
    public String user;
    public String bdDate;
    public String email;
    public String pass;
    public int coins;
    public int currentPirate;
    public int currentZombie;
    public ArrayList<Record> records;
    public ArrayList<Integer> zombieFigures;
    public ArrayList<Integer> pirateFigures;

    public User() {
    }

    public User(String key, String uid, String user, String bdDate, String email, String pass) {
        this.key = key;
        this.uid = uid;
        this.user = user;
        this.bdDate = bdDate;
        this.email = email;
        this.pass = pass;
        this.coins = 0;
        currentPirate =0;
        currentZombie=0;
        records = new ArrayList<>();
        records.add(new Record("null",0));
        zombieFigures = new ArrayList<>();
        zombieFigures.add(0);
        pirateFigures = new ArrayList<>();
        pirateFigures.add(0);
    }

}
