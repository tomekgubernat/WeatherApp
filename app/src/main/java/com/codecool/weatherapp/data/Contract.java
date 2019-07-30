package com.codecool.weatherapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {

    public static final String CONTENT_AUTHORITY = "com.codecool.weatherapp.data";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";


    public static final class Entry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();

        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_ID = "weather_id";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_MIN = "min";
        public static final String COLUMN_MAX = "max";
        public static final String COLUMN_ICON_ID = "icon";
        public static final String COLUMN_HUMIDITY = "humidity";
        public static final String COLUMN_PRESSURE = "pressure";
        public static final String COLUMN_DESCRIPTION = "description";





        public static Uri buildWeatherUriWithDate(long date) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(date))
                    .build();
        }

        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = System.currentTimeMillis();
            return Contract.Entry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }
    }
}
