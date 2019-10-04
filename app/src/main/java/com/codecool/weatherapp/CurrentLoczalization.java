package com.codecool.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CurrentLoczalization {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    public static void setCity2(String city2) {
        City2 = city2;
    }

    public static String City2 = "Warszawa";

    public static String tomek = "Warszawa";


    public static String requestPermission(final AppCompatActivity context, FusedLocationProviderClient mClient, final Geocoder mGeocoder){
        ActivityCompat.requestPermissions(context, new String[]{ACCESS_FINE_LOCATION}, 1);

        //String City2 = "Pekin";

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                new AlertDialog.Builder(context).setTitle("REQUIRED LOCATION PERMISSION").setMessage("You have to give this permission to acess the furure ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(context,
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
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            mClient.getLastLocation().addOnSuccessListener(context, new OnSuccessListener<Location>() {
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

                        //City2 = city;
                        setCity2(city);
                        //tomek = city;

//                        SharedPrefSave(city);
//                        TextView weatherLocation = findViewById(R.id.weather_location);
//                        weatherLocation.setText(city);
                        //currentPositionLabel.setText(city);
                    }
                }
            });
        }  return City2;
    }
}
