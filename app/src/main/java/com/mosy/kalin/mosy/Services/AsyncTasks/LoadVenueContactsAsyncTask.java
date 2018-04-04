package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;

public class LoadVenueContactsAsyncTask
    extends AsyncTask<GetVenueContactsBindingModel, String, VenueContacts>
{
    private final AsyncTaskListener<VenueContacts> asyncTaskListenerListener;

    public LoadVenueContactsAsyncTask(AsyncTaskListener<VenueContacts> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected VenueContacts doInBackground(GetVenueContactsBindingModel... models) {
        VenueContacts results = null;

        try {
            GetVenueContactsBindingModel model = models[0];
            results = new VenuesRepository().getContacts(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final VenueContacts result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
