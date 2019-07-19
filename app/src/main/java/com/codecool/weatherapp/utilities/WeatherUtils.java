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

//    public static int getWeatherIcon(int weatherId) {
//
//        if (weatherId >= 200 && weatherId <= 232) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 300 && weatherId <= 321) {
//            return R.drawable.ic_light_rain;
//        } else if (weatherId >= 500 && weatherId <= 504) {
//            return R.drawable.ic_rain;
//        } else if (weatherId == 511) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 520 && weatherId <= 531) {
//            return R.drawable.ic_rain;
//        } else if (weatherId >= 600 && weatherId <= 622) {
//            return R.drawable.ic_snow;
//        } else if (weatherId >= 701 && weatherId <= 761) {
//            return R.drawable.ic_fog;
//        } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
//            return R.drawable.ic_storm;
//        } else if (weatherId == 800) {
//            return R.drawable.ic_clear;
//        } else if (weatherId == 801) {
//            return R.drawable.ic_light_clouds;
//        } else if (weatherId >= 802 && weatherId <= 804) {
//            return R.drawable.ic_cloudy;
//        } else if (weatherId >= 900 && weatherId <= 906) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 958 && weatherId <= 962) {
//            return R.drawable.ic_storm;
//        } else if (weatherId >= 951 && weatherId <= 957) {
//            return R.drawable.ic_clear;
//        }
//
//        return R.drawable.ic_storm;
//    }

}
