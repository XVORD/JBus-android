package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import static com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.TextKeyListener;
import android.view.View;
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
    private TextView initial,username,email,balance, TopUp, tombol_topup, not_Registered, textview_not, textview_yes= null;
    private Button Registered = null;
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
        TopUp = findViewById(R.id.TopUp);
        tombol_topup = findViewById(R.id.tombol_topup);
        tombol_topup.setOnClickListener(v -> handleTopup());

        username.setText(loggedAccount.name);
        email.setText(loggedAccount.email);

        Double strdouble = new Double(loggedAccount.balance);
        balance.setText(strdouble.toString());

        not_Registered = findViewById(R.id.not_Registered);
        textview_not = findViewById(R.id.textview_not);
        Registered = findViewById(R.id.Registered);
        textview_yes = findViewById(R.id.textview_yes);

        if(loggedAccount.company == null){
            not_Registered.setVisibility(View.VISIBLE);
            textview_not.setVisibility(View.VISIBLE);
            Registered.setVisibility(View.INVISIBLE);
            textview_yes.setVisibility(View.INVISIBLE);
            not_Registered.setOnClickListener(v-> {moveActivity(this, RegisterRenterActivity.class);});
        }
        if (loggedAccount.company != null){
            Registered.setVisibility(View.VISIBLE);
            textview_yes.setVisibility(View.VISIBLE);
            not_Registered.setVisibility(View.INVISIBLE);
            textview_not.setVisibility(View.INVISIBLE);
            Registered.setOnClickListener(v-> {moveActivity(this, ManageBusActivity.class);});
        }
    }
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
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

    protected void handleRenter(){

    }
}