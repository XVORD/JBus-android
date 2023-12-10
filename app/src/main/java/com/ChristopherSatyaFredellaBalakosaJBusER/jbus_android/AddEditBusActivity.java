package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BaseResponse;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BusType;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Facility;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Station;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * AddEditBusActivity Class
 * It is responsible for handling the addition and editing
 * of bus information in the application. It provides a user interface for users to input
 * details such as bus name, capacity, price, type, facilities, departure station, and arrival
 * station. Users can choose from various facilities using checkboxes and select the type
 * of bus and station from dropdown menus. The class communicates with the API service to add or edit bus information.
 * @author Christopher Satya
 */
public class AddEditBusActivity extends AppCompatActivity {
    private TextView title;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText busNameET, capacityET, priceET;
    private Spinner busTypeSpinner, departureSpinner, arrivalSpinner;
    private CheckBox acCheckBox, wifiCheckBox, toiletCheckBox, lcdCheckBox;
    private CheckBox coolboxCheckBox, lunchCheckBox, baggageCheckBox, electricCheckBox;
    private Button addButton;
    private BusType[] busType = BusType.values();
    private List<Station> stationList = new ArrayList<>();
    private BusType selectedBusType;
    private int selectedDeptStationID;
    private int selectedArrStationID;
    private List<Facility> selectedFacilities = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_bus);
        getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();

        title = this.findViewById(R.id.titleAddBus);
        busNameET = this.findViewById(R.id.bus_name);
        capacityET = this.findViewById(R.id.capacity);
        priceET = this.findViewById(R.id.price);
        busTypeSpinner = this.findViewById(R.id.bus_type_dropdown);
        departureSpinner = this.findViewById(R.id.stat_depart_dropdown);
        arrivalSpinner = this.findViewById(R.id.stat_dest_dropdown);
        acCheckBox = findViewById(R.id.ac_cb);
        wifiCheckBox = findViewById(R.id.wifi_cb);
        toiletCheckBox = findViewById(R.id.toilet_cb);
        lcdCheckBox = findViewById(R.id.lcd_cb);
        coolboxCheckBox = findViewById(R.id.coolbox_cb);
        lunchCheckBox = findViewById(R.id.lunch_cb);
        baggageCheckBox = findViewById(R.id.baggage_cb);
        electricCheckBox = findViewById(R.id.electric_cb);
        addButton = this.findViewById(R.id.button_add_bus);

        String type = getIntent().getStringExtra("type");
        if (type.equals("addBus")) {
            addButton.setOnClickListener(v->handleAddBus());
            title.setText("Tambahkan Bus");
        } else {
            title.setText("Edit Bus");
        }

        ArrayAdapter adBus = new ArrayAdapter(this, android.R.layout.simple_list_item_1, busType);
        adBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        busTypeSpinner.setAdapter(adBus);
        busTypeSpinner.setOnItemSelectedListener(busTypeOISL);

        getStationList();
    }
    /**
     * Validates the user input for adding or editing bus information.
     * @return True if the input is valid, false otherwise.
     */
    private boolean validateInput() {
        updateSelectedFacilities();
        if (busNameET.getText().toString().isEmpty() ||
                capacityET.getText().toString().isEmpty() ||
                priceET.getText().toString().isEmpty() ||
                selectedFacilities.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (selectedDeptStationID == selectedArrStationID) {
            Toast.makeText(mContext, "Station cannot be same", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
    /**
     * Handles the addition of a new bus or the editing of an existing bus by sending
     * the data to the API service.
     */
    private void handleAddBus() {
        if (!validateInput()) return;

        String busName = busNameET.getText().toString();
        int seatCapacity = Integer.parseInt(capacityET.getText().toString());
        int price = Integer.parseInt(priceET.getText().toString());

        mApiService.addBus(LoginActivity.loggedAccount.id, busName, seatCapacity, selectedFacilities, selectedBusType, price, selectedDeptStationID, selectedArrStationID).enqueue(new Callback<BaseResponse<Bus>>() {
            @Override
            public void onResponse(Call<BaseResponse<Bus>> call, Response<BaseResponse<Bus>> response) {
                if (!response.isSuccessful()) return;

                BaseResponse<Bus> res = response.body();
                Toast.makeText(mContext, res.message, Toast.LENGTH_SHORT).show();
                mContext.startActivity(new Intent(mContext, ManageBusActivity.class));
            }

            @Override
            public void onFailure(Call<BaseResponse<Bus>> call, Throwable t) {

            }
        });
    }
    /**
     * Retrieves the list of stations from the API and populates the departure and arrival
     * station dropdowns.
     */
    private void getStationList() {
        mApiService.getAllStation().enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(!response.isSuccessful()) return;

                stationList = response.body();
                List<String> nameList = new ArrayList<>();
                for (Station s : stationList){
                    nameList.add(s.stationName);
                }

                ArrayAdapter deptBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, nameList);
                deptBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                departureSpinner.setAdapter(deptBus);
                departureSpinner.setOnItemSelectedListener(deptOISL);

                ArrayAdapter arrBus = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, nameList);
                arrBus.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
                arrivalSpinner.setAdapter(arrBus);
                arrivalSpinner.setOnItemSelectedListener(arrOISL);
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {

            }
        });
    }
    /**
     * Updates the selected facilities based on the checkboxes checked by the user.
     */
    private void updateSelectedFacilities() {
        selectedFacilities.clear(); // Clear the list before updating

        if (acCheckBox.isChecked()) {
            selectedFacilities.add(Facility.AC);
        }

        if (wifiCheckBox.isChecked()) {
            selectedFacilities.add(Facility.WIFI);
        }

        if (toiletCheckBox.isChecked()) {
            selectedFacilities.add(Facility.TOILET);
        }

        if (lcdCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LCD_TV);
        }

        if (coolboxCheckBox.isChecked()) {
            selectedFacilities.add(Facility.COOL_BOX);
        }

        if (lunchCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LUNCH);
        }

        if (baggageCheckBox.isChecked()) {
            selectedFacilities.add(Facility.LARGE_BAGGAGE);
        }

        if (electricCheckBox.isChecked()) {
            selectedFacilities.add(Facility.ELECTRIC_SOCKET);
        }
    }
    /**
     * Listener for the bus type dropdown selection.
     * @param parent The adapter view where the selection happened.
     * @param view The view within the adapter view that was clicked.
     * @param position The position of the item within the adapter view.
     * @param id The row id of the item that is selected.
     */
    AdapterView.OnItemSelectedListener busTypeOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedBusType = busType[position];
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    /**
     * Listener for the departure station dropdown selection.
     * @param parent The adapter view where the selection happened.
     * @param view The view within the adapter view that was clicked.
     * @param position The position of the item within the adapter view.
     * @param id The row id of the item that is selected.
     */
    AdapterView.OnItemSelectedListener deptOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedDeptStationID = stationList.get(position).id;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    /**
     * Listener for the arrival station dropdown selection.
     * @param parent The adapter view where the selection happened.
     * @param view The view within the adapter view that was clicked.
     * @param position The position of the item within the adapter view.
     * @param id The row id of the item that is selected.
     */
    AdapterView.OnItemSelectedListener arrOISL = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            selectedArrStationID = stationList.get(position).id;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


}