package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.DishesRepository;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;

public class LoadMenuListFiltersAsyncTask
    extends AsyncTask<GetRequestableFiltersBindingModel, String, RequestableFiltersResult>
{
    private final AsyncTaskListener<RequestableFiltersResult> asyncTaskListenerListener;

    public LoadMenuListFiltersAsyncTask(AsyncTaskListener<RequestableFiltersResult> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected RequestableFiltersResult doInBackground(GetRequestableFiltersBindingModel... models) {
        RequestableFiltersResult results = null;

        try {
            GetRequestableFiltersBindingModel model = models[0];
//            results = new DishesRepository().getFilters(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final RequestableFiltersResult result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
