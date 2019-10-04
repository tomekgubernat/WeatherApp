package com.codecool.weatherapp;

import android.content.ContentValues;
import android.content.Context;

import com.codecool.weatherapp.data.Contract;
import com.codecool.weatherapp.utilities.DataUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenWeatherJsonUtils {

    public static ContentValues[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String LIST = "list";
        final String MAIN = "main";
        final String WEATHER = "weather";
        final String MAX = "temp_max";
        final String MIN = "temp_min";
        final String ICON = "icon";
        final String HUM = "humidity";
        final String PRESS = "pressure";
        final String DESCRIPT = "description";
        final String CLOUDS = "clouds";
        final String ALL = "all";

        long currentLocalSystemDate = System.currentTimeMillis(); //Coordinated Universal Time (UTC)

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(LIST);

        ContentValues[] weatherContentValue = new ContentValues[weatherArray.length()];
        
        for (int i = 0; i < weatherArray.length(); i++) {

            double tempMax;
            double tempMin;
            String icon;
            String humidity;
            double press;
            String description;
            double clouds;
            
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            icon = weatherObject.getString(ICON);
            
            JSONObject mainWeatherData = dayForecast.getJSONObject(MAIN);
            JSONObject cloudsData = dayForecast.getJSONObject(CLOUDS);

            String unixTime = dayForecast.getString("dt");//Epoch & Unix Timestamp
            String unixTimeInMillis = unixTime + "000";

            long timeZoneInMillis = DataUtils.getOffsetTimeZone(currentLocalSystemDate);
            long dateWithTimeZone = timeZoneInMillis + Long.parseLong(unixTimeInMillis);

            tempMax = mainWeatherData.getDouble(MAX);
            tempMin = mainWeatherData.getDouble(MIN);
            humidity = mainWeatherData.getString(HUM);
            press = mainWeatherData.getDouble(PRESS);

            description = weatherObject.getString(DESCRIPT);
            clouds = cloudsData.getDouble(ALL);

            ContentValues weatherValuers = new ContentValues();
            weatherValuers.put(Contract.Entry.COLUMN_DATE, dateWithTimeZone);
            weatherValuers.put(Contract.Entry.COLUMN_MAX, tempMax);
            weatherValuers.put(Contract.Entry.COLUMN_MIN, tempMin);
            weatherValuers.put(Contract.Entry.COLUMN_ICON_ID, icon);
            weatherValuers.put(Contract.Entry.COLUMN_HUMIDITY, humidity);
            weatherValuers.put(Contract.Entry.COLUMN_PRESSURE, press);
            weatherValuers.put(Contract.Entry.COLUMN_DESCRIPTION, description);
            weatherValuers.put(Contract.Entry.COLUMN_CLOUDS, clouds);

            weatherContentValue[i] = weatherValuers;
        }
        return weatherContentValue;
    }

}
