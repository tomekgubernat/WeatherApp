package com.codecool.weatherapp.utilities;

import android.content.Context;

import com.codecool.weatherapp.Preferences;

public class WeatherUtils {

    private static double kelvinToCelsius(double tempInKelvin) {

        double tempInCelsius = tempInKelvin - 273.15;
        return tempInCelsius;
    }

    private static double kelvinToFahrenheit(double tempInKelvin) {

        double tempInFahrenheit = (tempInKelvin * 9/5) - 459.67;
        return tempInFahrenheit;
    }

    public static String formatTemperature (Context context, double temp) {

        if (!Preferences.isCelsius(context)) {
            double formattedToFahrenheit = kelvinToFahrenheit(temp);
            String roundedTemp = String.valueOf(Math.round(formattedToFahrenheit * 100)/100);
            return roundedTemp + "°K";
        } else  {
            double formattedToCelsius = kelvinToCelsius(temp);
            String roundedTemp = String.valueOf(Math.round(formattedToCelsius * 100)/100);
            return roundedTemp + "°C";
        }


        //if (!Preferences.isMetric(context))

//            double formattedToCelsius = kelvinToCelsius(temp);
//
//        String roundedTemp = String.valueOf(Math.round(formattedToCelsius * 100)/100);
//
//        String celsiusTemp = roundedTemp + "°C";
//        return celsiusTemp;
    }





}
