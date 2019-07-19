package com.codecool.weatherapp;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codecool.weatherapp.utilities.ConnectUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.OnClickListener, LoaderManager.LoaderCallbacks<String[]> {

    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String[] simpleJsonWeatherData;

    public FragmentManager fm;
    public Fragment fragment;

    public static final int LOADER_ID = 10;

    SwipeRefreshLayout mSwipeRefreshLayout;

    ProgressBar mProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);



        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_weather);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new WeatherAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        LoaderManager.LoaderCallbacks<String[]> callback = MainActivity.this;

        Bundle bundleForLoader = null;

        getSupportLoaderManager().initLoader(LOADER_ID, bundleForLoader, callback);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        mProgressIndicator = (ProgressBar) findViewById(R.id.progressindicator);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                invalidData();
                getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                mProgressIndicator.setVisibility(View.INVISIBLE);


            }
        });




        //loadAllWeatherData();



    }


    @Override
    public void onWeatherClick(int position) {
        String test = simpleJsonWeatherData[position];
        Toast.makeText(this, test + position, Toast.LENGTH_SHORT).show();

        if(fragment == null) {
            fragment = new DetailsWeatherFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

//    private void loadAllWeatherData(){
//        mRecyclerView.setVisibility(View.VISIBLE);
//
//        String location = Preferences.getPreferredWeatherLocation(this);
//        new FetchWeatherTask().execute(location);
//
//
//    }

    private void invalidData(){
        mAdapter.setWeatherData(null);
    }


    @NonNull
    @Override
    public Loader<String[]> onCreateLoader(int i, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<String[]>(this) {

            String[] mData;

            @Override
            protected void onStartLoading() {
                if (mData != null) {
                    deliverResult(mData);
                } else {
                    mProgressIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Nullable
            @Override
            public String[] loadInBackground() {

                String location = Preferences.getPreferredWeatherLocation(MainActivity.this);
                URL weatherRequestUrl = ConnectUtils.buildUrl(location);


                try{
                    String jsonWeatherResponse = ConnectUtils.getResponseFromHttpUrl(weatherRequestUrl);

                    simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                    return simpleJsonWeatherData;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult (String[] dt){
                mData = dt;
                super.deliverResult(dt);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String[]> loader, String[] strings) {
        mProgressIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);

        if (strings != null) {

            mAdapter.setWeatherData(strings);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String[]> loader) {

    }


//    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {
//
//        @Override
//        protected String[] doInBackground(String... params) {
//
//            String location = params[0];
//            URL weatherRequestUrl = ConnectUtils.buildUrl(location);
//
//
//            try{
//                String jsonWeatherResponse = ConnectUtils.getResponseFromHttpUrl(weatherRequestUrl);
//
//                simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);
//
//                return simpleJsonWeatherData;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String[] strings) {
//            //super.onPostExecute(strings);
//            mRecyclerView.setVisibility(View.VISIBLE);
//
//            if (strings != null) {
//
//                mAdapter.setWeatherData(strings);
//            }
//            }
//
//
//        }
    }


