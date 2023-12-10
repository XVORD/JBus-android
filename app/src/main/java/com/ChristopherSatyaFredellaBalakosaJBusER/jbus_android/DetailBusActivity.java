package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Facility;

import java.util.List;
/**
 * DetailBusActivity Class
 * Represents the activity that displays detailed information about a selected bus. Users can view the bus's attributes such as type, name, price,
 * capacity, departure, arrival, and available facilities. Additionally, users can proceed to make a booking by clicking the "Order" button.
 * @author Christopher Satya
 */
public class DetailBusActivity extends AppCompatActivity {
    TextView busType, name, price, capacity, departure, arrival;
    CheckBox AC, WiFi, Toilet, LCD_TV, CoolBox, Lunch, Baggage, Electric;
    Button order;
    public static Bus ClickedBus;
    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_bus);

        getSupportActionBar().hide();
        ClickedBus = MainActivity.listBus.get(MainActivity.busIndex-1);

        name = findViewById(R.id.textViewSelectedBusName);
        busType = findViewById(R.id.textViewSelectedBusType);
        capacity = findViewById(R.id.textViewSelectedCapacity);
        price = findViewById(R.id.textViewSelectedPrice);
        departure = findViewById(R.id.textViewSelectedDeparture);
        arrival = findViewById(R.id.textViewSelectedArrival);
        AC = findViewById(R.id.hasAC);
        WiFi = findViewById(R.id.hasWiFi);
        Toilet = findViewById(R.id.hasToilet);
        LCD_TV =findViewById(R.id.hasLCD_TV);
        CoolBox = findViewById(R.id.hasCoolbox);
        Lunch = findViewById(R.id.hasLunch);
        Baggage = findViewById(R.id.hasBaggage);
        Electric = findViewById(R.id.hasElectric);
        order = findViewById(R.id.orderButton);

        busType.setText(ClickedBus.busType.toString());

        if(ClickedBus.facilities.contains(Facility.AC)){
            AC.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.WIFI)){
            WiFi.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.TOILET)){
            Toilet.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.LCD_TV)){
            LCD_TV.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.COOL_BOX)){
            CoolBox.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.LUNCH)){
            Lunch.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.LARGE_BAGGAGE)){
            Baggage.setChecked(true);
        }
        if(ClickedBus.facilities.contains(Facility.ELECTRIC_SOCKET)){
            Electric.setChecked(true);
        }

        String Price = "IDR. " + ClickedBus.price.price;
        price.setText(Price);
        name.setText(ClickedBus.name);
        busType.setText(ClickedBus.busType.toString());
        departure.setText( ClickedBus.departure.toString());
        arrival.setText(ClickedBus.arrival.toString());
        String capacityValue = String.valueOf(ClickedBus.capacity);
        TextView capacityTextView = findViewById(R.id.textViewSelectedCapacity);
        capacityTextView.setText(capacityValue);


        order.setOnClickListener(v ->{
            Intent intent = new Intent(DetailBusActivity.this, MakeBooking.class);
            startActivity(intent);
        });

    }

}