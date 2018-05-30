package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;

public class LoadVenueLocationAsyncTask
    extends AsyncTask<GetVenueLocationBindingModel, String, VenueLocation>
{
    private final AsyncTaskListener<VenueLocation> asyncTaskListenerListener;

    public LoadVenueLocationAsyncTask(AsyncTaskListener<VenueLocation> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected VenueLocation doInBackground(GetVenueLocationBindingModel... models) {
        VenueLocation results = null;

        try {
            GetVenueLocationBindingModel model = models[0];
//            results = new VenuesRepository().getLocation(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final VenueLocation result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
