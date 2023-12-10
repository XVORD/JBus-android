package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.BaseApiService;
import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.request.UtilsApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * ManageBusActivity Class
 * It is an Android activity responsible for managing buses within the JBus Android application.
 * @author Christopher Satya
 */
public class ManageBusActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private ListView myBusListView;
    /**
     * Initializes the activity and sets up the user interface components.
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bus);

        getSupportActionBar().setTitle("Manage Bus");

        mContext = this;
        mApiService = UtilsApi.getApiService();
        myBusListView = this.findViewById(R.id.bus_listview);

       loadMyBus();
    }
    /**
     * Loads the list of buses owned or managed by the logged-in user.
     * It sends a request to the server to retrieve the user's buses and displays them in a list view.
     */
    protected void loadMyBus() {
        mApiService.getMyBus(LoginActivity.loggedAccount.id).enqueue(new Callback<List<Bus>>() {
            @Override
            public void onResponse(Call<List<Bus>> call, Response<List<Bus>> response) {
                if (!response.isSuccessful()) return;

                List<Bus> myBusList = response.body();
                MyArrayAdapter adapter = new MyArrayAdapter(mContext, myBusList);
                myBusListView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Bus>> call, Throwable t) {

            }
        });
    }
    /**
     * Custom ArrayAdapter for displaying a list of buses in the list view.
     */
    private class MyArrayAdapter extends ArrayAdapter<Bus> {

        /**
         * Creates a new instance of MyArrayAdapter.
         * @param context The context of the activity.
         * @param objects The list of buses to display.
         */
        public MyArrayAdapter(@NonNull Context context, @NonNull List<Bus> objects) {
            super(context, 0, objects);
        }
        /**
         * Generates and returns a view for a single item in the list view.
         * @param position    The position of the item in the list.
         * @param convertView The recycled view (if available).
         * @param parent      The parent view group.
         * @return The view for the specified item.
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View currentItemView = convertView;

            if (currentItemView == null) {
                currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
            }

            Bus currentBus = getItem(position);

            TextView busName = currentItemView.findViewById(R.id.bus_name);
            busName.setText(currentBus.name);

            ImageView addSched = currentItemView.findViewById(R.id.manage_schedule);
            addSched.setOnClickListener(v->{
                Intent i = new Intent(mContext, ManageBusSchedule.class);
                i.putExtra("busId", currentBus.id);
                mContext.startActivity(i);
            });

            return currentItemView;
        }
    }
    /**
     * Inflates the options menu for the activity.
     * @param menu The menu to inflate.
     * @return true if the menu was inflated successfully; otherwise, false.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_bus_menu, menu);
        return true;
    }
    /**
     * Handles menu item selection from the options menu.
     * @param item The selected menu item.
     * @return true if the item selection was handled; otherwise, false.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.add_bus) {
            Intent intent = new Intent(mContext, AddEditBusActivity.class);
            intent.putExtra("type", "addBus");
            startActivity(intent);
            return true;
        } else return super.onOptionsItemSelected(item);
    }
}