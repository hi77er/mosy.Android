package com.mosy.kalin.mosy.Services.AsyncTasks;

import android.os.AsyncTask;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.Async.Tasks.SearchMenuListItemsAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Services.MenuListItemsService;

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
            results = new SearchMenuListItemsAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, model).get();
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
