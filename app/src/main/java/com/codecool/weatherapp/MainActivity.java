package com.codecool.weatherapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.codecool.weatherapp.utilities.ConnectUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mWeatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWeatherTextView = (TextView) findViewById(R.id.weather_data);

        loadAllWeatherData();
    }

    private void loadAllWeatherData(){
        String location = Preferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String location = params[0];
            URL weatherRequestUrl = ConnectUtils.buildUrl(location);


            try{
                String jsonWeatherResponse = ConnectUtils.getResponseFromHttpUrl(weatherRequestUrl);

                String simpleJsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String strings) {
            //super.onPostExecute(strings);

//            if (strings != null) {
//                for (String weatherString : strings) {
//                    mWeatherTextView.append((weatherString) + "\n\n\n");
//                }
//            }

            mWeatherTextView.append((strings) + "\n\n\n");

        }
    }

}
