package com.codecool.weatherapp.utilities;

public class WeatherUtils {

    private static double kelvinToCelsius(double tempInKelvin) {

        double tempInCelsius = tempInKelvin - 273.15;
        return tempInCelsius;
    }

    public static String formatTemperature (double temp) {

        double formattedToCelsius = kelvinToCelsius(temp);

        String roundedTemp = String.valueOf(Math.round(formattedToCelsius * 100)/100);

        String celsiusTemp = roundedTemp + "°C";
        return celsiusTemp;
    }

}
