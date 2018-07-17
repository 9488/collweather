package com.hjl.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hjl.coolweather.db.City;
import com.hjl.coolweather.db.County;
import com.hjl.coolweather.db.Province;
import com.hjl.coolweather.gson.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2018/7/10.
 */

public class Utility {

    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){
            try {
                //json数据转换为JSONArray数组
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0;i < allProvinces.length();i++){
                    //JSONArray数组转换为jsonbject对象
                    JSONObject provincesObject = allProvinces.getJSONObject(i);
                    //json对象分别赋值给省市对象，对应数据库的每一个字段
                    Province province = new Province();
                    //省名
                    province.setProvinceName(provincesObject.getString("name"));
                    //省id
                    province.setProvinceCode(provincesObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean handleCityResponse(String response, int provinceID){
        if (!TextUtils.isEmpty(response)){
           try {
               JSONArray allCities = new JSONArray(response);
               for (int i = 0; i  < allCities.length();i++){
                   JSONObject cityObject = allCities.getJSONObject(i);
                   City city = new City();
                   city.setCityName(cityObject.getString("name"));
                   city.setCityCode(cityObject.getInt("id"));
                   city.setProvinceId(provinceID);
                   //如果不保存，不断查询数据库，数据库没有数据接着查询从服务器获取 如此循环，请调试找出问题
                   city.save();
               }
               return true;

           } catch (JSONException e) {
               e.printStackTrace();
           }
        }

        return false;
    }

    public static boolean handleCountyResponse(String response,int cityId){
        Log.d("chaxunchengzhen","meiyou shuju ");
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i =0 ; i < allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();

                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    public static Weather handleWeatherResponse(String response){
        try {
            //将json数据转换为json对象
            JSONObject jsonObject = new JSONObject(response);
            //将json对象转换为json数组
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            //
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
