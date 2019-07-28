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

        long localSystemDate = System.currentTimeMillis();

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(LIST);

        ContentValues[] weatherCOntentValue = new ContentValues[weatherArray.length()];

        int j = 0;

        for (int i = 0; i < weatherArray.length()/8; i++) {

            String DateFinal;
            double temp_max;
            double temp_min;
            String icon;

            long dateWithTimeZone = DataUtils.getUTCDateWithTimeZone(localSystemDate);
            long startDay = dateWithTimeZone/ DataUtils.DAY_IN_MILI * DataUtils.DAY_IN_MILI;

            long dateInMiliSec;

            j=i*8;

            JSONObject dayForecast = weatherArray.getJSONObject(j);

            JSONObject weatherObject = dayForecast.getJSONArray(WEATHER).getJSONObject(0);
            icon = weatherObject.getString(ICON);

            dateInMiliSec = startDay + DataUtils.DAY_IN_MILI * i;
            DateFinal = DataUtils.getStringDates(dateInMiliSec);

            JSONObject mainData = dayForecast.getJSONObject(MAIN);

            temp_max = mainData.getDouble(MAX);
            temp_min = mainData.getDouble(MIN);

            ContentValues weatherValuers = new ContentValues();
            weatherValuers.put(Contract.Entry.COLUMN_DATE, DateFinal);
            weatherValuers.put(Contract.Entry.COLUMN_MAX, temp_max);
            weatherValuers.put(Contract.Entry.COLUMN_MIN, temp_min);
            weatherValuers.put(Contract.Entry.COLUMN_ICON_ID, icon);

            weatherCOntentValue[i] = weatherValuers;
        }
        return weatherCOntentValue;
    }

}
