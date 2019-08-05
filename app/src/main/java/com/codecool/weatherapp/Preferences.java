package com.codecool.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private static final String DEFAULT_LOCATION = "London";
    private static final String KEY_UNIT_PREFERENCE = "key_temp_units";
    private static final String DEFAULT_UNITS = "celsius";


    public static String getPreferredWeatherLocation(Context context) {
        return getDefaultWeatherLocation();
    }

    private static String getDefaultWeatherLocation() {
        return DEFAULT_LOCATION;
    }


    public static boolean isCelsius(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceUnits = sharedPreferences.getString(KEY_UNIT_PREFERENCE, DEFAULT_UNITS);

        boolean selectedPreference;

        if(DEFAULT_UNITS.equals(preferenceUnits)){
            selectedPreference = true;
        } else {
            selectedPreference = false;
        }
        return selectedPreference;
    }
}
