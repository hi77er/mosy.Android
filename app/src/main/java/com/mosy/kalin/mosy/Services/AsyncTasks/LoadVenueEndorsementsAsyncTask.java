package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.VenuesRepository;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;

import java.util.ArrayList;

public class LoadVenueEndorsementsAsyncTask
    extends AsyncTask<GetVenueBadgeEndorsementsBindingModel, String, ArrayList<VenueBadgeEndorsement>>
{
    private final AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> asyncTaskListenerListener;

    public LoadVenueEndorsementsAsyncTask(AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected ArrayList<VenueBadgeEndorsement> doInBackground(GetVenueBadgeEndorsementsBindingModel... models) {
        ArrayList<VenueBadgeEndorsement> results = null;

        try {
            GetVenueBadgeEndorsementsBindingModel model = models[0];
            results = new VenuesRepository().getBadgeEndorsements(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final ArrayList<VenueBadgeEndorsement> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
