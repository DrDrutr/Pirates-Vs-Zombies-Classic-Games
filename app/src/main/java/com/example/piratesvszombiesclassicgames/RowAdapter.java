package com.example.piratesvszombiesclassicgames;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.piratesvszombiesclassicgames.R;
import com.example.piratesvszombiesclassicgames.Row;

import java.util.List;

public class RowAdapter extends ArrayAdapter<Row> {
    Context context;
    List<Row> objects;
    Typeface custom_font;

    public RowAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<Row> objects) {
        super(context, resource, textViewResourceId, objects);
        this.context=context;
        this.objects=objects;
        custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ARCENA.ttf");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.row,parent,false);

        TextView tvCounter = (TextView)view.findViewById(R.id.tvCounter);
        TextView tvBulls = (TextView)view.findViewById(R.id.tvBulls);
        TextView tvCows = (TextView)view.findViewById(R.id.tvCows);
        ImageView ivBulls=(ImageView)view.findViewById(R.id.ivBulls);
        ImageView ivCows=(ImageView)view.findViewById(R.id.ivCows);
        LinearLayout board = (LinearLayout) view.findViewById(R.id.board);

        ivBulls.setColorFilter(0xff000000);
        ivCows.setColorFilter(0xffffffff);
        tvBulls.setTypeface(custom_font);
        tvCows.setTypeface(custom_font);
        tvCounter.setTypeface(custom_font);

        Row temp = objects.get(position);
        if(temp.getCounter()<9)
            tvCounter.setText("0"+temp.getCounter());
        else tvCounter.setText(String.valueOf(temp.getCounter()));
        tvBulls.setText(String.valueOf(temp.getBulls()));
        tvCows.setText(String.valueOf(temp.getCows()));
        int x =pubFun.dpToPx(50,context);
        for (int k = 0; k < temp.getIv().length; k++) {
            ImageView iv = new ImageView(context);
            iv.setImageResource(R.drawable.white);
            iv.setBackgroundResource(R.drawable.hole);
            iv.setColorFilter(temp.getIv()[k]);
            iv.setLayoutParams(new LinearLayout.LayoutParams(x, x));
            board.addView(iv);
        }


        return view;
    }
}
