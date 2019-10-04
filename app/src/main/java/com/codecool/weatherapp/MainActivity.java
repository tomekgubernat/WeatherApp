package com.codecool.weatherapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codecool.weatherapp.data.Contract;
import com.codecool.weatherapp.utilities.DataUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity implements WeatherAdapter.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    private RecyclerView mRecyclerView;
    private WeatherAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public FragmentManager mFragmentManager;

    private FusedLocationProviderClient mClient;
    Geocoder mGeocoder;

    //EditText currentPositionLabel;
    TextView currentPositionLabel;
    TextView weatherLocation;

    private String City2;

    private TextInputLayout textInputCity;
    private Button confirmButton;
    private Button cancelButton;

    private static final Pattern CITY_PATTERN =
            Pattern.compile("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");

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
            Contract.Entry.COLUMN_CLOUDS,
    };

    public static final int INDEX_WEATHER_DATE = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 3;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_ICON_ID = 4;
    public static final int INDEX_WEATHER_HUMIDITY = 5;
    public static final int INDEX_WEATHER_PREASSURE = 6;
    public static final int INDEX_WEATHER_DESCRIPTION = 7;
    public static final int INDEX_WEATHER_CLOUDS = 8;

    private int mPosition = RecyclerView.NO_POSITION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getSupportActionBar().setElevation(0); //remove shadow and line below actionbar

        //sets icon on the action bar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.contrast);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



        mClient = LocationServices.getFusedLocationProviderClient(this);

        mGeocoder = new Geocoder(this, Locale.getDefault());

        //CurrentLoczalization.requestPermission(MainActivity.this, mClient, mGeocoder);


        final SharedPreferences SP = getApplicationContext().getSharedPreferences("NAME", 0);
        //Log.v("SPTEST", SP.getString("Name", null));


