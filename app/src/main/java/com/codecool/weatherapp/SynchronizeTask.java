package com.codecool.weatherapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.codecool.weatherapp.data.Contract;
import com.codecool.weatherapp.utilities.ConnectUtils;

import java.net.URL;

public class SynchronizeTask {

    synchronized public static void syncWeather(Context context) {

        try {

            String location = Preferences.getPreferredWeatherLocation(context);

            URL weatherRequestUrl = ConnectUtils.buildWeatherUrl(location);

            String jsonWeatherResponse = ConnectUtils.getResponseFromHttpUrl(weatherRequestUrl);

            ContentValues[] weatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(context, jsonWeatherResponse);

            if (weatherData != null && weatherData.length != 0) {
                ContentResolver contentResolver = context.getContentResolver();

                contentResolver.delete(Contract.Entry.CONTENT_URI, null, null);

                contentResolver.bulkInsert(Contract.Entry.CONTENT_URI, weatherData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            }

        }
}

