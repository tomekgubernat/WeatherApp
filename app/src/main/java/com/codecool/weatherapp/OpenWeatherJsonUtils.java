package com.codecool.weatherapp;

import android.content.Context;
import android.text.format.DateUtils;

import com.codecool.weatherapp.utilities.DataUtils;
import com.codecool.weatherapp.utilities.WeatherUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class OpenWeatherJsonUtils {

    public static String[] getSimpleWeatherStringsFromJson(Context context, String forecastJsonStr)
            throws JSONException {

        final String LIST = "list";
        final String MAIN = "main";
        final String WEATHER = "weather";
        final String DATE = "dt_txt";
        final String MAX = "temp_max";
        final String MIN = "temp_min";
        final String HUM = "humidity";
        final String DESCRIPTION = "description";

        long localSystemDate = System.currentTimeMillis();

        String[] parsedWeatherData = null;

        JSONObject forecastJson = new JSONObject(forecastJsonStr);

        JSONArray weatherArray = forecastJson.getJSONArray(LIST);

        parsedWeatherData = new String[weatherArray.length()/8];

        int j = 0;

        for (int i = 0; i < weatherArray.length()/8; i++) {

            String DateFinal;
            String high_low;
            double temp_max;
            double temp_min;
            int humidity;
            String description;
            String dt_txt;

            long dateWithTimeZone = DataUtils.getUTCDateWithTimeZone(localSystemDate);
            long startDay = dateWithTimeZone/ DataUtils.DAY_IN_MILI * DataUtils.DAY_IN_MILI;

            long dateInMiliSec;

            j=i*8;

            JSONObject dayForecast = weatherArray.getJSONObject(j);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            description = weatherObject.getString(DESCRIPTION);

            dt_txt = dayForecast.getString(DATE);

            dateInMiliSec = startDay + DataUtils.DAY_IN_MILI * i;
            DateFinal = DataUtils.getStringDates(dateInMiliSec);

            JSONObject mainData = dayForecast.getJSONObject(MAIN);

            temp_max = mainData.getDouble(MAX);
            temp_min = mainData.getDouble(MIN);
            humidity = mainData.getInt(HUM);
            high_low = WeatherUtils.formatHighLows(temp_max, temp_min);

            parsedWeatherData[i] = DateFinal + "\n" + dt_txt + "\n" + description + " | " + humidity + " | " + high_low;
        }
        return parsedWeatherData;
    }

}
