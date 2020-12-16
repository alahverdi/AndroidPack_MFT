package ir.allahverdi.digiapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.adapter.MusicAdapter;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;
import ir.allahverdi.digiapplication.entity.Music;

public class MusicActivity extends AppCompatActivity {

    private static final String TAG = "MusicActivity";
    private NetworkBroadcast networkBroadcast;

    int music_ID = 0;
    int musicSize = 0;
    ListView listView;
    EditText et_search;
    MusicAdapter adapter;
    ArrayList<Music> result;

    String[] src_Downloads = {
            "https://files.musico.ir/Song/MohammadReza%20Shajarian%20-%20Bi%20Hamzaban%20(128).mp3",
            "http://sv.naghmemusic.ir/archive/e/ebi/Ebi%20-%20Nazi%20Naz%20Kon/10%20Sayeh.mp3",
            "http://dl.takmusics.ir/Song/Hichkas%20%7C%20Ki%20Mige%20(320).mp3",
            "http://dl.downloadspeed.ir/nsr/Mohsen%20Namjoo/Alaki/08.%20Alaki%20-%20Www.DownLoadSpeed.IR.mp3",
            "http://dl.sakhamusic.ir/95/ordi/08/Damahi%20-%20Divaneh.mp3",
            "http://dl.mahanmusic.net/Musics/Habib/Habib%20Donya-320.mp3",
            "http://dl2.sarimusic.net/1397/09/23/1/Bomrani%20-%20Payeez.mp3",
            "http://dl.tabamusic.com/Music/1399/05/Sirvan%20Khosravi%20-%20Hobab.mp3",
            "https://irsv.upmusics.com/Tracks/Songs/Homayoun%20Shajarian%20-%20Arayeshe%20Ghaliz%20(UpMusic).mp3",
            "https://ts2.tarafdari.com/contents/user615351/content-sound/hmdshmlw.mp3"};

    String[] src_sdCard = {
            "/sdcard/شجریان - به سکوت سرد زمان.mp3",
            "/sdcard/ابی - سایه.mp3",
            "/sdcard/هیچکس - کی میگه.mp3",
            "/sdcard/نامجو - الکی.mp3",
            "/sdcard/داماهی - دیوانه.mp3",
            "/sdcard/حبیب - دنیا.mp3",
            "/sdcard/بمرانی - پاییز.mp3",
            "/sdcard/سیروان - حباب.mp3",
            "/sdcard/همایون شجریان - آرایش غلیظ.mp3",
            "/sdcard/شاملو - اشک رازیست.mp3",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //disable dark mode :
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        checkPermission();
        setupToolbar();
        init();
        setupListView();
    }

    private void init() {
        //unFocus all editTexts :
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_search = findViewById(R.id.et_search);

        // set music search box behaviour :
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    } // end init()

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
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // int this case user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(MusicActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

