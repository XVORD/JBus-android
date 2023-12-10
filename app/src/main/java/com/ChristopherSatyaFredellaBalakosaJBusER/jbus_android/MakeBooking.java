package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import static com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.LoginActivity.loggedAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.BaseResponse;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Payment;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Schedule;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.UtilsApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * MakeBooking Class
 * It is an Android activity that allows users to make a booking for a bus with available schedules and seats.
 * @author Christopher Satya
 */
public class MakeBooking extends AppCompatActivity {

    private BaseApiService apiService;
    private Bus selectedBus;
    private TextView txtBusName, txtBusDetails;
    private Spinner spinnerSchedules, spinnerSeats;
    private Button btnConfirmBooking;

    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_booking);

        setupViews();
        apiService = UtilsApi.getApiService();

        int busId = getIntent().getIntExtra("busId", -1);
        fetchBusDetails(busId);
    }
    /**
     * Sets up the views and event listeners for the activity.
     */
    private void setupViews() {
        txtBusName = findViewById(R.id.busName_Book);
        txtBusDetails = findViewById(R.id.busDetails_Book);
        spinnerSchedules = findViewById(R.id.schedule_dropdown);
        spinnerSeats = findViewById(R.id.seat_dropdown);
        btnConfirmBooking = findViewById(R.id.book_button);

        btnConfirmBooking.setOnClickListener(v -> processBooking());
    }
    /**
     * Fetches the details of the selected bus from the server.
     * @param busId The ID of the selected bus.
     */
    private void fetchBusDetails(int busId) {
        if (busId == -1) {
            Toast.makeText(this, "Error: Bus ID not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Call<Bus> call = apiService.getBusbyId(busId);
        call.enqueue(new Callback<Bus>() {
            @Override
            public void onResponse(Call<Bus> call, Response<Bus> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selectedBus = response.body();
                    displayBusInfo(selectedBus);
                } else {
                    showError("Failed to retrieve bus details.");
                }
            }

            @Override
            public void onFailure(Call<Bus> call, Throwable t) {
                showError("Server communication error.");
            }
        });
    }
    /**
     * Displays the information of the selected bus on the UI.
     * @param bus The selected bus.
     */
    private void displayBusInfo(Bus bus) {
        txtBusName.setText(bus.name);
        String details = "Type: " + bus.busType + "\nCapacity: " + bus.capacity + "\nPrice: " + bus.price.price;
        txtBusDetails.setText(details);

        List<String> scheduleDates = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Schedule schedule : bus.schedules) {
            scheduleDates.add(dateFormat.format(schedule.departureSchedule));
        }

        ArrayAdapter<String> scheduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, scheduleDates);
        spinnerSchedules.setAdapter(scheduleAdapter);
        spinnerSchedules.setOnItemSelectedListener(new ScheduleSelectedListener());
    }

    private class ScheduleSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            updateSeatSpinner(selectedBus.schedules.get(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Do nothing
        }
    }

    /**
     * Updates the seat spinner based on the selected schedule.
     * @param schedule The selected schedule.
     */

    private void updateSeatSpinner(Schedule schedule) {
        List<String> availableSeats = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : schedule.seatAvailability.entrySet()) {
            if (entry.getValue()) {
                availableSeats.add(entry.getKey());
            }
        }

        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, availableSeats);
        spinnerSeats.setAdapter(seatAdapter);
    }

    /**
     * Processes the booking by sending a booking request to the server.
     */
    private void processBooking() {
        int scheduleIndex = spinnerSchedules.getSelectedItemPosition();
        String selectedSeat = (String) spinnerSeats.getSelectedItem();
        if (scheduleIndex < 0 || selectedSeat == null) {
            showError("Please select a seat.");
            return;
        }

        confirmBooking(selectedBus.schedules.get(scheduleIndex), selectedSeat);
    }
    /**
     * Confirms the booking by sending a booking request to the server.
     * @param schedule The selected schedule for booking.
     * @param seat     The selected seat for booking.
     */
    private void confirmBooking(Schedule schedule, String seat) {
        List<String> seats = new ArrayList<>();
        seats.add(seat);

        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(schedule.departureSchedule);
        apiService.makeBooking(loggedAccount.id, selectedBus.accountId, selectedBus.id, seats, formattedDate)
                .enqueue(new BookingCallback());
    }
    /**
     * Custom callback class for handling the booking response from the server.
     */
    private class BookingCallback implements Callback<BaseResponse<Payment>> {
        @Override
        public void onResponse(Call<BaseResponse<Payment>> call, Response<BaseResponse<Payment>> response) {
            if (response.isSuccessful() && response.body() != null && response.body().success) {
                navigateToMainScreen();
                showMessage("Booking successful.");
            } else {
                showError("Booking failed.");
            }
        }
        /**
         * Custom callback class for handling the booking response from the server.
         */
        @Override
        public void onFailure(Call<BaseResponse<Payment>> call, Throwable t) {
            showError("Booking error.");
        }
    }
    /**
     * Navigates to the main screen of the application.
     */
    private void navigateToMainScreen() {
        Intent intent = new Intent(MakeBooking.this, MainActivity.class);
        startActivity(intent);
    }
    /**
     * Displays an error message to the user.
     * @param message The error message to display.
     */
    private void showError(String message) {
        Toast.makeText(MakeBooking.this, message, Toast.LENGTH_SHORT).show();
    }
    /**
     * Displays a message to the user.
     * @param message The message to display.
     */
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}