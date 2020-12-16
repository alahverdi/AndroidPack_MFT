package ir.allahverdi.digiapplication.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;

public class AboutUsActivity extends AppCompatActivity {

    private NetworkBroadcast networkBroadcast;
    TextView phone_aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        init();
        setupToolbar();
    }

    private void init() {
        phone_aboutUs = findViewById(R.id.phone_aboutUs);
        phone_aboutUs.setText(FaNum.convert(phone_aboutUs.getText().toString()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register broadCast for checking NETWORK :
        networkBroadcast = new NetworkBroadcast(this);
        registerReceiver(networkBroadcast, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // unRegister NETWORK BroadCast :
        unregisterReceiver(networkBroadcast);
    }

    private void setupToolbar() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_DetailsActivity);
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void callMe(View view) {
        String phoneNumber = "tel:+989359172200";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(phoneNumber));

        startActivity(intent);
    }

    public void onclick_website(View view) {
        String url = "http://alialahverdi.ir/";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // in this case user valid and nothing happens ...
        } else {
            // send user into Register Activity :

            Intent intent = new Intent(AboutUsActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }


}