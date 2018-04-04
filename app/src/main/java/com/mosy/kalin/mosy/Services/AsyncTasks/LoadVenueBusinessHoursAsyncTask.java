package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;

public class LoadVenueBusinessHoursAsyncTask
    extends AsyncTask<GetVenueBusinessHoursBindingModel, String, VenueBusinessHours>
{
    private final AsyncTaskListener<VenueBusinessHours> asyncTaskListenerListener;

    public LoadVenueBusinessHoursAsyncTask(AsyncTaskListener<VenueBusinessHours> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected VenueBusinessHours doInBackground(GetVenueBusinessHoursBindingModel... models) {
        VenueBusinessHours results = null;

        try {
            GetVenueBusinessHoursBindingModel model = models[0];
            results = new VenuesRepository().getBusinessHours(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final VenueBusinessHours result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
