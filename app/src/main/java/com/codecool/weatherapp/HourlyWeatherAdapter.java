package com.codecool.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecool.weatherapp.utilities.ConnectUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>{

    private ArrayList<String> mDegreeList;
    private ArrayList<String> mHourList;
    private ArrayList<String> mHourIconList;
    private Context mContext;

    public HourlyWeatherAdapter(ArrayList<String> degreeList, ArrayList<String> hourList, ArrayList<String> hourIconList, Context context) {
        mDegreeList = degreeList;
        mHourList = hourList;
        mHourIconList = hourIconList;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hourly_weather_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.degree.setText(mDegreeList.get(i));
        viewHolder.hour.setText(mHourList.get(i));

        URL weatherIcon = ConnectUtils.buildImageUrl(mHourIconList.get(i));
        Picasso.get().load(weatherIcon.toString()).into(viewHolder.hour_icon);
    }

    @Override
    public int getItemCount() {
        return (null != mDegreeList ? mDegreeList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView hour;
        TextView degree;
        ImageView hour_icon;

        public ViewHolder(View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.forecast_hour);
            degree = itemView.findViewById(R.id.forecast_degree);
            hour_icon = itemView.findViewById(R.id.forecast_hour_icon);
        }
    }
}