    private void setupListView() {

        // 1) Make View :
        listView = findViewById(R.id.listView);

        // 2) Create data :
        ArrayList<Music> data = getMusicData();

        // 3) Create Adapter :
        adapter = new MusicAdapter(this, data);

        // 4) Bind Adapter to View :
        listView.setAdapter(adapter);

        // 5) onItemClick
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = (Music) parent.getItemAtPosition(position);
                music_ID = music.getId();

                switch (music_ID) {
                    case 1:
                        new MyDownloder().execute(src_Downloads[0]);
                        break;
                    case 2:
                        new MyDownloder().execute(src_Downloads[1]);
                        break;
                    case 3:
                        new MyDownloder().execute(src_Downloads[2]);
                        break;
                    case 4:
                        new MyDownloder().execute(src_Downloads[3]);
                        break;
                    case 5:
                        new MyDownloder().execute(src_Downloads[4]);
                        break;
                    case 6:
                        new MyDownloder().execute(src_Downloads[5]);
                        break;
                    case 7:
                        new MyDownloder().execute(src_Downloads[6]);
                        break;
                    case 8:
                        new MyDownloder().execute(src_Downloads[7]);
                        break;
                    case 9:
                        new MyDownloder().execute(src_Downloads[8]);
                        break;
                    case 10:
                        new MyDownloder().execute(src_Downloads[9]);
                        break;
                } // end switch
            }
        });
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

    private String songNameGenerator(int id) {
        String songName = "";
        switch (id) {
            case 1:
                songName = "شجریان - به سکوت سرد زمان";
                break;
            case 2:
                songName = "ابی - سایه";
                break;
            case 3:
                songName = "هیچکس - کی میگه";
                break;
            case 4:
                songName = "نامجو - الکی";
                break;
            case 5:
                songName = "داماهی - دیوانه";
                break;
            case 6:
                songName = "حبیب - دنیا";
                break;
            case 7:
                songName = "بمرانی - پاییز";
                break;
            case 8:
                songName = "سیروان - حباب";
                break;
            case 9:
                songName = "همایون شجریان - آرایش غلیظ";
                break;
            case 10:
                songName = "شاملو - اشک رازیست";
                break;
        }
        return songName;
    }

    private ArrayList<Music> getMusicData() {
        result = new ArrayList<>();

        result.add(new Music(2, R.drawable.loading, songNameGenerator(2)));
        result.add(new Music(1, R.drawable.loading, songNameGenerator(1)));
        result.add(new Music(3, R.drawable.loading, songNameGenerator(3)));
        result.add(new Music(4, R.drawable.loading, songNameGenerator(4)));
        result.add(new Music(5, R.drawable.loading, songNameGenerator(5)));
        result.add(new Music(6, R.drawable.loading, songNameGenerator(6)));
        result.add(new Music(7, R.drawable.loading, songNameGenerator(7)));
        result.add(new Music(8, R.drawable.loading, songNameGenerator(8)));
        result.add(new Music(9, R.drawable.loading, songNameGenerator(9)));
        result.add(new Music(10, R.drawable.loading, songNameGenerator(10)));

        return result;
    }


    ///////////////////////////////      Downloader Class ()     ///////////////////////////////
    public class MyDownloder extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        public MyDownloder() {
            this.progressDialog = new ProgressDialog(MusicActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setMax(100);
            this.progressDialog.setTitle("در حال دانلود ...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.progressDialog.setProgress(0);
            this.progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String urlAddress = params[0];

            String path = "";

            try {
                URL url = new URL(urlAddress);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                int lenght = urlConnection.getContentLength();
                musicSize = lenght;
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());

                switch (music_ID) {
                    case 1:
                        path = src_sdCard[0];
                        break;
                    case 2:
                        path = src_sdCard[1];
                        break;
                    case 3:
                        path = src_sdCard[2];
                        break;
                    case 4:
                        path = src_sdCard[3];
                        break;
                    case 5:
                        path = src_sdCard[4];
                        break;
                    case 6:
                        path = src_sdCard[5];
                        break;
                    case 7:
                        path = src_sdCard[6];
                        break;
                    case 8:
                        path = src_sdCard[7];
                        break;
                    case 9:
                        path = src_sdCard[8];
                        break;
                    case 10:
                        path = src_sdCard[9];
                        break;
                }
                OutputStream outputStream = new FileOutputStream(path);

                byte[] buffer = new byte[1024];

                int count = -1;
                int total = 0;
                while ((count = inputStream.read(buffer)) != -1) {
                    total += count;
                    publishProgress((total * 100) / lenght);
                    outputStream.write(buffer, 0, count);
                }

                outputStream.flush();
                outputStream.close();

                inputStream.close();

                return path;

            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        } // end DoInBackground ()

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            this.progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String musicName = s.substring(8);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MusicActivity.this);
            builder.setContentTitle("دانلود انجام شد")
                    .setContentText(musicName)
                    .setSmallIcon(R.drawable.ic_music);

            Intent intent = new Intent(MusicActivity.this, MusicDetailsActivity.class);
            intent.putExtra("name", musicName)
                    .putExtra("musicLocation", s)
                    .putExtra("size", String.valueOf(musicSize));

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    MusicActivity.this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            builder.addAction(R.drawable.ic_music, "جزپیات آهنگ", pendingIntent);

            // check version of Android OS for display notification :
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("chanelID", "channelName", notificationManager.IMPORTANCE_DEFAULT);
                builder.setChannelId("chanelID");
                notificationManager.createNotificationChannel(channel);
            }


            Notification notification = builder.build();
            notificationManager.notify(1, notification);

            this.progressDialog.dismiss();
            Log.e(TAG, s);

        }
    } ///////////////////////////////      end Downloader Class ()     ///////////////////////////////



}