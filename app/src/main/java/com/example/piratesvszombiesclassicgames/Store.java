package com.example.piratesvszombiesclassicgames;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Store extends AppCompatActivity {

    ProductAdapter paP;
    ListView lvP;
    ArrayList<Product> productListP;
    ProductAdapter paZ;
    ListView lvZ;
    ArrayList<Product> productListZ;
    ProgressBar pb;
    Product lastSelected;
    TextView cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        cc = (TextView) findViewById(R.id.coinscounter);
        cc.setText(pubFun.user.coins+" Coins!");
        cc.setTypeface(custom_font);
        productListP = new ArrayList();
        int price = 250;
        for (int i = 0; i < 5; i++) {
            productListP.add(new Product(price,isBoughtPirate(i),isCurrentPirate(i)));
            price*=2;
        }
        paP = new ProductAdapter(this,0,0,productListP,true);
        lvP = (ListView) findViewById(R.id.lv1);
        lvP.setAdapter(paP);
        lvP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pubFun.clickSound(getBaseContext());
                lastSelected = paP.getItem(i);
                if(!lastSelected.isBought) {
                    if(pubFun.user.coins<lastSelected.price)
                        moneyAlet();
                    else {
                        pubFun.user.coins -= lastSelected.price;
                        cc.setText(pubFun.user.coins+" Coins!");
                        pubFun.user.pirateFigures.add(i);
                        lastSelected.isBought = true;
                        cleanCurrentPirate();
                        lastSelected.isCurrent = true;
                        paP.notifyDataSetChanged();
                        pb.setProgress(pubFun.user.pirateFigures.size() + pubFun.user.zombieFigures.size());
                        pubFun.user.currentPirate = i;
                        pubFun.dref.setValue(pubFun.user);
                    }
                }
                else {
                    cleanCurrentPirate();
                    lastSelected.isCurrent = true;
                    paP.notifyDataSetChanged();
                    pubFun.user.currentPirate = i;
                    pubFun.dref.setValue(pubFun.user);
                }
            }
        });
        productListZ = new ArrayList();
        price = 250;
        for (int i = 0; i < 5; i++) {
            productListZ.add(new Product(price,isBoughtZombie(i),isCurrentZombie(i)));
            price*=2;
        }
        paZ= new ProductAdapter(this,0,0,productListZ,false);
        lvZ = (ListView) findViewById(R.id.lv2);
        lvZ.setAdapter(paZ);
        lvZ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pubFun.clickSound(getBaseContext());
                lastSelected = paZ.getItem(i);
                if(!lastSelected.isBought) {
                    if(pubFun.user.coins<lastSelected.price)
                        moneyAlet();
                    else {
                        pubFun.user.coins -= lastSelected.price;
                        cc.setText(pubFun.user.coins+" Coins!");
                        pubFun.user.zombieFigures.add(i);
                        lastSelected.isBought = true;
                        cleanCurrentZombie();
                        lastSelected.isCurrent = true;
                        paZ.notifyDataSetChanged();
                        pb.setProgress(pubFun.user.pirateFigures.size() + pubFun.user.zombieFigures.size());
                        pubFun.user.currentZombie = i;
                        pubFun.dref.setValue(pubFun.user);
                    }
                }
                else {
                    cleanCurrentZombie();
                    lastSelected.isCurrent = true;
                    paZ.notifyDataSetChanged();
                    pubFun.user.currentZombie = i;
                    pubFun.dref.setValue(pubFun.user);
                }
            }
        });

        pb=(ProgressBar) findViewById(R.id.progressBar);
        pb.setProgress(pubFun.user.pirateFigures.size()+ pubFun.user.zombieFigures.size());
    }

    public boolean isBoughtPirate(int x){
        for (int i = 0; i < pubFun.user.pirateFigures.size(); i++) {
            if(pubFun.user.pirateFigures.get(i)==x)
                return true;
        }
        return false;
    }
    public boolean isBoughtZombie(int x){
        for (int i = 0; i < pubFun.user.zombieFigures.size(); i++) {
            if(pubFun.user.zombieFigures.get(i)==x)
                return true;
        }
        return false;
    }

    public boolean isCurrentPirate(int x){
        return pubFun.user.currentPirate==x;
    }
    public boolean isCurrentZombie(int x){
        return pubFun.user.currentZombie==x;
    }

    public void cleanCurrentPirate(){
        for (int i = 0; i < paP.objects.size(); i++) {
            paP.getItem(i).isCurrent=false;
        }
    }
    public void cleanCurrentZombie(){
        for (int i = 0; i < paZ.objects.size(); i++) {
            paZ.getItem(i).isCurrent=false;
        }
    }

    public void moneyAlet(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Store.this);
        builder.setCancelable(true);
        builder.setTitle("You are Broke!");
        builder.setMessage("Return playing games and earn some more coins!");
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.show();
    }
}
