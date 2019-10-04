package com.codecool.weatherapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecool.weatherapp.utilities.ConnectUtils;
import com.codecool.weatherapp.utilities.DataUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class DetailDialogBox extends DialogFragment {

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_ICON_ID = 3;
    public static final int INDEX_WEATHER_PREASSURE = 4;
    public static final int INDEX_WEATHER_HUMIDITY = 5;
    public static final int INDEX_WEATHER_DESCRIPTION = 6;
    public static final int INDEX_WEATHER_CLOUDS = 7;

    private static final String ARG_DATE = "date";
    //private Uri mUri;

    public static DetailDialogBox newInstance(ArrayList data) {
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_DATE, data);

        DetailDialogBox fragment = new DetailDialogBox();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder mDialog = new AlertDialog.Builder(getActivity());
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.daily_details_dialogbox,null);

        mDialog.setView(v);
        final AlertDialog detailsDialog = mDialog.create();
        detailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ArrayList<String> WeatherDataArray = getArguments().getStringArrayList(ARG_DATE);

        TextView detailsDate = (TextView) v.findViewById(R.id.details_date);
        long dateInMillis = Long.parseLong(WeatherDataArray.get(INDEX_WEATHER_DATE));
        String readableDate = DataUtils.getReadablyStringDates(dateInMillis);
        detailsDate.setText(readableDate);

        ImageView detailsIcon = (ImageView) v.findViewById(R.id.details_icon);
        String icon = WeatherDataArray.get(INDEX_WEATHER_ICON_ID);
        URL weatherIcon = ConnectUtils.buildImageUrl(icon);
        Picasso.get().load(weatherIcon.toString()).into(detailsIcon);

        TextView detailsPressure = (TextView) v.findViewById(R.id.details_pressure);
        String formatPressure = WeatherDataArray.get(INDEX_WEATHER_PREASSURE) + " hPa";
        detailsPressure.setText(formatPressure);

        TextView detailsHumidity = (TextView) v.findViewById(R.id.details_humidity);
        String formatHumidity = WeatherDataArray.get(INDEX_WEATHER_HUMIDITY) + " %";
        detailsHumidity.setText(formatHumidity);

        TextView detailsDescription = (TextView) v.findViewById(R.id.details_description);
        detailsDescription.setText(WeatherDataArray.get(INDEX_WEATHER_DESCRIPTION));

        TextView detailsCloudy = (TextView) v.findViewById(R.id.details_cloudy);
        String formatCloudy = WeatherDataArray.get(INDEX_WEATHER_CLOUDS) + " %";
        detailsCloudy.setText(formatCloudy);

        return detailsDialog;
    }
}
