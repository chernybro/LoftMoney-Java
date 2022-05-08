package com.chernybro.loftmoneyjava.presentation.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chernybro.loftmoneyjava.LoftApp;
import com.chernybro.loftmoneyjava.R;
import com.chernybro.loftmoneyjava.presentation.login.LoginActivity;
import com.chernybro.loftmoneyjava.presentation.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        if (TextUtils.isEmpty(authToken)) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
