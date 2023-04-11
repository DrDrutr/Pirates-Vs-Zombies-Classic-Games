package com.example.piratesvszombiesclassicgames;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

public class MenuPlus extends PopupMenu {
    public MenuPlus(final Context context, View anchor, final insDialog instr, final SettingsDialog sd) {
        super(context, anchor);
        super.getMenuInflater().inflate(R.menu.menu, super.getMenu());
        final Intent in = new Intent();
        in.putExtra("logout",false);
        super.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.HomeScreen1:
                        pubFun.clickSound(context);
                        ((Activity)context).setResult(((Activity)context).RESULT_OK,in);
                        ((Activity)context).finish();
                        return true;
                    case R.id.GameList1:
                        pubFun.clickSound(context);
                        ((Activity)context).setResult(((Activity)context).RESULT_CANCELED,in);
                        ((Activity)context).finish();
                        return true;
                    case R.id.Instructions:
                        pubFun.clickSound(context);
                        instr.show();
                        return true;
                    case R.id.Settings:
                        pubFun.clickSound(context);
                        sd.show();
                        return true;
                }
                return false;
            }
        });
    }

}
