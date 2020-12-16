package ir.allahverdi.digiapplication.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.R;

public class SettingDigiActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_screen);
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void checkUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(Const.sharedPref, MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            // user Valid and nothing happens ...
        } else {
            Intent intent = new Intent(SettingDigiActivity.this, RegisterActivity.class);
            startActivity(intent);
            Toast.makeText(this, "لطفا ثبت نام کنید", Toast.LENGTH_LONG).show();
        }
    }
}