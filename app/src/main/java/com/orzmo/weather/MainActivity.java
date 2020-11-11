package com.orzmo.weather;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.orzmo.weather.utils.CallBack;
import com.orzmo.weather.utils.JsonToCast;
import com.orzmo.weather.utils.JsonToLives;
import com.orzmo.weather.utils.LivesAdapter;
import com.orzmo.weather.utils.WeatherHelper;
import com.orzmo.weather.weather.Forecasts;
import com.orzmo.weather.weather.Lives;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private String outSideTest = "";
    private LivesAdapter livesAdapter;
    private ListView listView;
    private List<Lives> livesList = new ArrayList<Lives>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = (ListView) this.findViewById(R.id.home_list);

        this.initView();
        Intent intent = getIntent();
        String cityCode = intent.getStringExtra("cityCode");
        String cityName = intent.getStringExtra("cityName");
        this.addWatchCity(cityCode, cityName);



    }
    private void initView() {


        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        String list = pref.getString("userWatched", "");


        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DistrictActivity.class);
                startActivity(intent);
            }
        });

        Button manageButton = findViewById(R.id.button_manage);
        manageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageCityActivity.class);
                startActivity(intent);
            }
        });

        Button refreshButton = findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRefresh();
            }
        });



        if (!list.equals("")) {
            String[] cityCodes = list.split(",");

            for (String code : cityCodes) {
                this.initWeather(code);
            }

        }
    }

    private void handleRefresh() {
        livesList.clear();
        SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        String list = pref.getString("userWatched", "");

        if (!list.equals("")) {
            String[] cityCodes = list.split(",");

            for (String code : cityCodes) {
                refreshWeather(code);
            }

        }
    }

    private void addWatchCity(String cityCode, String cityName) {
        if (cityCode!=null && cityName!=null){
            SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
            String oldList = pref.getString("userWatched","");
            if (oldList.equals("")) {
                oldList += cityCode;
            } else {
                oldList = cityCode + "," + oldList;
            }
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("userWatched", oldList);
            editor.putString(cityCode, cityName);
            editor.commit();

            Log.d(TAG, pref.getString("userWatched", ""));
        }

    }

    private void initWeather(final String cityCode) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                WeatherHelper weatherHelper = new WeatherHelper(cityCode, new CallBack() {
                    @Override
                    public void run (String s) {
                        getNewWeather(s);
                    }
                });
                weatherHelper.setExtensions("base");
                weatherHelper.getWeatherWithCache(getPreferences(Context.MODE_PRIVATE));

            }
        }).start();
    }

    private void refreshWeather(final String cityCode) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                WeatherHelper weatherHelper = new WeatherHelper(cityCode, new CallBack() {
                    @Override
                    public void run (String s) {
                        getNewWeather(s);
                    }
                });
                weatherHelper.setExtensions("base");
                weatherHelper.getWeatherNotCache(getPreferences(Context.MODE_PRIVATE));

            }
        }).start();
    }

    private void getNewWeather(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JsonToLives jtl = new JsonToLives(json);
                Lives live = jtl.getLive();
                livesList.add(live);
//                livesList.sort();
                livesAdapter = new LivesAdapter(MainActivity.this, R.layout.item, livesList);
                listView.setAdapter(livesAdapter);
            }
        });

//        JsonToCast jtc = new JsonToCast(json);
//        Forecasts forecasts = jtc.getCast();
    }
}
