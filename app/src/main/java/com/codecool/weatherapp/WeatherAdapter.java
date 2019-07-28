package com.codecool.weatherapp;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
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

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.MyViewHolder> {

    final private OnClickListener mOnClickListener;
    private final Context mContext;
    // Create cursor.
    // Cursor is the Interface which represents a 2 dimensional table of any database. We don't need load all data into memor.
    private Cursor mCursor;

    public WeatherAdapter(Context context, OnClickListener onClickListener){
        mOnClickListener = onClickListener;
        mContext = context;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mWeatherDate;
        public final ImageView mWeatherIcon;
        public final TextView mWeatherTempHigh;
        public final TextView mWeatherTempLow;

        OnClickListener mOnClickListener;

        public MyViewHolder(View itemView, OnClickListener onClickListener) {
            super(itemView);
            mWeatherDate = (TextView) itemView.findViewById(R.id.weatherDate);
            mWeatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            mWeatherTempHigh = (TextView) itemView.findViewById(R.id.weatherTempHigh);
            mWeatherTempLow = (TextView) itemView.findViewById(R.id.weatherTempLow);

            this.mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String weatherForDay = mWeatherTempLow.getText().toString();
            mOnClickListener.onWeatherClick(weatherForDay);
        }
    }

    public interface OnClickListener {
        void onWeatherClick(String weatherForDay);
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.weather_list_item, viewGroup, false);
        view.setFocusable(true);

        return new MyViewHolder(view, mOnClickListener);
    }

    //Method it takes all of the data from a cursor and refill views.
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        mCursor.moveToPosition(i);

        //DATE//
        long dateInMillis = mCursor.getLong(MainActivity.INDEX_WEATHER_DATE);
        String data = mCursor.getString(mCursor.getColumnIndex(Contract.Entry.COLUMN_DATE));

        myViewHolder.mWeatherDate.setText(data);

        //ICON//
        String weatherId = mCursor.getString(MainActivity.INDEX_WEATHER_ICON_ID);
        URL weatherIcon = ConnectUtils.buildImageUrl(weatherId);
        Log.v("image", weatherIcon.toString() );

        Picasso.get().load(weatherIcon.toString()).into(myViewHolder.mWeatherIcon);

        //TEMPERATURE//

        //high
        double highInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MAX_TEMP);

        String tempHigh = WeatherUtils.formatTemperature(highInCelsius);

        myViewHolder.mWeatherTempHigh.setText(tempHigh);

        //low
        double lowInCelsius = mCursor.getDouble(MainActivity.INDEX_WEATHER_MIN_TEMP);

        String tempLow = WeatherUtils.formatTemperature(lowInCelsius);

        myViewHolder.mWeatherTempLow.setText(tempLow);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
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
