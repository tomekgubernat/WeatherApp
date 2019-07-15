package com.codecool.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private String[] mWeatherData;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.newTextViewName);
        }
    }


    public WeatherAdapter(){
        //mWeatherData = myWeatherData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_list_item, viewGroup, false);

        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mTextView.setText(mWeatherData[i]);
    }

    @Override
    public int getItemCount() {
        if (null == mWeatherData) return 0;
        return mWeatherData.length;
    }


    public void setWeatherData(String[] weatherData){
        mWeatherData = weatherData;
        notifyDataSetChanged();

    }


}
