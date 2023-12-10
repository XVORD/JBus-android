package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import static com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.MainActivity.busIndex;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;

import java.util.ArrayList;
import java.util.List;
/**
 * BusArrayAdapter Class
 * It is a custom ArrayAdapter used to display a list of buses in a ListView. It extends the ArrayAdapter class and provides a custom view for each
 * item in the list. Each item displays the name of a bus, and users can click on the "schedule" icon to view the details of that bus.
 * @author Christopher Satya
 */
public class BusArrayAdapter extends ArrayAdapter<Bus> {
    private Context context;
    private List<Bus> busList;
    /**
     * Constructs a new BusArrayAdapter.
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing the TextView to use when instantiating views.
     * @param busList  The list of buses to display.
     */
    public BusArrayAdapter(@NonNull Context context, int resource, @NonNull List<Bus> busList) {

        super(context, resource, busList);
        this.context = context;
        this.busList = busList;
    }
    /**
     * Returns a view for the specified position in the adapter.
     * @param position The position of the item within the adapter's data set.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The view for the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view_list, parent, false);
        }

        Bus currentNumberPosition = getItem(position);

        TextView textView = currentItemView.findViewById(R.id.Bus1);
        textView.setText(currentNumberPosition.name);


        ImageView pressSchedule = currentItemView.findViewById(R.id.schedule);
        pressSchedule.setOnClickListener(v-> {
            Intent intent = new Intent(context, DetailBusActivity.class);
            intent.putExtra("BusId", currentNumberPosition.id);
            context.startActivity(intent);
        });

        return currentItemView;
    }
}