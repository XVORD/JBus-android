package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import static com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Account;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BaseResponse;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutMeActivity extends AppCompatActivity {
    private TextView initial,username,email,balance, TopUp, tombol_topup = null;
    private BaseApiService mApiService;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        mApiService = UtilsApi.getApiService();
        mContext = this;

        initial = findViewById(R.id.initial);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        balance = findViewById(R.id.balance);
        TopUp = findViewById(R.id.TopUP);
        tombol_topup = findViewById(R.id.tombol_topup);
        tombol_topup.setOnClickListener(v -> handleTopup());
        username.setText(loggedAccount.name);
        email.setText(loggedAccount.email);
        Double strdouble = new Double(loggedAccount.balance);
        balance.setText(strdouble.toString());
    }
    protected void handleTopup() {
        String topUpS = TopUp.getText().toString();
        if (topUpS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        double topUpD = Double.valueOf(topUpS);
        mApiService.topUp(LoginActivity.loggedAccount.id, topUpD).enqueue(new Callback<BaseResponse<Double>>() {
            @Override
            public void onResponse(Call<BaseResponse<Double>> call, Response<BaseResponse<Double>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Double> res = response.body();
                if (res.success){
                    finish();
                    LoginActivity.loggedAccount.balance += res.payload;
                    startActivity(getIntent());
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<BaseResponse<Double>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}