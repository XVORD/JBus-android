package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class AboutMeActivity extends AppCompatActivity {

    private TextView username = null;
    private TextView email = null;
    private TextView balance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        balance = findViewById(R.id.balance);

        username.setText("Christopher Satya");
        email.setText("Chris4@gmail.com");
        balance.setText("999.999.999");
    }
}