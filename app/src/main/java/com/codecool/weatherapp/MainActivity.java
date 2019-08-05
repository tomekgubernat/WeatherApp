package com.codecool.weatherapp;

import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codecool.weatherapp.data.Contract;
import com.codecool.weatherapp.utilities.DataUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    public FragmentManager mFragmentManager;



    public static final int LOADER_ID = 10;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ProgressBar mProgressIndicator;

    //Array contains name of column from ContentProvider.
    // A "projection" defines the columns that will be returned for each row

    public static final String[] mProjection = {
            Contract.Entry.COLUMN_DATE,
            Contract.Entry.COLUMN_MIN,
            Contract.Entry.COLUMN_MAX,
            Contract.Entry.COLUMN_ICON_ID,
            Contract.Entry.COLUMN_HUMIDITY,
            Contract.Entry.COLUMN_PRESSURE,
            Contract.Entry.COLUMN_DESCRIPTION,



    };

    public static final int INDEX_WEATHER_DATE = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 3;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_ICON_ID = 4;
    public static final int INDEX_WEATHER_HUMIDITY = 5;
    public static final int INDEX_WEATHER_PREASSURE = 6;
    public static final int INDEX_WEATHER_DESCRIPTION = 7;




    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setElevation(0); //remove shadow and line below actionbar


        mFragmentManager = getSupportFragmentManager();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_weather);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new WeatherAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progressindicator);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                SyncUtils.StarSync(MainActivity.this);
                updateRefreshDate();
                mProgressIndicator.setVisibility(View.INVISIBLE);
            }
        });

        mProgressIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        SyncUtils.initialize(this);


        //geDate();


    }


    public void openDialog(ArrayList datailData){
        DetailDialogBox detailDialogBox = DetailDialogBox.newInstance(datailData);
        detailDialogBox.show(getSupportFragmentManager(), "test");
    }

    @Override
    public void onWeatherClick(Cursor weatherForDay) {
        //Toast.makeText(this, weatherForDay, Toast.LENGTH_SHORT).show();

        //Uri uriForDateClicked = Contract.Entry.buildWeatherUriWithDate(weatherForDay);

        //Cursor cursor = weatherForDay;

        String date = weatherForDay.getString(MainActivity.INDEX_WEATHER_DATE);
        String min = weatherForDay.getString(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String max = weatherForDay.getString(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String icon = weatherForDay.getString(MainActivity.INDEX_WEATHER_ICON_ID);
        String press = weatherForDay.getString(MainActivity.INDEX_WEATHER_PREASSURE);
        String hum = weatherForDay.getString(MainActivity.INDEX_WEATHER_HUMIDITY);
        String des = weatherForDay.getString(MainActivity.INDEX_WEATHER_DESCRIPTION);

        ArrayList<String> test = new ArrayList<>();

        test.add(date);
        test.add(min);
        test.add(max);
        test.add(icon);
        test.add(press);
        test.add(hum);
        test.add(des);


        openDialog(test);


    }




    public Loader<Cursor> onCreateLoader(int loaderid, Bundle bundle) {
        switch (loaderid) {
            case LOADER_ID:
                Uri QueryUri = Contract.Entry.CONTENT_URI;
                String sortOrder = Contract.Entry.COLUMN_DATE + " ASC";
                String selection = Contract.Entry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this, QueryUri, mProjection, null, null, null);

            default:
                    throw new RuntimeException("Loader not implemented " + loaderid);
        }
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);
            mSwipeRefreshLayout.setRefreshing(false);



        if(data.getCount() != 0) {
                mProgressIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);



        }
    }


    public void updateRefreshDate (){

        String summary = "Update: " + DataUtils.getStringHours();
        TextView mUpdate = (TextView) findViewById(R.id.weather_update);
        mUpdate.setText(summary);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.weather_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.localization:
                Toast.makeText(this, "Localization", Toast.LENGTH_SHORT).show();
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }


//    private ArrayList<String> mDegree = new ArrayList<>();
//    private ArrayList<String> mHour =  new ArrayList<>();
//
//    private void geDate(){
//        mDegree.add("31");
//        mDegree.add("27");
//        mDegree.add("22");
//        mDegree.add("18");
//        mDegree.add("15");
//        mDegree.add("13");
//        mDegree.add("12");
//
//        mHour.add("06:00");
//        mHour.add("09:00");
//        mHour.add("12:00");
//        mHour.add("15:00");
//        mHour.add("18:00");
//        mHour.add("21:00");
//        mHour.add("24:00");
//
//
//        initRecyclerView();
//    }
//
//    private void initRecyclerView(){
//
//        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        RecyclerView mRecyclerView2 = (RecyclerView) findViewById(R.id.recycleview_hourly_weather);
//        mRecyclerView2.setLayoutManager(mLayoutManager2);
//        HourlyWeatherAdapter mAdapter2 = new HourlyWeatherAdapter(mDegree, mHour, this);
//        mRecyclerView2.setAdapter(mAdapter2);
//    }

}


