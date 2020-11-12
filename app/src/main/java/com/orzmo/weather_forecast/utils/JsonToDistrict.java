package com.orzmo.weather_forecast.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orzmo.weather_forecast.Location.District;
import com.orzmo.weather_forecast.Location.DistrictParse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * @author panilsy@icloud.com
 * @description json转district助手
 */
public class JsonToDistrict {
    private static final String TAG = "JsonToDistrict";
    private String json = "";
    private CallBack cb = null;

    public JsonToDistrict() {
    }

    public JsonToDistrict(String json) {
        this.json = json;
    }

    public JsonToDistrict(String json, CallBack cb) {
        this.json = json;
        this.cb = cb;
    }

    public DistrictParse getDistricts() {
        try {
            JSONObject jsonObject = new JSONObject(this.json);
            String requestStatus = jsonObject.getString("status");
            if (!requestStatus.equals("1")) {
                cb.run("获取数据失败，请检查网络");
                return null;
            }
            JSONArray temp1 = jsonObject.getJSONArray("districts");
            String temp2 = temp1.get(0).toString();
            JSONObject temp3 = new JSONObject(temp2);
            JSONArray districts = temp3.getJSONArray("districts");

            Gson gson = new Gson();
            List<District> districtList = gson.fromJson(districts.toString(), new TypeToken<List<District>>(){}.getType());

            for(District test: districtList) {
                System.out.println(test.getName());
            }
            DistrictParse districtParse = new DistrictParse();
            districtParse.setDistricts(districtList);
            districtParse.setLength(districtList.size());
            return districtParse;
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
