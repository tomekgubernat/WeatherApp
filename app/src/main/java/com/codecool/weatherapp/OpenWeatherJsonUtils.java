package com.codecool.weatherapp;

import android.content.Context;

import com.codecool.weatherapp.utilities.WeatherUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenWeatherJsonUtils {

    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String OWM_LIST = "list";

        final String OWM_MAIN = "main";

        final String OWM_WEATHER = "weather";

        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";

        final String OWM_HUM = "humidity";
        final String OWM_DESCRIPTION = "main";



        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        parsedWeatherData = new String[weatherArray.length()];

        for (int i = 0; i < weatherArray.length(); i++) {

            String high_low;
            double temp_max;
            double temp_min;
            int humidity;
            //String description;

            JSONObject dayForecast = weatherArray.getJSONObject(i);

            //JSONObject weatherObject = forecastJson.getJSONArray(OWM_WEATHER).getJSONObject(0);
            //description = weatherObject.getString(OWM_DESCRIPTION);

            JSONObject mainData = dayForecast.getJSONObject(OWM_MAIN);

            temp_max = mainData.getDouble(OWM_MAX);
            temp_min = mainData.getDouble(OWM_MIN);
            humidity = mainData.getInt(OWM_HUM);
            high_low = WeatherUtils.formatHighLows(temp_max, temp_min);

            parsedWeatherData[i] =  " | " + humidity + " | " + high_low;

        }


        return parsedWeatherData;
    }

}
