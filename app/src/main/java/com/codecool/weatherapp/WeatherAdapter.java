package com.codecool.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codecool.weatherapp.utilities.ConnectUtils;
import com.codecool.weatherapp.utilities.DataUtils;
import com.codecool.weatherapp.utilities.WeatherUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    final private OnClickListener mOnClickListener;
    private final Context mContext;
    // Create cursor.
    // Cursor is the Interface which represents a 2 dimensional table of any database. We don't need load all data into memory.
    private static Cursor mCursor;
    private static SharedPreferences sharedPreferences;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_HOURLY = 2;

    private static ArrayList<String> mDegreesList = new ArrayList<>(7);
    private static ArrayList<String> mHoursList = new ArrayList<>();
    private static ArrayList<String> mIconList = new ArrayList<>();
    
    private boolean mUseTodayLayout;

    public WeatherAdapter(Context context, OnClickListener onClickListener){
        mOnClickListener = onClickListener;
        mContext = context;
        mUseTodayLayout = true;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mWeatherDate;
        public final ImageView mWeatherIcon;
        public final TextView mWeatherTempHigh;
        public final TextView mWeatherTempLow;
        public final TextView mWeatherTemp;
        public final TextView mWeatherLocation;
        public final TextView mWeatherDescription;

        public RecyclerView mRecyclerView;
        OnClickListener mOnClickListener;

        //, OnClickListener onClickListener
        public MyViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mWeatherDate = (TextView) itemView.findViewById(R.id.weather_date);
            mWeatherIcon = (ImageView) itemView.findViewById(R.id.weather_icon);
            mWeatherTempHigh = (TextView) itemView.findViewById(R.id.weather_temp_high);
            mWeatherTempLow = (TextView) itemView.findViewById(R.id.weather_temp_low);
            mWeatherTemp = (TextView) itemView.findViewById(R.id.weather_temp);
            mWeatherLocation = (TextView) itemView.findViewById(R.id.weather_location);
            mWeatherDescription = (TextView) itemView.findViewById(R.id.weather_description);

            this.mRecyclerView = (RecyclerView) itemView.findViewById(R.id.horizontal_recycleview_hourly_weather);
            this.mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        public void bindViewFirstList(int pos, Context context) {

            mCursor.moveToPosition(pos);

            //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            //String loc = sharedPreferences.getString("NAME", null);
            //Log.v("loca", loc);
            String test = Preferences.getLocalization(context);
            mWeatherLocation.setText(test);

            //DATE//
            long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
            String datafinal = DataUtils.getReadablyStringDates(dateInMillis);
            mWeatherDate.setText(datafinal);

            //ICON//
            String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
            URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
            Log.v("image", weatherIcon.toString() );
            Picasso.get().load(weatherIcon.toString()).into(mWeatherIcon);

            //TEMPERATURE//
            //high
            double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
            String tempHigh = WeatherUtils.formatTemperature(context, highInCelsius);
            mWeatherTempHigh.setText(tempHigh);
            mWeatherTemp.setText(tempHigh);

            //low
            double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
            String tempLow = WeatherUtils.formatTemperature(context, lowInCelsius);
            mWeatherTempLow.setText(tempLow);

            //DESCRIPTION//
            String description = mCursor.getString(MainActivity.INDEX_WEATHER_DESCRIPTION);
            mWeatherDescription.setText(description);

//            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//            String location2 = sharedPreferences.getString("Name", null);
//            Log.v("YY", location2);
//            mWeatherLocation.setText(location2);
        }


            public void bindViewSecondList(int pos, Context context) {

                pos = pos-1;
                mCursor.moveToPosition(pos*8);

                //DATE//
                long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
                String datafinal = DataUtils.getReadablyStringDates(dateInMillis);
                mWeatherDate.setText(datafinal);

                //ICON//
                String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
                URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
                Log.v("image", weatherIcon.toString());
                Picasso.get().load(weatherIcon.toString()).into(mWeatherIcon);

                //TEMPERATURE//
                //high
                double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
                String tempHigh = WeatherUtils.formatTemperature(context, highInCelsius);
                mWeatherTempHigh.setText(tempHigh);
                mWeatherTemp.setText(tempHigh);

                //low
                double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
                String tempLow = WeatherUtils.formatTemperature(context, lowInCelsius);
                mWeatherTempLow.setText(tempLow);

            }


        public ArrayList<String> bindViewHourly(Context context) {

            for (int p = 1; p < 8; p++){
                mCursor.moveToPosition(p);

                //TEMPERATURE//
                double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
                String hourlyTemp = WeatherUtils.formatTemperature(context, highInCelsius);

                //HOURS//
                long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
                String hours = DataUtils.getDate(dateInMillis);

                //ICON//
                String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);


                if (mDegreesList.size() < 8) {
                    mDegreesList.add(hourlyTemp);
                    mHoursList.add(hours);
                    mIconList.add(weatherId);
                }
            }
            return mDegreesList;
        }


        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();//-1
            mCursor.moveToPosition(adapterPosition*8);
            mOnClickListener.onWeatherClick(mCursor);
        }
    }

    public interface OnClickListener {
        void onWeatherClick(Cursor weatherForDay);
    }

    private class FirstViewHolder extends MyViewHolder {
        public FirstViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView, onClickListener);
        }
    }

    private class SeconfdViewHolder extends MyViewHolder {
        public SeconfdViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView, onClickListener);
        }
    }

    private class HourlyViewHolder extends MyViewHolder {
        public HourlyViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView, onClickListener);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v;

        if (i == VIEW_TYPE_TODAY) {
            v = LayoutInflater.from(mContext).inflate(R.layout.weather_list_first_item, viewGroup, false);
            FirstViewHolder vh = new FirstViewHolder(v, mOnClickListener);
            return vh;
        } else if (i == VIEW_TYPE_HOURLY) {
            v = LayoutInflater.from(mContext).inflate(R.layout.horizontal_recyclerview, viewGroup, false);
            HourlyViewHolder vh = new HourlyViewHolder(v, mOnClickListener);
            return vh;

        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.weather_list_next_item, viewGroup, false);
            SeconfdViewHolder vh = new SeconfdViewHolder(v, mOnClickListener);
            return vh;
        }
    }

    //Method it takes all of the data from a cursor and refill views.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        try {
            if (myViewHolder instanceof FirstViewHolder) {
                FirstViewHolder vh = (FirstViewHolder) myViewHolder;
                vh.bindViewFirstList(i, mContext);

            } else if (myViewHolder instanceof SeconfdViewHolder) {
                SeconfdViewHolder vh = (SeconfdViewHolder) myViewHolder;
                vh.bindViewSecondList(i, mContext);

            } else if (myViewHolder instanceof HourlyViewHolder) {
                HourlyViewHolder vh = (HourlyViewHolder) myViewHolder;
                ArrayList<String> mDegree = vh.bindViewHourly(mContext);

                Log.v("TEST", String.valueOf(mDegree));
                HourlyWeatherAdapter itemListDataAdapter = new HourlyWeatherAdapter(mDegree, mHoursList, mIconList, mContext);
                vh.mRecyclerView.setHasFixedSize(true);
                vh.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
                vh.mRecyclerView.setAdapter(itemListDataAdapter);

                mCursor.moveToPosition(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {

        int listSize = 0;
        if (null == mCursor) return 0;

        if(mCursor != null)
            listSize = mCursor.getCount()/8;

        if(listSize > 0)
            return 1 + listSize;
        else return 0;

        //return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (mUseTodayLayout && position == 0) {
            return VIEW_TYPE_TODAY;
        } else if(position == 1){
            return VIEW_TYPE_HOURLY;
        } else {
            return VIEW_TYPE_FUTURE_DAY;
        }
    }


    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

//    public void swapCursor(Cursor newCursor){
//        if(mCursor != null){
//            mCursor.close();
//        }
//
//        mCursor = newCursor;
//        if(newCursor != null){
//            notifyDataSetChanged();
//        }
//    }
}
