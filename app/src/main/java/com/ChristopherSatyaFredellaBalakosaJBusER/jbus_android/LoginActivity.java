package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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
 * LoginActivity Class
 * Represents the login screen of the JBusER Android application.
 * Users can enter their email and password to log in or navigate to the registration screen.
 * Upon successful login, users are redirected to the main screen of the application.
 * @author Christopher Satya
 */
public class LoginActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    public static Account loggedAccount;
    private TextView daftar_sekarang = null;
    private Button tombol_login = null;
    // Fields and methods of the LoginActivity class...

    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        mContext = this;
        mApiService = UtilsApi.getApiService();
        daftar_sekarang = findViewById(R.id.daftar_sekarang);
        tombol_login = findViewById(R.id.tombol_login);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        //email.text.setTextColor(R.color.black);

        tombol_login.setOnClickListener(v -> handleLogin());
        daftar_sekarang.setOnClickListener(v-> {
            moveActivity(this, RegisterActivity.class);
        });

    }
    /**
     * Navigates to the specified activity class.
     * @param ctx The context of the current activity.
     * @param cls The class of the activity to navigate to.
     */
    private void moveActivity(Context ctx, Class<?> cls){
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }
    private void viewToast(Context ctx, String message){
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Handles the login process when the login button is clicked.
     * It sends a login request to the server and handles the response.
     */
    protected void handleLogin() {
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        mApiService.login(emailS, passwordS).enqueue(new Callback<BaseResponse<Account>>() {
            @Override
            public void onResponse(Call<BaseResponse<Account>> call, Response<BaseResponse<Account>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Email Salah " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                BaseResponse<Account> res = response.body();

                if (res.success){
                    loggedAccount = res.payload;
                    /*tombol_login.setOnClickListener(v -> moveActivity(mContext, MainActivity.class));*/
                    Intent intent = new Intent(mContext, MainActivity.class);
                    startActivity(intent);
                    viewToast(mContext, "Welcome");
                    finish();
                }
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Account>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}