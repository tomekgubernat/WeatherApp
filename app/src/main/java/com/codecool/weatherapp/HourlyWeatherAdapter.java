package com.codecool.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>{

    private ArrayList<String> mDegree;
    private ArrayList<String> mHour;
    private Context mContext;

    public HourlyWeatherAdapter(ArrayList<String> degree, ArrayList<String> hour, Context context) {
        mDegree = degree;
        mHour = hour;
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
        viewHolder.degree.setText(mDegree.get(i));
        viewHolder.hour.setText(mHour.get(i));
    }

    @Override
    public int getItemCount() {
        return mDegree.size();
        //return (null != mDegree ? mDegree.size() : 0);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView hour;
        TextView degree;

        public ViewHolder(View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            degree = itemView.findViewById(R.id.degree);
        }
    }
}
