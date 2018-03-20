package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenueRepository;
import com.mosy.kalin.mosy.DTOs.Contact;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;

import java.util.ArrayList;

public class LoadVenueContactsAsyncTask
    extends AsyncTask<GetVenueContactsBindingModel, String, ArrayList<Contact>>
{
    private final AsyncTaskListener<ArrayList<Contact>> asyncTaskListenerListener;

    public LoadVenueContactsAsyncTask(AsyncTaskListener<ArrayList<Contact>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected ArrayList<Contact> doInBackground(GetVenueContactsBindingModel... models) {
        ArrayList<Contact> results = null;

        try {
            GetVenueContactsBindingModel model = models[0];
            results = new VenueRepository().getVenueContacts(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final ArrayList<Contact> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