//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String location2 = sharedPreferences.getString("Name", null);
        //weatherLocation = findViewById(R.id.weather_location);
        //weatherLocation.setText("test");

        mFragmentManager = getSupportFragmentManager();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview_weather);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new WeatherAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mProgressIndicator = (ProgressBar) findViewById(R.id.progress_indicator);

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            } else {

            }
        }
    }

    public String requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);

        final String city2 = "Pekin";

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(this).setTitle("REQUIRED LOCATION PERMISSION").setMessage("You have to give this permission to acess the furure ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
                        }

                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Double latitude = location.getLatitude();
                        Double longitude = location.getLongitude();

                        List<Address> addressList = null;
                        try {
                            addressList = mGeocoder.getFromLocation(latitude, longitude,1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String city = addressList.get(0).getLocality();

                        City2 = city;

//                        SharedPrefSave(city);
//                        TextView weatherLocation = findViewById(R.id.weather_location);
//                        weatherLocation.setText(city);
                        //currentPositionLabel.setText(city);
                    }
                }
            });
        }  return City2;
    }

    public void openDialogBoxWithDetails(ArrayList datailData) {
        DetailDialogBox detailDialogBox = DetailDialogBox.newInstance(datailData);
        detailDialogBox.show(getSupportFragmentManager(), "details");
    }

    @Override
    public void onWeatherClick(Cursor weatherForDay) {

        String date = weatherForDay.getString(MainActivity.INDEX_WEATHER_DATE);
        String min = weatherForDay.getString(MainActivity.INDEX_WEATHER_MIN_TEMP);
        String max = weatherForDay.getString(MainActivity.INDEX_WEATHER_MAX_TEMP);
        String icon = weatherForDay.getString(MainActivity.INDEX_WEATHER_ICON_ID);
        String press = weatherForDay.getString(MainActivity.INDEX_WEATHER_PREASSURE);
        String hum = weatherForDay.getString(MainActivity.INDEX_WEATHER_HUMIDITY);
        String des = weatherForDay.getString(MainActivity.INDEX_WEATHER_DESCRIPTION);
        String cloud = weatherForDay.getString(MainActivity.INDEX_WEATHER_CLOUDS);

        ArrayList<String> WeatherDataArray = new ArrayList<>();

        WeatherDataArray.add(date);
        WeatherDataArray.add(min);
        WeatherDataArray.add(max);
        WeatherDataArray.add(icon);
        WeatherDataArray.add(press);
        WeatherDataArray.add(hum);
        WeatherDataArray.add(des);
        WeatherDataArray.add(cloud);

        openDialogBoxWithDetails(WeatherDataArray);
    }


    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case LOADER_ID:
                Uri QueryUri = Contract.Entry.CONTENT_URI;
                String sortOrder = Contract.Entry.COLUMN_DATE + " ASC";
                String selection = Contract.Entry.getSqlSelectForTodayOnwards();

                return new CursorLoader(this, QueryUri, mProjection, null, null, null);

            default:
                throw new RuntimeException("Loader not implemented " + loaderId);
        }
    }


    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        mSwipeRefreshLayout.setRefreshing(false);


        if (data.getCount() != 0) {
            mProgressIndicator.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void updateRefreshDate() {
        String updateTime = "Update: " + DataUtils.getStringHours();
        TextView mUpdate = (TextView) findViewById(R.id.weather_update);
        mUpdate.setText(updateTime);
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
        switch (item.getItemId()) {
            case R.id.settings:
                //Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Log.v("SH", sharedPreferences.getString("key_temp_units", ""));
                return true;

            case R.id.localization:
                //Toast.makeText(this, "Localization", Toast.LENGTH_SHORT).show();
                final AlertDialog.Builder mDialog = new AlertDialog.Builder(MainActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.localization_dialogbox, null);

                currentPositionLabel = (TextView) mView.findViewById(R.id.write);

                final ImageButton currentPosiotionButton = (ImageButton) mView.findViewById(R.id.select_current_position);
                //final TextView weatherLocation = findViewById(R.id.weather_location);

                final SharedPreferences SP = getApplicationContext().getSharedPreferences("NAME", 0);

                confirmButton = mView.findViewById(R.id.confirm_button);
                cancelButton = mView.findViewById(R.id.cancel_button);
                textInputCity = mView.findViewById(R.id.text_input_city);

                currentPosiotionButton.setEnabled(true);
                currentPosiotionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String s = CurrentLoczalization.requestPermission(MainActivity.this, mClient, mGeocoder);
                        //String s = requestPermission();
                        SharedPrefSave(s);
                        TextView weatherLocation = findViewById(R.id.weather_location);
                        weatherLocation.setText(s);

                        currentPositionLabel.setText(SP.getString("Name", null));
                        Preferences.setLocalization(SP.getString("Name", null));
                        SyncUtils.StarSync(MainActivity.this);
                        updateRefreshDate();
                    }
                });

                mDialog.setView(mView);
                final AlertDialog dialog = mDialog.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(!validateCity()){
                            return;
                        } else {
                            String input = textInputCity.getEditText().getText().toString();
                            SharedPrefSave(input);
                            Preferences.setLocalization(input);
                            //weatherLocation.setText(SP.getString("Name", null));
                            SyncUtils.StarSync(MainActivity.this);
                            updateRefreshDate();

                            dialog.cancel();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                currentPositionLabel.setText(SP.getString("Name", null));
                dialog.show();

                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }


    public void SharedPrefSave(String Name){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("NAME", 0);
        SharedPreferences.Editor prefEdit = pref.edit();
        prefEdit.putString("Name", Name);
        prefEdit.commit();
    }

    private boolean validateCity(){
        String cityInput = textInputCity.getEditText().getText().toString().trim();

//        if(cityInput.isEmpty()){
//            textInputCity.setError("Field can't be empty");
//            return false;
//        } else
            if (!CITY_PATTERN.matcher(cityInput).matches()) {
            textInputCity.setError("Password too week");
            return false;
        } else {
            textInputCity.setError(null);
            return true;
        }
    }



}


