package com.example.piratesvszombiesclassicgames;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    List<Product> objects;
    boolean b;
    Typeface custom_font;

    public ProductAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Product> objects, boolean b) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        this.b=b;
        custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ARCENA.ttf");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.product,parent,false);

        TextView tv = (TextView) view.findViewById(R.id.tv);
        ImageView pic = (ImageView) view.findViewById(R.id.pic);

        Product temp = objects.get(position);
        if(temp.isBought){
            pubFun.figure(b,position,pic);
        }
        else{
            tv.setText("?\n"+temp.price);
            tv.setTypeface(custom_font);
        }
        if(temp.isCurrent)
            pic.setBackgroundResource(R.drawable.roundbutton);
        else pic.setBackgroundResource(R.drawable.bigsign);

        return view;
    }
}
