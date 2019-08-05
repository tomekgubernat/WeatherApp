package com.codecool.weatherapp;

import android.content.Context;
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

import com.codecool.weatherapp.data.Contract;
import com.codecool.weatherapp.utilities.ConnectUtils;
import com.codecool.weatherapp.utilities.WeatherUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    final private OnClickListener mOnClickListener;
    private final Context mContext;
    // Create cursor.
    // Cursor is the Interface which represents a 2 dimensional table of any database. We don't need load all data into memor.
    private static Cursor mCursor;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_HOURLY = 2;

    private static ArrayList<String> mDegree = new ArrayList<>();


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


        public RecyclerView mRecyclerView;

        OnClickListener mOnClickListener;

        //, OnClickListener onClickListener

        public MyViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mWeatherDate = (TextView) itemView.findViewById(R.id.weatherDate);
            mWeatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            mWeatherTempHigh = (TextView) itemView.findViewById(R.id.weatherTempHigh);
            mWeatherTempLow = (TextView) itemView.findViewById(R.id.weatherTempLow);
            mWeatherTemp = (TextView) itemView.findViewById(R.id.weatherTemp);

            this.mRecyclerView = (RecyclerView) itemView.findViewById(R.id.recycleview_hourly_weather);

            this.mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);

        }
            public void bindViewSecondList(int pos) {

                pos = pos - 1;


                mCursor.moveToPosition(pos);

                //DATE//
                long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
                String data = mCursor.getString(mCursor.getColumnIndex(Contract.Entry.COLUMN_DATE));

                mWeatherDate.setText(data);

                //ICON//
                String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
                URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
                Log.v("image", weatherIcon.toString() );

                Picasso.get().load(weatherIcon.toString()).into(mWeatherIcon);

                //TEMPERATURE//

                //high
                double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);

                String tempHigh = WeatherUtils.formatTemperature(highInCelsius);

                mWeatherTempHigh.setText(tempHigh);

                mWeatherTemp.setText(tempHigh);


                //low
                double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

                String tempLow = WeatherUtils.formatTemperature(lowInCelsius);

                mWeatherTempLow.setText(tempLow);

            }

            public void bindViewFirstList(int pos) {


                mCursor.moveToPosition(pos);

                //DATE//
                long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
                String data = mCursor.getString(mCursor.getColumnIndex(Contract.Entry.COLUMN_DATE));

                mWeatherDate.setText(data);

                //ICON//
                String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
                URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
                Log.v("image", weatherIcon.toString() );

                Picasso.get().load(weatherIcon.toString()).into(mWeatherIcon);

                //TEMPERATURE//

                //high
                double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);

                String tempHigh = WeatherUtils.formatTemperature(highInCelsius);

                mWeatherTempHigh.setText(tempHigh);

                mWeatherTemp.setText(tempHigh);


                //low
                double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

                String tempLow = WeatherUtils.formatTemperature(lowInCelsius);

                mWeatherTempLow.setText(tempLow);
            }

        public ArrayList<String> bindViewHourly(int pos) {

            //private ArrayList<String> mHour =  new ArrayList<>();

            pos = pos - 1;

            mCursor.moveToPosition(pos);


            //TEMPERATURE//

            //high
            double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);

            String tempHigh = WeatherUtils.formatTemperature(highInCelsius);

            mDegree.add(tempHigh);

            return mDegree;
        }







        @Override
        public void onClick(View v) {
//            String weatherForDay = mWeatherTempLow.getText().toString();
//            mOnClickListener.onWeatherClick(weatherForDay);

            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);

            //long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
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

//        int layoutId;
//
//        switch (i) {
//
//            case VIEW_TYPE_TODAY: {
//                layoutId = R.layout.weather_list_first;
//                break;
//            }
//
//            case VIEW_TYPE_FUTURE_DAY: {
//                layoutId = R.layout.weather_list_item;
//                break;
//            }
//
//            default:
//                throw new IllegalArgumentException("Invalid view type, value of " + i);
//        }
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(layoutId, viewGroup, false);
//        view.setFocusable(true);



        View v;

        if (i == VIEW_TYPE_TODAY) {
            v = LayoutInflater.from(mContext).inflate(R.layout.weather_list_first, viewGroup, false);
            FirstViewHolder vh = new FirstViewHolder(v, mOnClickListener);
            return vh;
        } else if (i == VIEW_TYPE_HOURLY) {
            v = LayoutInflater.from(mContext).inflate(R.layout.hourly, viewGroup, false);
            HourlyViewHolder vh = new HourlyViewHolder(v, mOnClickListener);
            return vh;

        } else {
            v = LayoutInflater.from(mContext).inflate(R.layout.weather_list_item, viewGroup, false);
            SeconfdViewHolder vh = new SeconfdViewHolder(v, mOnClickListener);
            return vh;
        }

