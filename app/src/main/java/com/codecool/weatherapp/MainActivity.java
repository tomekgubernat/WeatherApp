package com.codecool.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.codecool.weatherapp.utilities.ConnectUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.OnClickListener {

    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    String[] simpleJsonWeatherData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);


        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_weather);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new WeatherAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        loadAllWeatherData();

    }

    private void loadAllWeatherData(){
        mRecyclerView.setVisibility(View.VISIBLE);

        String location = Preferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    @Override
    public void onWeatherClick(int position) {
        String test = simpleJsonWeatherData[position];
        Toast.makeText(this, test + position, Toast.LENGTH_SHORT).show();
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            String location = params[0];
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

        @Override
        protected void onPostExecute(String[] strings) {
            //super.onPostExecute(strings);
            mRecyclerView.setVisibility(View.VISIBLE);

            if (strings != null) {

                mAdapter.setWeatherData(strings);
                }
            }


        }
    }


