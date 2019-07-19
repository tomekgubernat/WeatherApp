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


    private static final String WEATHER_URL_ = "http://api.openweathermap.org/data/2.5/forecast?q=London,uk&APPID=c29467bfa16601d499d65f8fea90da2e";

    private static final String WEATHER_ICON_URL_ = "http://openweathermap.org/img/wn/";



    //private static String apiKey = "c29467bfa16601d499d65f8fea90da2e";

    //final static String QUERY_PARAM = "q";
    //final static String APPID_PARAM = "&appid=";


    public static URL buildUrl(String locationQuery) {
        Uri builtUri = Uri.parse(WEATHER_URL_).buildUpon()
                //.appendQueryParameter(QUERY_PARAM, locationQuery)
                //.appendQueryParameter(APPID_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI" + url);

        return url;
    }

    public static URL buildUrlImage(String iconId) {
        Uri builtUri = Uri.parse(WEATHER_ICON_URL_).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built Icon URI" + url + iconId + "@2x.png");

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

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
