package com.codecool.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Preferences {

    //private static final String DOWNLOAD_CURRENT_LOCATION = "Warszawa";
    private static final String KEY_UNIT_PREFERENCE = "key_temp_units";
    private static final String DEFAULT_UNITS = "celsius";
    private static final String DOWNLOAD_CURRENT_LOCATION = "Warszawa"; //from GPS


    private static String Localization = CurrentLoczalization.City2; //

    public static void setLocalization(String localization) {
        Localization = localization;
    }

    public static String getPreferredWeatherLocation() {
        return Localization;
    }

    public static boolean isCelsius(Context context){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String preferenceUnits = sharedPreferences.getString(KEY_UNIT_PREFERENCE, DEFAULT_UNITS);
        Log.v("unit", preferenceUnits);

        boolean selectedPreference;

        if(DEFAULT_UNITS.equals(preferenceUnits)){
            selectedPreference = true;
        } else {
            selectedPreference = false;
        }
        return selectedPreference;
    }

    public static boolean isLocation(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE);
        String preferenceUnits = sharedPreferences.getString("Name", null);

        boolean selectedPreference;
        //Log.v("tomek", preferenceUnits);


        if(DOWNLOAD_CURRENT_LOCATION.equals(preferenceUnits)){
            selectedPreference = true;
        } else {
            selectedPreference = false;
        }
        return selectedPreference;
    }


    public static String getLocalization(Context context) {

        if (!isLocation(context)) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("NAME", Context.MODE_PRIVATE);
            String preferenceUnits = sharedPreferences.getString("Name", null);
            if (preferenceUnits == null){
                preferenceUnits = DOWNLOAD_CURRENT_LOCATION;
            }

            return preferenceUnits;// z SP
        } else  {
            return DOWNLOAD_CURRENT_LOCATION; //default
        }
    }

}
