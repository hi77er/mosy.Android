package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenueRepository;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueByIdBindingModel;

public class LoadVenueAsyncTask
    extends AsyncTask<GetVenueByIdBindingModel, String, Venue>
{
    private final AsyncTaskListener<Venue> asyncTaskListenerListener;

    public LoadVenueAsyncTask(AsyncTaskListener<Venue> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected Venue doInBackground(GetVenueByIdBindingModel... models) {
        Venue results = null;
        try {
            GetVenueByIdBindingModel model = models[0];
            results = new VenueRepository().getVenueById(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final Venue result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
