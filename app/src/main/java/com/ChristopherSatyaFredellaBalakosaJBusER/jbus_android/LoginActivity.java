package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private TextView daftar_sekarang = null;
    private Button tombol_login = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        daftar_sekarang = findViewById(R.id.daftar_sekarang);
        tombol_login = findViewById(R.id.tombol_login);
        daftar_sekarang.setOnClickListener(v-> {
            moveActivity(this, RegisterActivity.class);
        });
        tombol_login.setOnClickListener(x->{
            moveActivity(this, MainActivity.class);
        });
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
}