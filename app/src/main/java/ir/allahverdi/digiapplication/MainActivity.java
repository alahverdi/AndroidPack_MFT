package ir.allahverdi.digiapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import ir.allahverdi.digiapplication.adapter.ProductAdapterGridView;
import ir.allahverdi.digiapplication.adapter.ProductAdapterListView;
import ir.allahverdi.digiapplication.adapter.SliderAdapter;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;
import ir.allahverdi.digiapplication.database.ProductDbHelper;
import ir.allahverdi.digiapplication.entity.Product;
import ir.allahverdi.digiapplication.entity.SliderItem;
import ir.allahverdi.digiapplication.fragment.ActionBottomDialogFragment;
import ir.allahverdi.digiapplication.service.DigikalaApi;
import ir.allahverdi.digiapplication.ui.AboutUsActivity;
import ir.allahverdi.digiapplication.ui.DetailsActivity;
import ir.allahverdi.digiapplication.ui.GpsActivity;
import ir.allahverdi.digiapplication.ui.MapsActivity;
import ir.allahverdi.digiapplication.ui.MusicActivity;
import ir.allahverdi.digiapplication.ui.RegisterActivity;
import ir.allahverdi.digiapplication.ui.SettingDigiActivity;
import ir.allahverdi.digiapplication.ui.SplashActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private NetworkBroadcast networkBroadcast;
    ImageButton btn_sort_grid, btn_sort_list;
    private SliderAdapter sliderAdapter;
    ProductAdapterGridView adapter_grid;
    ProductAdapterListView adapter_list;
    SliderView sliderView;
    GridView gridView;
    EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //disable dark mode :
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();
        checkUser();
        //sayHello();
        selectFromDatabase();
        setupBanner();
        setupToolbar();
        setupNavigationView();

    } // end onCreate

    private void init() {
        // prevent to focus and open keyboard for searchBox in toolbar when activity started :
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // get animation for sor buttons:
        // init Views:
        gridView = findViewById(R.id.gridView);
        btn_sort_grid = findViewById(R.id.btn_sort_grid);
        final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.btn_animation);
        btn_sort_grid.setAnimation(animAlpha);
        btn_sort_grid.setImageResource(R.drawable.ic_sort_gridview_selected);
        btn_sort_list = findViewById(R.id.btn_sort_list);
        et_search = findViewById(R.id.et_search);

        // define search box behaviour :
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                adapter_grid.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    } // end init()

    @Override
    protected void onStart() {
        super.onStart();

        // apply Setting from shared preferences :
        if (adapter_grid != null) {
            adapter_grid.notifyDataSetChanged();
        }
        if (adapter_list != null) {
            adapter_list.notifyDataSetChanged();
        }
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

    private void sayHello() {

        // in this method I want to show simple 'Welcome Message' to the user ...

        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);
        if (sharedPreferences.contains("username")) {
            String name = sharedPreferences.getString("username", "");
            Toast.makeText(this, " سلام " + name + " خوش آمدی ", Toast.LENGTH_LONG).show();
        }
    }

    private void checkUser() {

        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

    /////////////////////////////////////////////////////////////////////////// setUp banner methods
    private void setupBanner() {
        sliderView = findViewById(R.id.imageSlider);
        sliderAdapter = new SliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);

        //set indicator animation by using SliderLayout.
        //WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(getResources().getColor(R.color.colorAccent));
        sliderView.setIndicatorUnselectedColor(getResources().getColor(R.color.slider_dot));
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                Log.i("GGG", "onIndicatorClicked: " + sliderView.getCurrentPagePosition());
            }
        });

        renewItems(sliderView);
        removeLastItem(sliderView);
        addNewItem(sliderView);
    }

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 3; i++) {
            SliderItem sliderItem = new SliderItem();
            //sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://alialahverdi.ir/wp-content/uploads/2020/12/bannerDigi01.jpg");
            } else {
                sliderItem.setImageUrl("https://alialahverdi.ir/wp-content/uploads/2020/12/bannerDigi02.jpg");
            }
            sliderItemList.add(sliderItem);
        }
        sliderAdapter.renewItems(sliderItemList);
    }

    public void removeLastItem(View view) {
        if (sliderAdapter.getCount() - 1 >= 0)
            sliderAdapter.deleteItem(sliderAdapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        //sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://alialahverdi.ir/wp-content/uploads/2020/12/bannerDigi03.jpg");
        sliderAdapter.addItem(sliderItem);
    }

    /////////////////////////////////////////////////////////////////////// end setUp banner methods

    private void setupToolbar() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_MainActivity);
        Toolbar toolbar = findViewById(R.id.toolbar_home);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout, toolbar, 0, 0);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void setupNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view_details);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent_activity = new Intent();

                switch (item.getItemId()) {

                    case R.id.nav_menu_register:
                        // Start Register Activity
                        intent_activity = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent_activity);
                        break;

                    case R.id.nav_download_music:
                        // Start Music Activity
                        intent_activity = new Intent(MainActivity.this, MusicActivity.class);
                        startActivity(intent_activity);
                        break;

                    case R.id.nav_menu_setting:
                        // Start Setting Activity
                        intent_activity = new Intent(MainActivity.this, SettingDigiActivity.class);
                        startActivity(intent_activity);
                        break;

                    case R.id.nav_menu_about_us:
                        // Start About Us Activity
                        intent_activity = new Intent(MainActivity.this, AboutUsActivity.class);
                        startActivity(intent_activity);
                        break;

                    case R.id.nav_menu_contact_us:
                        // Start GPS Activity
                        intent_activity = new Intent(MainActivity.this, GpsActivity.class);
                        startActivity(intent_activity);
                        break;
                }
                return false;
            }
        });
    }

    private List<Product> getDataFromDatabase() {
        Log.e(TAG, "getDataFromDatabase: ");
        return new ProductDbHelper(this).select();
    }

    private void selectFromDatabase() {
        adapter_grid = new ProductAdapterGridView(this, (ArrayList<Product>) getDataFromDatabase());
        gridView.setAdapter(adapter_grid);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = (Product) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra(Const.INTENT_PARAMETERS_ID, product.getId());

                startActivity(intent);

            }
        });
    }

    public void onclick_sort(View view) {


        if (view.getId() == R.id.btn_sort_list) {
            final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.btn_animation);
            btn_sort_list.startAnimation(animAlpha);
            btn_sort_list.setImageResource(R.drawable.ic_sort_list_selected);

            btn_sort_grid.setImageResource(R.drawable.ic_sort_gridview);

            adapter_list = new ProductAdapterListView(this, (ArrayList<Product>) getDataFromDatabase());
            gridView.setNumColumns(1);
            gridView.setAdapter(adapter_list);
            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter_list.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else if (view.getId() == R.id.btn_sort_grid) {
            btn_sort_list.setImageResource(R.drawable.ic_sort_list);
            final Animation animAlpha = AnimationUtils.loadAnimation(this, R.anim.btn_animation);
            btn_sort_grid.startAnimation(animAlpha);
            btn_sort_grid.setImageResource(R.drawable.ic_sort_gridview_selected);

            gridView.setNumColumns(2);
            gridView.setAdapter(adapter_grid);

        }
    }

} // end activity