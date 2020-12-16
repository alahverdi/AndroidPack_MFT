package ir.allahverdi.digiapplication.ui;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;
import ir.allahverdi.digiapplication.database.ProductDbHelper;
import ir.allahverdi.digiapplication.entity.Product;
import ir.allahverdi.digiapplication.fragment.ActionBottomDialogFragment;

public class DetailsActivity extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener {

    private NetworkBroadcast networkBroadcast;

    ImageButton btn_more_toolbar_details;
    int id;
    TextView et_tittle_details, et_price_details, et_id_details, tv_model_details;
    ImageView iv_details;

    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();
        getAndSetIntents();
        setupToolbar();

    } // end omCreate()


    private void init() {

        // init views :
        et_tittle_details = findViewById(R.id.tv_tittle_details);
        et_price_details = findViewById(R.id.tv_price_details);
        et_id_details = findViewById(R.id.tv_id_details);
        iv_details = findViewById(R.id.iv_details);
        tv_model_details = findViewById(R.id.tv_model_details);
        btn_more_toolbar_details = findViewById(R.id.btn_more_toolbar_details);

        // set more option behaviour in toolbar (show Bottom Sheet) :
        btn_more_toolbar_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheet(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        networkBroadcast = new NetworkBroadcast(this);
        registerReceiver(networkBroadcast, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        // unRegister BroadCast Receiver :
        unregisterReceiver(networkBroadcast);
    }

    private void checkUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // user valid and nothing happens ...
        } else {

            Intent intent = new Intent(DetailsActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

    private void getAndSetIntents() {

        // first check intent from main Activity :
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        // make an instance of product and use Id (PRIMARY KEY) for reading info from database :
        id = intent.getIntExtra(Const.INTENT_PARAMETERS_ID, 0);
        Product product = new ProductDbHelper(this).select(id);

        // apply values into views from database :
        et_id_details.setText(FaNum.convert(String.valueOf(product.getId())));
        et_tittle_details.setText(product.getName());
        tv_model_details.setText(product.getModel());

        // use picasso library for image Id :
        Picasso.get()
                .load(product.getImgId())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(iv_details);

        ratingBar = findViewById(R.id.ratingBar_details);
        ratingBar.setRating(product.getScore());
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                product.setScore(rating);
                new ProductDbHelper(DetailsActivity.this).update(product);
            }
        });

        // put separator for persian price :
        DecimalFormat decimalFormat = new DecimalFormat("0,000");
        String price_separator = decimalFormat.format(product.getPrice());
        et_price_details.setText(String.valueOf(FaNum.convert(price_separator)));

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

    // Bottom Sheet (more option in toolbar):
    public void showBottomSheet(View view) {
        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);
    }

    @Override
    public void onItemClick(String item) {
        //tvSelectedItem.setText("Selected action item is " + item);
        switch (item) {
            case "نمودار قیمت":
                String urlChart = "https://www.digikala.com/";
                Intent intentChart = new Intent(Intent.ACTION_VIEW);
                intentChart.setData(Uri.parse(urlChart));
                startActivity(intentChart);
                break;
            case "مقایسه محصول":
                String urlCompare = "https://www.digikala.com/promotion-center/";
                Intent intentCompare = new Intent(Intent.ACTION_VIEW);
                intentCompare.setData(Uri.parse(urlCompare));
                startActivity(intentCompare);
                break;
            case "به اشتراک گذاری کالا":
                String Url = "https://www.alialahverdi.ir/";
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plain");
                intentShare.putExtra(Intent.EXTRA_TEXT, Url);
                startActivity(Intent.createChooser(intentShare, "Share : "));
                break;
            case "اطلاع رسانی شگفت انگیز":
                Toast.makeText(this, "اضافه شد", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void onclickSms(View view) {
        String phoneNumber = "sms:+989359172200";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(phoneNumber));
        intent.putExtra("sms_body", "متن مورد نظر");

        startActivity(intent);
    }

    public void onclickCall(View view) {
        String phoneNumber = "tel:+989359172200";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(phoneNumber));

        startActivity(intent);
    }

}
