package com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ChristopherSatyaFredellaBalakosaJBusER.jbus_android.model.Bus;

import java.util.ArrayList;
import java.util.List;

public class BusArrayAdapter extends ArrayAdapter<Bus> {
    private Context context;
    private List<Bus> busList;
    public BusArrayAdapter(@NonNull Context context, int resource, @NonNull List<Bus> busList) {

        super(context, resource, busList);
        this.context = context;
        this.busList = busList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentItemView = convertView;

        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
        }

        Bus currentNumberPosition = getItem(position);

        TextView textView = currentItemView.findViewById(R.id.textView);
        textView.setText(currentNumberPosition.name);

        textView = currentItemView.findViewById(R.id.textView2);
        textView.setText(currentNumberPosition.name);

        textView = currentItemView.findViewById(R.id.textView3);
        textView.setText(currentNumberPosition.name);

        textView = currentItemView.findViewById(R.id.textView4);
        textView.setText(currentNumberPosition.name);

        return currentItemView;
    }
}

