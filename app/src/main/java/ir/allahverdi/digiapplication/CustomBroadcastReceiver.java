package ir.allahverdi.digiapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CustomBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("BroadCast", "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
    }
}
