package com.codecool.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.codecool.weatherapp.data.Contract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

public class SyncUtils {

    private static boolean sInit;

    private static final int SYNC_INTERVAL_HOURS = 3;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static final String SUNSHINE_SYNC_TAG = "sunshine-sync";


    static void FirebaseJobDispatcher(@NonNull final Context context) {
        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job syncJob = dispatcher.newJobBuilder().setService(FirebaseJobService.class).setTag(SUNSHINE_SYNC_TAG)
                .setConstraints(Constraint.ON_ANY_NETWORK).setLifetime(Lifetime.FOREVER).setRecurring(true)
                .setTrigger(Trigger.executionWindow(SYNC_INTERVAL_SECONDS, SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(syncJob);
    }


    public static void StarSync(@NonNull final Context context) {

        Intent intent = new Intent(context, IntentService.class);
        context.startService(intent);
    }

    synchronized public static void initialize(@NonNull final Context context) {
        if (sInit) return;

        sInit = true;

        FirebaseJobDispatcher(context);

        Thread checkFOrEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                Uri forecastUri = Contract.Entry.CONTENT_URI;

                String[] projection = {Contract.Entry.COLUMN_ID};
                String selection = Contract.Entry.getSqlSelectForTodayOnwards();

                Cursor cursor = context.getContentResolver().query(forecastUri, null, selection, null, null);

                if (cursor == null || cursor.getCount() == 0) {
                    StarSync(context);
                }

                cursor.close();
            }
        });

        checkFOrEmpty.start();
    }

//        new AsyncTask<Void, Void, Void>() {
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                Uri forecastUri = Contract.Entry.CONTENT_URI;
//
//                String[] projection = {Contract.Entry.COLUMN_ID};
//                String selection = Contract.Entry.getSqlSelectForTodayOnwards();
//
//                Cursor cursor = context.getContentResolver().query(forecastUri, null, null, null, null);
//
//                if (cursor == null || cursor.getCount() == 0) {
//                    StarSync(context);
//                }
//
//                cursor.close();
//
//
//                return null;
//            }
//        }.execute();
    }


