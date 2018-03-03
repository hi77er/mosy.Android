package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.MenuRepository;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;

public class LoadMenuListFiltersAsyncTask
    extends AsyncTask<GetRequestableFiltersBindingModel, String, RequestableFiltersResponse>
{
    private final AsyncTaskListener<RequestableFiltersResponse> asyncTaskListenerListener;

    public LoadMenuListFiltersAsyncTask(AsyncTaskListener<RequestableFiltersResponse> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected RequestableFiltersResponse doInBackground(GetRequestableFiltersBindingModel... models) {
        RequestableFiltersResponse results = null;

        try {
            GetRequestableFiltersBindingModel model = models[0];
            results = new MenuRepository().getMenuListItemFilters(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final RequestableFiltersResponse result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
