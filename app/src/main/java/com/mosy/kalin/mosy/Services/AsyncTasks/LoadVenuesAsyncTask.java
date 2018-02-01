package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.Async.Tasks.SearchMenuListItemsAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Services.MenuListItemsService;
import com.mosy.kalin.mosy.Services.VenuesService;

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
            results = new SearchVenuesAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model).get();

            VenuesService vService = new VenuesService();
            vService.LoadVenuesOutdoorImageThumbnails(results);
            vService.sortVenuesByDistanceToDevice(results);
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
