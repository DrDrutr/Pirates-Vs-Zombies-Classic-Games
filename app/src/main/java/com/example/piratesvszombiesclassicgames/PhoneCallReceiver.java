package com.example.piratesvszombiesclassicgames;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneCallReceiver extends BroadcastReceiver {

    public static boolean is;

    @Override
    public void onReceive(Context context, Intent intent) {
        is= false;
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    is = true;
                }
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    is = false;
                }

            }
        },PhoneStateListener.LISTEN_CALL_STATE);
    }
}