//        } else {
//            v = LayoutInflater.from(mContext).inflate(R.layout.hourly_weather_item, viewGroup, false);
//            HourlyViewHolder vh = new HourlyViewHolder(v, mOnClickListener);
//            return vh;
//        }


        //return new MyViewHolder(view, mOnClickListener);




    }

    //private ArrayList<String> mDegree = new ArrayList<>();
    private ArrayList<String> mHour =  new ArrayList<>();

    private void geDate(){
//        mDegree.add("31");
//        mDegree.add("27");
//        mDegree.add("22");
//        mDegree.add("18");
//        mDegree.add("15");
//        mDegree.add("13");
//        mDegree.add("12");

        mHour.add("06:00");
        mHour.add("09:00");
        mHour.add("12:00");
        mHour.add("15:00");
        mHour.add("18:00");
        mHour.add("21:00");
        mHour.add("24:00");

    }

    //Method it takes all of the data from a cursor and refill views.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//        mCursor.moveToPosition(i);
//
//
//
//        //DATE//
//        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
//        String data = mCursor.getString(mCursor.getColumnIndex(Contract.Entry.COLUMN_DATE));
//
//        myViewHolder.mWeatherDate.setText(data);
//
//        //ICON//
//        String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
//        URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
//        Log.v("image", weatherIcon.toString() );
//
//        Picasso.get().load(weatherIcon.toString()).into(myViewHolder.mWeatherIcon);
//
//        //TEMPERATURE//
//
//        //high
//        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);
//
//        String tempHigh = WeatherUtils.formatTemperature(highInCelsius);
//
//        myViewHolder.mWeatherTempHigh.setText(tempHigh);
//
//        myViewHolder.mWeatherTemp.setText(tempHigh);
//
//
//        //low
//        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);
//
//        String tempLow = WeatherUtils.formatTemperature(lowInCelsius);
//
//        myViewHolder.mWeatherTempLow.setText(tempLow);

//        geDate();
//
//        HourlyWeatherAdapter itemListDataAdapter = new HourlyWeatherAdapter(mDegree,mHour,mContext);
//        myViewHolder.mRecyclerView.setHasFixedSize(true);
//        myViewHolder.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
//        myViewHolder.mRecyclerView.setAdapter(itemListDataAdapter);


        try {
            if (myViewHolder instanceof FirstViewHolder) {
                FirstViewHolder vh = (FirstViewHolder) myViewHolder;
                vh.bindViewFirstList(i);

            } else if (myViewHolder instanceof SeconfdViewHolder) {
                SeconfdViewHolder vh = (SeconfdViewHolder) myViewHolder;
                vh.bindViewSecondList(i);

            } else if (myViewHolder instanceof HourlyViewHolder) {
                HourlyViewHolder vh = (HourlyViewHolder) myViewHolder;
                ArrayList<String> mDegree = vh.bindViewHourly(i);

                geDate();

                Log.v("TEST", String.valueOf(mDegree));
                HourlyWeatherAdapter itemListDataAdapter = new HourlyWeatherAdapter(mDegree,mHour,mContext);
                vh.mRecyclerView.setHasFixedSize(true);
                vh.mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
                vh.mRecyclerView.setAdapter(itemListDataAdapter);

                mCursor.moveToPosition(i);
                double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);


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
            listSize = mCursor.getCount();

        if(listSize > 0)
            return 1 + listSize;
        else return 0;



        //return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
//        if (mUseTodayLayout && position == 0) {
//            return VIEW_TYPE_TODAY;
//        } else {
//            return VIEW_TYPE_FUTURE_DAY;
//        }

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
