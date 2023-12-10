package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BaseResponse;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Renter;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * RegisterRenterActivity class
 * It is an Android activity responsible for handling the registration of a new company (Renter) within the JBusER Android application.
 * @author Christopher Satya
 */
public class RegisterRenterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText Company_name, Address, Phone_Number;
    private Button tombol_register;

    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_renter);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        Company_name = this.findViewById(R.id.Company_name);
        Address = this.findViewById(R.id.Address);
        Phone_Number = this.findViewById(R.id.Phone_Number);
        tombol_register = this.findViewById(R.id.tombol_register);

        tombol_register.setOnClickListener(v->{
            handleRegisterCompany();
        });
    }
    /**
     * Handles the registration of a new company (Renter) by sending a registration request to the server.
     * It retrieves the company name, address, and phone number from the user input fields.
     */
    protected void handleRegisterCompany() {
        String companyName = Company_name.getText().toString();
        String address = Address.getText().toString();
        String phoneNumber = Phone_Number.getText().toString();

        if (companyName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

            mApiService.registerRenter(LoginActivity.loggedAccount.id, companyName, address, phoneNumber).enqueue(new Callback<BaseResponse<Renter>>() {
            @Override
            public void onResponse(Call<BaseResponse<Renter>> call, Response<BaseResponse<Renter>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Renter> res = response.body();
                if (res.success) mContext.startActivity(new Intent(mContext, AboutMeActivity.class));
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<BaseResponse<Renter>> call, Throwable t) {
                // Handle failure to make the registration request
            }
        });
    }


}