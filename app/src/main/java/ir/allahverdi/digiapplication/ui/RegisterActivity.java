package ir.allahverdi.digiapplication.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.airbnb.lottie.LottieAnimationView;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.MainActivity;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.broadcast.NetworkBroadcast;

public class RegisterActivity extends AppCompatActivity {

    private Button btn_confirm;
    String name, mobile, email, password;
    Button img_btn_start, btn_register_edit;
    ImageButton img_btn_accept_register_edit;
    private NetworkBroadcast networkBroadcast;
    LottieAnimationView lottie_register, lottie_register_edit;
    private EditText et_name, et_mobile, et_email, et_password;
    TextView tv_name_register, tv_mobile_register, tv_email_register, tv_password_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //disable dark mode :
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        init();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInputs();
                checkUserInfoValidation();
            }
        });
    } // end onCreate()

    private void checkUserInfoValidation() {

        // check user info validation :
        if (isValidInput(name, mobile, email, password)) {

            // display welcome Alert Dialog :
            AlertDialog alertDialog;
            View alert_dialog_view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.alert_dialog_register_layout, null);
            lottie_register = alert_dialog_view.findViewById(R.id.lottie_register);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setView(alert_dialog_view)
                    .setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();

            // start button in alert dialog and start Main Activity :
            img_btn_start = alert_dialog_view.findViewById(R.id.img_btn_start);
            img_btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            // Save user info in SharedPref :
            SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("username", name)
                    .putString("mobile", mobile)
                    .putString("email", email)
                    .putString("password", password);

            editor.apply();
        }
    }

    private void getUserInputs() {
        // get user inputs :
        name = et_name.getText().toString().trim();
        mobile = et_mobile.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
    }

    private void init() {
        //unFocus all editTexts :
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        et_name = findViewById(R.id.et_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_confirm = findViewById(R.id.btn_confirm);

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

    private boolean isValidInput(String name, String mobile, String email, String password) {
        if (name.length() < 3) {
            et_name.setError("نام باید حداقل بیشتر از دو حرف باشد. مانند:\n\n ali");
            et_name.requestFocus();
            return false;
        }

        if (mobile.length() != 11 || !mobile.startsWith("09")) {
            et_mobile.setError("لطفا شماره موبایل را صحیح وارد کنید. مانند:\n\n 09359172200");
            et_mobile.requestFocus();
            return false;
        }

        if (email.lastIndexOf('@') <= 0
                || !email.contains(".")
                || email.lastIndexOf('.') < email.lastIndexOf('@')
                || email.split("@").length > 2) {

            et_email.setError("لطفا ایمیل صحیح را وارد کنید. مانند:\n\n al.allahverdi@gmail.com");
            et_email.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            et_password.setError("کلمه عبور  باید حداقل بیشتر از هشت کاراکتر باشد.");
            et_password.requestFocus();
            return false;
        }

        return true;
    }

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // if user is Valid I display register information with alert dialog from shPref

            AlertDialog alertDialog;
            View alert_dialog_view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.alert_dialog_register_edit_layout, null);

            // get alert dialog views :
            lottie_register_edit = alert_dialog_view.findViewById(R.id.lottie_register_edit);
            img_btn_accept_register_edit = alert_dialog_view.findViewById(R.id.img_btn_accept_register_edit);
            btn_register_edit = alert_dialog_view.findViewById(R.id.btn_register_edit);
            tv_name_register = alert_dialog_view.findViewById(R.id.tv_name_register);
            tv_mobile_register = alert_dialog_view.findViewById(R.id.tv_mobile_register);
            tv_email_register = alert_dialog_view.findViewById(R.id.tv_email_register);

            // set alert dialog views from shared Preferences :
            tv_name_register.setText(sharedPreferences.getString("username", "null"));
            tv_mobile_register.setText(sharedPreferences.getString("mobile", "null"));
            tv_email_register.setText(sharedPreferences.getString("email", "null"));

            // display Alert Dialog :
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setView(alert_dialog_view)
                    .setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();

            // set accept button in alert dialog :
            img_btn_accept_register_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            // set edit info button in alert dialog :
            btn_register_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

        } else {
            //Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }

} // end Register Activity