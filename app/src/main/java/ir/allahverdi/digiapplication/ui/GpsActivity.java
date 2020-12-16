package ir.allahverdi.digiapplication.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.broadcast.GpsBroadcast;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;

public class GpsActivity extends AppCompatActivity implements LocationListener {

    private static final float MIN_DIS = 1f;
    private static final long MIN_TIME = 5000;

    LocationManager locationManager;
    Button btn_return, btn_googleMaps;
    private GpsBroadcast gpsBroadcast;
    private NetworkBroadcast networkBroadcast;
    LottieAnimationView lottie_gps, lottie_gps_loading;
    TextView tv_latitude_gps, tv_longitude_gps, tv_altitude_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        checkPermissionGPS();
        init();
    }

    private void init() {

        btn_return = findViewById(R.id.btn_return);
        lottie_gps = findViewById(R.id.lottie_gps);
        btn_googleMaps = findViewById(R.id.btn_googleMaps);
        tv_altitude_gps = findViewById(R.id.tv_altitude_gps);
        tv_latitude_gps = findViewById(R.id.tv_latitude_gps);
        tv_longitude_gps = findViewById(R.id.tv_longitude_gps);
        lottie_gps_loading = findViewById(R.id.lottie_gps_loading);

        // I put this button because I don't want to use toolbar in this Activity ...
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // start Maps Activity and show user location on Google Map :
        btn_googleMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GpsActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    } // end init()

    @Override
    protected void onStart() {
        super.onStart();

        checkPermissionGPS();
        checkUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register broadCast for checking NETWORK :
        networkBroadcast = new NetworkBroadcast(this);
        registerReceiver(networkBroadcast, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

        // register broadCast for checking GPS :
        gpsBroadcast = new GpsBroadcast(this);
        registerReceiver(gpsBroadcast, new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unRegister NETWORK BroadCast :
        unregisterReceiver(networkBroadcast);

        locationManager.removeUpdates(this);

        // unRegister NETWORK BroadCast :
        unregisterReceiver(gpsBroadcast);
    }

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // in this case user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(GpsActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

    private void checkPermissionGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //  ask permission again :
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    , 1
            );
            /*
             we get permission and for refresh activity we just put finish() ...
             It could be better by refresh activity ...
            */
            finish();
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME,
                MIN_DIS,
                this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        // get 3 values from GPS sensor :
        double longt = location.getLongitude();
        double lat = location.getLatitude();
        double alt = location.getAltitude();

        // set values to views :
        tv_longitude_gps.setText(FaNum.convert(String.valueOf(longt)));
        tv_latitude_gps.setText(FaNum.convert(String.valueOf(lat)));
        tv_altitude_gps.setText(FaNum.convert(String.valueOf(alt)));

        // disappear loading animation when we get info from sensor :
        if (lottie_gps.getVisibility() != View.GONE) {
            lottie_gps_loading.setVisibility(View.GONE);
        }

        // simple LOG for test and debug ...
        Log.e("TAG", "\n\n\nonLocationChanged:" +
                "\n\n" +
                longt + "\nlat : " +
                lat + "\nalt : " +
                alt + "\n\n\n");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
    }

} // end Activity