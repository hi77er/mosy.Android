package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenueRepository;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;

public class LoadVenueIndoorImageMetadataAsyncTask
    extends AsyncTask<GetVenueIndoorImageMetaBindingModel, String, VenueImage>
{
    private final AsyncTaskListener<VenueImage> asyncTaskListenerListener;

    public LoadVenueIndoorImageMetadataAsyncTask(AsyncTaskListener<VenueImage> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected VenueImage doInBackground(GetVenueIndoorImageMetaBindingModel... models) {
        VenueImage results = null;

        try {
            GetVenueIndoorImageMetaBindingModel model = models[0];
            results = new VenueRepository().getVenueIndoorImageMeta(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final VenueImage result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
