package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.util.ArrayList;

public class LoadVenuesAsyncTask
    extends AsyncTask<SearchVenuesBindingModel, String, ArrayList<Venue>>
{
    private final AsyncTaskListener<ArrayList<Venue>> asyncTaskListenerListener;

    public LoadVenuesAsyncTask(AsyncTaskListener<ArrayList<Venue>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected ArrayList<Venue> doInBackground(SearchVenuesBindingModel... models) {
        ArrayList<Venue> results = null;

        try {
            SearchVenuesBindingModel model = models[0];
//            results = new VenuesRepository().loadVenues(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final ArrayList<Venue> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
