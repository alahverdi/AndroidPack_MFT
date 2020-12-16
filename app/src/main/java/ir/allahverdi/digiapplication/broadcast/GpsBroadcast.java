package ir.allahverdi.digiapplication.broadcast;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;

import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.ui.GpsActivity;

public class GpsBroadcast extends BroadcastReceiver {
    Context context;
    AlertDialog alertDialogGPS;
    LottieAnimationView lottie_gps;
    ImageButton img_btn_gps_setting;

    public GpsBroadcast(Context context) {
        this.context = context;

        //make alert GPS dialog :
        View alert_dialog_view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_gps_layout, null);
        lottie_gps = alert_dialog_view.findViewById(R.id.lottie_gps);
        img_btn_gps_setting = alert_dialog_view.findViewById(R.id.img_btn_gps_setting);

        img_btn_gps_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_setting_gps = new Intent();
                intent_setting_gps.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent_setting_gps);
                alertDialogGPS.dismiss();
            }
        });

        AlertDialog.Builder builderGps = new AlertDialog.Builder(context);
        builderGps.setView(alert_dialog_view)
                .setCancelable(false);

        alertDialogGPS = builderGps.create();

        if (checkGps(context)) {
            if (alertDialogGPS != null) {
                alertDialogGPS.dismiss();
            }
        } else {
            if (alertDialogGPS != null) {
                alertDialogGPS.show();
            }
        }
    }


    boolean checkGps(Context context) {
        GpsManager gpsManager = new GpsManager(context);
        if (gpsManager.isGpsAvailable()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LocationManager manager = (LocationManager) context.getSystemService(Service.LOCATION_SERVICE);
        if (checkGps(context)) {
            if (alertDialogGPS != null) {
                alertDialogGPS.dismiss();
            }
        } else {
            if (alertDialogGPS != null) {
                alertDialogGPS.show();
            }
        } // end onReceiver()
    } // end GPS BroadCast class

} // end GPS Broadcast
