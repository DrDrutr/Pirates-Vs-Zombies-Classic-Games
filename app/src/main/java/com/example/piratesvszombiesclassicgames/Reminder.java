package com.example.piratesvszombiesclassicgames;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class Reminder extends Service {

    static Boolean isRun = false;
    static int sec;
    threadhere thread;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(thread==null) {
            thread=new threadhere();
            thread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thread=null;
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Notification getNotification() {
        int icon = R.drawable.logo;
        long when = System.currentTimeMillis();
        String title = "The Lucky Wheel Is Ready";
        String ticker = "ticker";
        String text="Spin it in order to win more coins!";
        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
        intent.putExtra("isStop", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        Notification notification = builder.setContentIntent(pendingIntent)
                .setSmallIcon(icon).setTicker(ticker).setWhen(when)
                .setAutoCancel(true).setContentTitle(title).setDefaults(Notification.DEFAULT_SOUND)
                .setContentText(text).build();
        return notification;
    }
    public class threadhere extends Thread{
        @Override
        public void run() {
            super.run();
            sec=3600;
            isRun=true;
            while(sec>=0) {
                try {
                    Thread.sleep(1000);
                    sec--;
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            startForeground(1,getNotification());
            isRun=false;
        }
    }

}
