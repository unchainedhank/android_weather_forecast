package com.orzmo.weather_forecast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.orzmo.weather_forecast.Location.District;
import com.orzmo.weather_forecast.Location.DistrictParse;
import com.orzmo.weather_forecast.utils.CallBack;
import com.orzmo.weather_forecast.utils.DistrictHelper;
import com.orzmo.weather_forecast.utils.JsonToDistrict;

public class ProvinceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_province);


        this.initView();
    }

    /**
     * @author panilsy@icloud.com
     * @description 初始化视图
     */
    private void initView() {
        new Thread(new Runnable(){
            @Override
            public void run() {
                DistrictHelper districtHelper = new DistrictHelper(new CallBack() {
                    @Override
                    public void run (String s) {
                        getNewDistrict(s);
                    }
                });
                districtHelper.getFirstDistrict();
            }
        }).start();
    }

    /**
     * @author panilsy@icloud.com
     * @description 获取区
     * @param s
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProvinceActivity.this, android.R.layout.simple_list_item_1, data);
                ListView lv = findViewById(R.id.list_view1);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        District d = districts.getDistricts().get(i);
                        handleClickDistrictFirst(d.getName());
                    }
                });

            }
        });


    }

    /**
     * @author panilsy@icloud.com
     * @description 处理点击事件
     * @param cityName
     */
    private void handleClickDistrictFirst(String cityName) {
        Intent intent = new Intent(ProvinceActivity.this, CityActivity.class);
        intent.putExtra("cityName", cityName);
        startActivity(intent);
        finish();
    }


}
