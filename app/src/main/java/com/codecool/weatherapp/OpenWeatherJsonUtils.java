package com.codecool.weatherapp;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

public class OpenWeatherJsonUtils {

    public static String getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String MAIN_DATA = "main";
        final String OWM_WEATHER = "weather";


        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";

        final String OWM_HUM = "humidity";
        final String OWM_DESCRIPTION = "main";


        double temp_max;
        double temp_min;
        int humidity;
        String description;

        String parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        JSONObject dayForecast = forecastJson.getJSONObject(MAIN_DATA);

        JSONObject weatherObject = forecastJson.getJSONArray(OWM_WEATHER).getJSONObject(0);
        description = weatherObject.getString(OWM_DESCRIPTION);

        temp_max = dayForecast.getDouble(OWM_MAX);
        temp_min = dayForecast.getDouble(OWM_MIN);
        humidity = dayForecast.getInt(OWM_HUM);


        parsedWeatherData = description + " | " + humidity + " | " + temp_min + " / " + temp_max;

        return parsedWeatherData;
    }

}
