package ir.allahverdi.digiapplication.ui;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {


    private GoogleMap mMap;
    LocationManager locationManager;

    // LatLong Home is (Default):
    double longt = 51.375673, lat = 35.732761, alt = 46;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng tehran = new LatLng(lat, longt);
        mMap.addMarker(new MarkerOptions().position(tehran).title("Marker in Tehran"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tehran));
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkUser();

        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        boolean isEnableGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (isEnableGPS) {
            Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "GPS Disabled", Toast.LENGTH_SHORT).show();
        }


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
            startActivity(getIntent());
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1f, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        longt = location.getLongitude();
        lat = location.getLatitude();
        alt = location.getAltitude();

        Log.e("TAG", "\n\n\nonLocationChanged: \n" +
                "\nlat : " +longt +
                "\nlat : " + lat +
                "\nalt : " + alt +
                "\n\n\n");

        showAlertDialog(longt, lat, alt);

        // Add a marker in Tehran and move the camera
        LatLng tehran = new LatLng(lat, longt);
        mMap.addMarker(new MarkerOptions().position(tehran).title("تهران"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tehran));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tehran, 15.5f));
    }

    private void showAlertDialog(double lngt, double lat, double alt) {

        AlertDialog alertDialog;
        View alert_dialog_view = LayoutInflater.from(MapsActivity.this).inflate(R.layout.alert_dialog_satelite, null);
        ImageButton img_btn_accept_location;

        img_btn_accept_location = alert_dialog_view.findViewById(R.id.img_btn_accept_location);
        TextView tv_latitude , tv_longitude , tv_altitude;
        tv_latitude = alert_dialog_view.findViewById(R.id.tv_latitude);
        tv_longitude = alert_dialog_view.findViewById(R.id.tv_longitude);
        tv_altitude = alert_dialog_view.findViewById(R.id.tv_altitude);

        tv_latitude.setText(FaNum.convert(String.valueOf(lat)));
        tv_longitude.setText(FaNum.convert(String.valueOf(lngt)));
        tv_altitude.setText(FaNum.convert(String.valueOf(alt)));

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MapsActivity.this);
        builder.setView(alert_dialog_view);
        alertDialog = builder.create();
        alertDialog.show();


        img_btn_accept_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
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

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // in this case user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(MapsActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }


} // end Maps Activity