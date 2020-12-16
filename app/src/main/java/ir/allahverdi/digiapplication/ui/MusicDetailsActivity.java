package ir.allahverdi.digiapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;

public class MusicDetailsActivity extends AppCompatActivity {

    LottieAnimationView lottie_music;
    private NetworkBroadcast networkBroadcast;
    TextView tv_music_name, tv_music_size, tv_music_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_details);

        init();
        setupToolbar();
        getIntentValues();

    }

    private void init() {
        lottie_music = findViewById(R.id.lottie_music);
        tv_music_name = findViewById(R.id.tv_music_name);
        tv_music_size = findViewById(R.id.tv_music_size);
        tv_music_location = findViewById(R.id.tv_music_location);
    }

    private void getIntentValues() {
        if (getIntent() != null) {
            String name = getIntent().getStringExtra("name");
            String locationAddress = getIntent().getStringExtra("musicLocation");
            String size = getIntent().getStringExtra("size");
            int sizeMB = Integer.valueOf(size) / 1000000;
            tv_music_name.setText(name);
            tv_music_size.setText(FaNum.convert(String.valueOf(sizeMB)) + " Mb");
            tv_music_location.setText(locationAddress);
        }
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

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(MusicDetailsActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

    public void onclick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}