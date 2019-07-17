package com.codecool.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    private String[] mWeatherData;
    private OnClickListener mOnClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mTextView;
        OnClickListener mOnClickListener;

        public MyViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.newTextViewName);
            this.mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onWeatherClick(getAdapterPosition());
        }


    }

    public interface OnClickListener {
        void onWeatherClick(int position);
    }

    public WeatherAdapter(OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
        //mWeatherData = myWeatherData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_list_item, viewGroup, false);

        MyViewHolder vh = new MyViewHolder(view, mOnClickListener);
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
