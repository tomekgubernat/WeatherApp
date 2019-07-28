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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codecool.weatherapp.data.Contract;

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
    };

    public static final int INDEX_WEATHER_DATE = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 3;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_ICON_ID = 4;


    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

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
                getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);
                mProgressIndicator.setVisibility(View.INVISIBLE);
            }
        });

        mProgressIndicator.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        SyncUtils.initialize(this);
    }


    public void openDialog(String datailData){
        DetailDialogBox detailDialogBox = DetailDialogBox.newInstance(datailData);
        detailDialogBox.show(getSupportFragmentManager(), "test");
    }

    @Override
    public void onWeatherClick(String weatherForDay) {
        Toast.makeText(this, weatherForDay, Toast.LENGTH_SHORT).show();

        openDialog(weatherForDay);


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

            if(data.getCount() != 0) {
                mProgressIndicator.setVisibility(View.INVISIBLE);
                mRecyclerView.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

}


