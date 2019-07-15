package com.codecool.weatherapp.utilities;

public class WeatherUtils {

    private static double kelvinToCelsius(double tempInKelvin) {

        double tempInCelsius = tempInKelvin - 273.15;
        return tempInCelsius;
    }

    public static String formatHighLows(double high, double low) {

        double formattedHigh = kelvinToCelsius(high);
        double formattedLow = kelvinToCelsius(low);

        String roundedHigh = String.valueOf(Math.round(formattedHigh * 100)/100);
        String roundedLow = String.valueOf(Math.round(formattedLow * 100)/ 100);

        String highLowStr = roundedHigh + "°C" + " / " + roundedLow + "°C";
        return highLowStr;
    }
}
