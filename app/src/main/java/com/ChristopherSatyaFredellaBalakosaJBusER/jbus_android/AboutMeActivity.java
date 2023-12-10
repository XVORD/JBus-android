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

/**
 * AboutMeActivity Class
 * Activity which contains information of the account used when logging in,as well as implementing related methods such as top up and register renter.
 * @author Christopher Satya
 */
public class AboutMeActivity extends AppCompatActivity {
    private TextView initial,username,email,balance, TopUp, not_Registered, textview_yes;
    private Button Registered, tombol_topup;
    private BaseApiService mApiService;
    private Context mContext;
    /**
     * Initializes and sets up the UI components for the About Me activity.
     * @param savedInstanceState The saved instance state, if any.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        getSupportActionBar().hide();
        mApiService = UtilsApi.getApiService();
        mContext = this;

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        initial = findViewById(R.id.initial);


        balance = findViewById(R.id.balance);
        TopUp = findViewById(R.id.TopUp);
        tombol_topup = findViewById(R.id.tombol_topup);
        tombol_topup.setOnClickListener(v -> handleTopup());


        not_Registered = findViewById(R.id.not_Registered);
        Registered = findViewById(R.id.Registered);
        textview_yes = findViewById(R.id.textview_yes);
        handleRefreshAccount();

        tombol_topup.setOnClickListener(v->{
            handleTopup();

        });
        not_Registered.setOnClickListener(v->moveActivity(this, RegisterRenterActivity.class));

        Registered.setOnClickListener(v->moveActivity(this, ManageBusActivity.class));
    }
    /**
     * Navigates to another activity based on the provided class.
     * @param ctx The current context.
     * @param cls The target activity class.
     */
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }
    /**
     * Handles the top-up functionality for the user's account balance.
     */
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
    /**
     * Refreshes and displays the user's account information, including balance, email, and registration status.
     */
    protected void handleRefreshAccount() {
        BaseApiService mApiService = UtilsApi.getApiService();
        mApiService.getAccountbyId(LoginActivity.loggedAccount.id).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "App error", Toast.LENGTH_SHORT).show();
                    return;
                }
                initial.setText(""+loggedAccount.name.toUpperCase().charAt(0));
                username.setText(loggedAccount.name);
                email.setText(loggedAccount.email);
                balance.setText("IDR "+loggedAccount.balance);
                if (loggedAccount.company == null) {
                    textview_yes.setText("You're not registered as a renter");
                    Registered.setVisibility(View.INVISIBLE);
                    not_Registered.setVisibility(View.VISIBLE);
                } else {
                    textview_yes.setText("You're already registered as a renter");
                    Registered.setVisibility(View.VISIBLE);
                    not_Registered.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                // do something
            }
        });
    }
}