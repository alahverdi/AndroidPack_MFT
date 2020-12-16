package ir.allahverdi.digiapplication.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;

import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.R;

public class NetworkBroadcast extends BroadcastReceiver {

    Context context;
    AlertDialog alertDialog;
    LottieAnimationView lottie;
    ImageButton img_btn_wifi_setting, img_btn_mobileData_setting;

    public NetworkBroadcast(Context context) {
        this.context = context;

        // create Network Alert Dialog :
        View alert_dialog_view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_internet_layout, null);
        lottie = alert_dialog_view.findViewById(R.id.lottie_network);
        img_btn_wifi_setting = alert_dialog_view.findViewById(R.id.img_btn_wifi_setting);
        img_btn_mobileData_setting = alert_dialog_view.findViewById(R.id.img_btn_mobileData_setting);

        img_btn_wifi_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_wifi_setting = new Intent();
                intent_wifi_setting.setAction(Settings.ACTION_WIFI_SETTINGS);
                context.startActivity(intent_wifi_setting);
                alertDialog.dismiss();
            }
        });

        img_btn_mobileData_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mobileData_setting = new Intent();
                intent_mobileData_setting.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent_mobileData_setting);
                alertDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(alert_dialog_view)
                .setCancelable(false);

        alertDialog = builder.create();
    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.isNetworkAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = manager.getActiveNetworkInfo();

        //should check null because in airplane mode it will be null
        //if (netInfo != null) {
        if (checkInternet(context)) {
            if (alertDialog != null) {
                alertDialog.dismiss();
            }
        } else {
            if (alertDialog != null) {
                alertDialog.show();
            }
        }
        //} // end if loop
    } // end onReceiver()
} // end class Network BroadCast

