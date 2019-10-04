package com.codecool.weatherapp;

import android.content.Intent;

public class IntentService extends android.app.IntentService {

    public IntentService() {
        super("IntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SynchronizeTask.syncWeather(this);

    }
}
