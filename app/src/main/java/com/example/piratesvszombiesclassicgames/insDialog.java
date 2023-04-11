package com.example.piratesvszombiesclassicgames;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class insDialog extends Dialog {

    private TextView tv;
    private ImageView closer;

    public insDialog(@NonNull final Context context) {
        super(context);
        super.setContentView(R.layout.instructions);
        super.setCancelable(false);
        tv = (TextView) super.findViewById(R.id.ins);
        closer = (ImageView) super.findViewById(R.id.closer);
        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insDialog.super.cancel();
                pubFun.clickSound(context);
            }
        });
        super.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/ARCENA.ttf");
        tv.setTypeface(custom_font);
        super.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setText(String str){
        tv.setText(str);
    }

}
