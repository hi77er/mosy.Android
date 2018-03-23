package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Http.HttpParams;
import com.mosy.kalin.mosy.DAL.Repositories.MenuListItemRepository;
import com.mosy.kalin.mosy.DAL.Repositories.VenueRepository;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetDishAllergensBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;

import java.util.ArrayList;

public class LoadDishAllergensAsyncTask
    extends AsyncTask<GetDishAllergensBindingModel, String, ArrayList<DishFilter>>
{
    private final AsyncTaskListener< ArrayList<DishFilter>> asyncTaskListenerListener;

    public LoadDishAllergensAsyncTask(AsyncTaskListener<ArrayList<DishFilter>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected  ArrayList<DishFilter> doInBackground(GetDishAllergensBindingModel... models) {
        ArrayList<DishFilter> results = null;

        try {
            GetDishAllergensBindingModel model = models[0];
            results = new MenuListItemRepository().getAllergens(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final  ArrayList<DishFilter> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
