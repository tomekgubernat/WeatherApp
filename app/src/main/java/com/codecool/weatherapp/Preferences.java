package com.codecool.weatherapp;

import android.content.Context;

public class Preferences {

    private static final String DEFAULT_WEATHER_LOCATION = "Londyn,UK";


    public static String getPreferredWeatherLocation(Context context) {
        /** This will be implemented in a future lesson **/
        return getDefaultWeatherLocation();
    }

    private static String getDefaultWeatherLocation() {
        /** This will be implemented in a future lesson **/
        return DEFAULT_WEATHER_LOCATION;
    }
}
