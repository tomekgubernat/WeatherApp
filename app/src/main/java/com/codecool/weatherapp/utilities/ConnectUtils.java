package com.codecool.weatherapp.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class ConnectUtils {

    private static final String TAG = ConnectUtils.class.getSimpleName();

    private static String OPEN_WEATHER_API_KEY = "c29467bfa16601d499d65f8fea90da2e";

    final static String QUERY_PARAM = "q";
    final static String APPID_PARAM = "appid";


    public static URL buildWeatherUrl(String location) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast?")
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(APPID_PARAM, OPEN_WEATHER_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "FORECAST URL: " + url);
        return url;
    }

    public static URL buildImageUrl(String iconId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("www.openweathermap.org")
                .appendPath("img")
                .appendPath("wn")
                .appendPath(iconId + "@2x.png")
                .build();

        URL url = null;
        try {
            url = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "FORECAST ICON URI: " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream).useDelimiter("\\A"); //regex \A stands for :start of a string!

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}
