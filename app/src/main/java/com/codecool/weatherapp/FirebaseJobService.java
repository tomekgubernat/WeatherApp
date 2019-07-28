package com.codecool.weatherapp;

import android.app.job.JobParameters;
import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobService;

public class FirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFeatchTask;

    @Override
    public boolean onStartJob(final com.firebase.jobdispatcher.JobParameters jobParameters) {

        mFeatchTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                SynchronizeTask.syncWeather(context);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                jobFinished(jobParameters, false);
            }
        };

        mFeatchTask.execute();

        return true;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        if (mFeatchTask != null) {
            mFeatchTask.cancel(true);
        }
        return true;
    }

}
