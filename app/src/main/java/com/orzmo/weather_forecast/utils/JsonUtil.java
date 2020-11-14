package com.orzmo.weather_forecast.utils;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class JsonUtil {
    private static final String TAG = "DistrictHelper";

    private String keywords = "";
    private String subDistrict = "1";

    private final CallBack cb;

    public JsonUtil(CallBack cb) {
        this.cb = cb;
    }

    private String buildBaseUrl() {
        String offset = "100";
        String key = "f9fc0d7a78cd70acb1ccd2c60db661fe";
        String output = "json";
        String baseUrl = "https://restapi.amap.com/v3/config/district?";
        if (this.keywords.equals("")) {
            return baseUrl + "key=" + key + "&subdistrict=" + this.subDistrict + "&offset=" + offset + "&output=" + output;

        } else {
            return baseUrl + "key=" + key + "&subdistrict=" + this.subDistrict + "&offset=" + offset + "&output=" + output + "&keywords=" + this.keywords;

        }
    }

    public void getProvince() {
        try {
            // 创建实例和请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(this.buildBaseUrl()).build();

            Response response = client.newCall(request).execute();
            String resData = response.body().string();
            Log.d(TAG, resData);
            // 回调函数
            this.cb.run(resData);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void getCity(String code) {
        this.subDistrict = "1";
        this.keywords = code;
        try {
            // 创建实例和请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(this.buildBaseUrl()).build();

            Response response = client.newCall(request).execute();
            String resData = response.body().string();
            Log.d(TAG, resData);
            // 回调函数
            this.cb.run(resData);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
