package com.orzmo.weather_forecast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.orzmo.weather_forecast.Location.District;
import com.orzmo.weather_forecast.Location.DistrictParse;
import com.orzmo.weather_forecast.utils.CallBack;
import com.orzmo.weather_forecast.utils.DistrictHelper;
import com.orzmo.weather_forecast.utils.JsonToDistrict;

public class CityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        Intent intent = getIntent();
        this.initView(intent.getStringExtra("cityName"));
    }

    /**
     * @author panilsy@icloud.com
     * @description 初始化视图
     * @param cityName
     */
    private void initView(final String cityName) {
        new Thread(new Runnable(){
            @Override
            public void run() {
                DistrictHelper districtHelper = new DistrictHelper(new CallBack() {
                    @Override
                    public void run (String s) {
                        getNewDistrict(s);
                    }
                });
                districtHelper.getSecondDistrict(cityName);
            }
        }).start();
    }

    /**
     * @author panilsy@icloud.com
     * @description 获取省份
     */

    private void getNewDistrict(final String s) {

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                JsonToDistrict jtd = new JsonToDistrict(s);
                final DistrictParse districts = jtd.getDistricts();

                String[] data = new String[districts.getLength()];
                int i = 0;
                for (District item : districts.getDistricts()) {
                    data[i] = item.getName();
                    i++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(CityActivity.this, android.R.layout.simple_list_item_1, data);
                ListView lv = findViewById(R.id.list_view2);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(CityActivity.this, MainActivity.class);
                        intent.putExtra("cityCode", districts.getDistricts().get(i).getAdcode());
                        intent.putExtra("cityName", districts.getDistricts().get(i).getName());
                        startActivity(intent);
                        finish();
                    }
                });

            }
        });

    }

}
