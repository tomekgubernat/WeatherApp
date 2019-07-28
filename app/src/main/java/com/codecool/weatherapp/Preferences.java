package com.codecool.weatherapp;

import android.content.Context;

public class Preferences {

    private static final String DEFAULT_LOCATION = "Londyn";

    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_LOCATION;
    }
}
