package com.orzmo.weather_forecast.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orzmo.weather_forecast.R;
import com.orzmo.weather_forecast.Items;

import java.util.List;

public class LivesAdapter extends ArrayAdapter {
    private final int resourceId;
    public LivesAdapter(Context context, int resource, List<Items> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Items items = (Items) getItem(position);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView province = view.findViewById(R.id.home_list_province);
        TextView city = view.findViewById(R.id.home_list_city);
        TextView weather = view.findViewById(R.id.home_list_weather);
        TextView temperature = view.findViewById(R.id.home_list_temperature);
        TextView winddirection = view.findViewById(R.id.home_list_winddirection);
        TextView humidty = view.findViewById(R.id.home_list_humidity);
        TextView windpower = view.findViewById(R.id.home_list_windpower);

        province.setText(items.getProvince());
        city.setText(items.getCity());
        weather.setText("天气：" + items.getWeather());
        temperature.setText("温度：" + items.getTemperature());
        winddirection.setText("风向：" + items.getWinddirection());
        humidty.setText("湿度：" + items.getHumidity());
        windpower.setText("风速：" + items.getWindpower());
        return view;
    }
}
