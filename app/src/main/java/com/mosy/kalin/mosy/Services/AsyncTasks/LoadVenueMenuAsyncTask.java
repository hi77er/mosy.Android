package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;

import java.util.ArrayList;

public class LoadVenueMenuAsyncTask
    extends AsyncTask<GetVenueMenuBindingModel, String, ArrayList<MenuList>>
{
    private final AsyncTaskListener<ArrayList<MenuList>> asyncTaskListenerListener;

    public LoadVenueMenuAsyncTask(AsyncTaskListener<ArrayList<MenuList>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected ArrayList<MenuList> doInBackground(GetVenueMenuBindingModel... models) {
        ArrayList<MenuList> results = null;

        try {
            GetVenueMenuBindingModel model = models[0];
//            results = new VenuesRepository().getMenu(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final ArrayList<MenuList> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
