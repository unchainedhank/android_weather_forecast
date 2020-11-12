package com.orzmo.weather;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.orzmo.weather.utils.CallBack;
import com.orzmo.weather.utils.JsonToLives;
import com.orzmo.weather.utils.LivesAdapter;
import com.orzmo.weather.utils.WeatherHelper;
import com.orzmo.weather.weather.Lives;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LivesAdapter livesAdapter;
    private ListView listView;
    private final List<Lives> livesList = new ArrayList<>();
    private PullToRefreshView pullToRefreshView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.listView = this.findViewById(R.id.home_list);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                makeDailog(position);
                return true;
            }
        });
        this.initView();
        Intent intent = getIntent();
        String cityCode = intent.getStringExtra("cityCode");
        String cityName = intent.getStringExtra("cityName");
        this.addWatchCity(cityCode, cityName);
    }

    private void makeDailog(final int mk) {
        final SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
        String list = pref.getString("userWatched","");
        final String[] cityCodes = list.split(",");
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("确定删除");
        dialog.setMessage("您确定要删除️");
        dialog.setCancelable(false);
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder cityCodesString = new StringBuilder();
                for (int j=0;j<cityCodes.length;j++){
                    if (j != mk) {
                        if (cityCodesString.toString().equals("")) {
                            cityCodesString = new StringBuilder(cityCodes[j]);
                        } else {
                            cityCodesString.append(",").append(cityCodes[j]);
                        }
                    }
                }
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userWatched", cityCodesString.toString());
                editor.apply();
                handleRefresh();
                Toast.makeText(MainActivity.this,"删除城市成功！", Toast.LENGTH_LONG).show();

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this,"呵呵呵，怎么不删了？", Toast.LENGTH_LONG).show();

            }
        });
        dialog.show();
    }

    /**
     * @author panilsy@icloud.com
     * @description 初始化视图
     */
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


        pullToRefreshView = findViewById(R.id.pull_to_refresh);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handleRefresh();
                        pullToRefreshView.setRefreshing(false);
                    }
                },1200);
            }
        });

        if (!list.equals("")) {
            String[] cityCodes = list.split(",");

            for (String code : cityCodes) {
                this.initWeather(code);
            }

        }
    }

    /**
     * @author panilsy@icloud.com
     * @description 处理刷新事件
     */

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
        livesList.clear();
        if (cityCode!=null && cityName!=null){
            SharedPreferences pref = getSharedPreferences("data", Context.MODE_PRIVATE);
            String oldList = pref.getString("userWatched","");


            // 先解构，判断是否存在这个城市了
            if (!oldList.equals("")) {
                String[] temp = oldList.split(",");

                for(String k : temp) {
                    if (k.equals(cityCode)) {
                        // 存在了
                        Toast.makeText(MainActivity.this,"您已经添加过这个城市了！", Toast.LENGTH_LONG).show();
                        return;
                    }
                }

            }

            if (oldList.equals("")) {
                oldList += cityCode;
            } else {
                oldList = cityCode + "," + oldList;
            }

            SharedPreferences.Editor editor = pref.edit();
            editor.putString("userWatched", oldList);
            editor.putString(cityCode, cityName);
            editor.apply();

            this.handleRefresh();
        }

    }

    /**
     * @author panilsy@icloud.com
     * @description 初始化天气预报
     * @param cityCode
     */
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

    /**
     * @author panilsy@icloud.com
     * @description 刷新天气的线程函数
     * @param cityCode
     */

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
        Toast.makeText(MainActivity.this,"data access", Toast.LENGTH_LONG).show();

    }

    /**
     * @author panilsy@icloud.com
     * @description 获取初始化天气的函数
     * @param json
     */

    private void getNewWeather(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                JsonToLives jtl = new JsonToLives(json);
                Lives live = jtl.getLive();
                livesList.add(live);
                livesAdapter = new LivesAdapter(MainActivity.this, R.layout.item, livesList);
                listView.setAdapter(livesAdapter);
            }
        });
    }
}
