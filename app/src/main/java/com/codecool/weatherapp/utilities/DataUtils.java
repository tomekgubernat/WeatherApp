package com.codecool.weatherapp.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DataUtils {

    public static final long SECOND_IN_MILI = 1000;
    public static final long MINUTE_IN_MILI = SECOND_IN_MILI * 60;
    public static final long HOUR_IN_MILI = MINUTE_IN_MILI * 60;
    public static final long DAY_IN_MILI = HOUR_IN_MILI * 24;

    public static String getDate(long miliSec) {

        DateFormat simple = new SimpleDateFormat("EEE");

        Date result = new Date(miliSec);

        return simple.format(result);
    }


    public static long getUTCDateWithTimeZone(long localDate) {
        TimeZone timeZone = TimeZone.getDefault();
        long offSet = timeZone.getOffset(localDate);
        return localDate + offSet;
    }
    public static long getUTCDateWithoutTimeZone(long localDate) {
        TimeZone timeZone = TimeZone.getDefault();
        long offSet = timeZone.getOffset(localDate);
        return localDate - offSet;
    }

    public static long getDayNumber(long date) {
        TimeZone tz = TimeZone.getDefault();
        long gmtOffset = tz.getOffset(date);
        return (date + gmtOffset) / DAY_IN_MILI;
    }


    public static String getStringDates(long miliSec) {



        long dateFromAPI = getUTCDateWithoutTimeZone(miliSec);
//        long dayNumberFromData = dateFromAPI / DAY_IN_MILLIS;
//        long currentDayNumber = System.currentTimeMillis() / DAY_IN_MILLIS;

        long dayNumberFromData = getDayNumber(dateFromAPI);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        String localizedDayName = new SimpleDateFormat(" | dd MMM").format(dateFromAPI);

        String dayName = getName(dateFromAPI);

        if (dayNumberFromData == currentDayNumber) {


            if (dayNumberFromData - currentDayNumber < 2) {
                return dayName + localizedDayName;
            } else {
                return localizedDayName;
            }

        }
        return dayName + localizedDayName;
    }



    private static String getName(long dateInMiliSec) {

//        long dayNumberFromData = dateInMiliSec/DAY_IN_MILLIS;
//        long currentDayNumber = System.currentTimeMillis()/DAY_IN_MILLIS;

        long dayNumberFromData = getDayNumber(dateInMiliSec);
        long currentDayNumber = getDayNumber(System.currentTimeMillis());

        if(dayNumberFromData == currentDayNumber) {
            return "Today";
        } else if (dayNumberFromData == currentDayNumber + 1) {
            return "Tomorrow";
        } else {
            DateFormat simple = new SimpleDateFormat("EEEE");
            Date result = new Date(dateInMiliSec);
            return simple.format(result);

        }
    }


    public static boolean isDateNormalized(long millisSinceEpoch) {
        boolean isDateNormalized = false;
        if (millisSinceEpoch % DAY_IN_MILI == 0) {
            isDateNormalized = true;
        }

        return isDateNormalized;
    }
}
