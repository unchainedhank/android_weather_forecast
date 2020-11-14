package com.orzmo.weather_forecast.utils;

import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherRequest {

    private static final String TAG = "WeatherHelper";

    private final String city;
    private String extensions = "all";
    private final CallBack cb;

    public WeatherRequest(String city, CallBack cb) {
        this.city = city;
        this.cb = cb;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    private String buildBaseUrl() {
        String key = "d226ad5341f28e6bb80d510c3ed9417a";
        String output = "json";
        String baseUrl = "https://restapi.amap.com/v3/weather/weatherInfo?";
        return baseUrl + "key=" + key + "&city=" + this.city + "&extensions=" + this.extensions + "&output=" + output;
    }

    public void getWeatherNotCache(SharedPreferences pref) {
        try {
            // 创建实例和请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(this.buildBaseUrl()).build();

            Response response = client.newCall(request).execute();
            String resData = response.body().string();

            // 缓存数据
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("lives-" + this.city, resData);
            editor.apply();

            // 回调函数
            this.cb.run(resData);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void getWeatherWithCache(SharedPreferences pref) {
        String live = pref.getString("lives-" + this.city,"");
        if (!live.equals("")) {
            this.cb.run(live);
        } else {
            this.getWeatherNotCache(pref);
        }
    }
}
