package com.example.commande_pc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.commande_pc.entity.User;


@SuppressLint("CustomSplashScreen")
public class LaunchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        new Handler().postDelayed(() -> {
            User user = Utils.getLoggedUser(LaunchActivity.this);
            Class<?> destination;
            if (user == null) {
                destination = LoginActivity.class;
            } else {
                destination = MainActivity.class;
            }
            Intent intent = new Intent(LaunchActivity.this, destination);
            startActivity(intent);
            finish();
        }, 6000);
    }
}