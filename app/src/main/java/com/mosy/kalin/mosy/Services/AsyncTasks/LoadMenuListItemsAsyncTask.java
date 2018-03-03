package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;

import com.mosy.kalin.mosy.DAL.Repositories.MenuRepository;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;

import java.util.ArrayList;

public class LoadMenuListItemsAsyncTask
    extends AsyncTask<SearchMenuListItemsBindingModel, String, ArrayList<MenuListItem>>
{
    private final AsyncTaskListener<ArrayList<MenuListItem>> asyncTaskListenerListener;

    public LoadMenuListItemsAsyncTask(AsyncTaskListener<ArrayList<MenuListItem>> listener) {
        asyncTaskListenerListener = listener;
    }

    @Override
    protected void onPreExecute() {
        asyncTaskListenerListener.onPreExecute();
    }

    @Override
    protected ArrayList<MenuListItem> doInBackground(SearchMenuListItemsBindingModel... models) {
        ArrayList<MenuListItem> results = null;

        try {
            SearchMenuListItemsBindingModel model = models[0];
            results = new MenuRepository().searchMenuListItems(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    @Override
    protected void onPostExecute(final ArrayList<MenuListItem> result) {
        asyncTaskListenerListener.onPostExecute(result);
    }
}
