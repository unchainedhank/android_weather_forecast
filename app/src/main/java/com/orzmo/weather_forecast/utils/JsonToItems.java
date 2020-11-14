package com.orzmo.weather_forecast.utils;

import com.orzmo.weather_forecast.Items;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonToItems {
    private final String json;
    private final CallBack cb = null;

    public JsonToItems(String json) {
        this.json = json;
    }

    public Items getLive() {
        Items items = new Items();

        try {
            JSONObject jsonObject = new JSONObject(this.json);
            String requestStatus = jsonObject.getString("status");
            if (!requestStatus.equals("1")) {
                cb.run("获取数据失败，请检查网络");
                return items;
            }


            JSONArray livesArr = jsonObject.getJSONArray("lives");

            String livesString = livesArr.get(0).toString();
            JSONObject liveObj = new JSONObject(livesString);

            items.setAdcode(liveObj.getString("adcode"));
            items.setCity(liveObj.getString("city"));
            items.setHumidity(liveObj.getString("humidity"));
            items.setProvince(liveObj.getString("province"));
            items.setReporttime(liveObj.getString("reporttime"));
            items.setTemperature(liveObj.getString("temperature"));
            items.setWeather(liveObj.getString("weather"));
            items.setWinddirection(liveObj.getString("winddirection"));
            items.setWindpower(liveObj.getString("windpower"));

            return items;
        } catch (Exception e) {
            return items;
        }


    }
}
