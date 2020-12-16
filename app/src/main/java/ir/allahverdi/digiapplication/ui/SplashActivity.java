package ir.allahverdi.digiapplication.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.manager.ConnectivityMonitor;

import java.util.Timer;
import java.util.TimerTask;

import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.service.ServiceDigikalaApi;

public class SplashActivity extends AppCompatActivity {


    SplashView splash_view;
    Timer timer = new Timer();
    private NetworkBroadcast networkBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //disable dark mode :
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();
        setupAnimLogo();

        networkBroadcast = new NetworkBroadcast();
        registerReceiver(networkBroadcast, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    private void init() {
        splash_view = new SplashView(this);
        setContentView(splash_view);

        // register BroadCast :
        SuccessApi successApi = new SuccessApi();
        registerReceiver(successApi, new IntentFilter("SuccessApi"));
    }


    @Override
    protected void onStop() {
        unregisterReceiver(networkBroadcast);
        super.onStop();
    }

    private void setupAnimLogo() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (splash_view.logoTop >= splash_view.cy - 400) {
                    splash_view.logoTop -= 4;
                    splash_view.logoBottom -= 4;
                    splash_view.invalidate();
                }
            }
        }, 0, 2);
    }


    // get response from SERVICE that get Api Service :
    private class SuccessApi extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    //BroadCast Receiver for checking NETWORK state :
    private class NetworkBroadcast extends BroadcastReceiver {

        AlertDialog alertDialog;
        LottieAnimationView lottie;
        ImageButton img_btn_wifi_setting, img_btn_mobileData_setting;

        public NetworkBroadcast() {
            // setUp Alert Dialog for Network State :

            View alert_dialog_view = LayoutInflater.from(SplashActivity.this).inflate(R.layout.alert_dialog_internet_layout, null);
            lottie = alert_dialog_view.findViewById(R.id.lottie_network);
            img_btn_wifi_setting = alert_dialog_view.findViewById(R.id.img_btn_wifi_setting);
            img_btn_mobileData_setting = alert_dialog_view.findViewById(R.id.img_btn_mobileData_setting);

            img_btn_wifi_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_wifi_setting = new Intent();
                    intent_wifi_setting.setAction(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent_wifi_setting);
                    alertDialog.dismiss();
                }
            });

            img_btn_mobileData_setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent_mobileData_setting = new Intent();
                    intent_mobileData_setting.setAction(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    startActivity(intent_mobileData_setting);
                    alertDialog.dismiss();
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);

            builder.setView(alert_dialog_view)
                    .setCancelable(false);

            alertDialog = builder.create();
        }

        boolean checkInternet(Context context) {

            ServiceManager serviceManager = new ServiceManager(context);

            if (serviceManager.isNetworkAvailable()) {

                // check Network and if it's available SERVICE Started :

                Intent intentService = new Intent(SplashActivity.this, ServiceDigikalaApi.class);
                startService(intentService);

                return true;

            } else {

                return false;
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = manager.getActiveNetworkInfo();

            //could check null because in airplane mode it will be null :
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

    public static class ServiceManager {

        Context context;

        public ServiceManager(Context base) {
            context = base;
        }

        public boolean isNetworkAvailable() {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

} // end activity