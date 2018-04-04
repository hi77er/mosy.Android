package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListFiltersAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListItemsAsyncTask;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

@EBean
public class DishesService {

    public void getDishes(Context applicationContext,
                          AsyncTaskListener<ArrayList<MenuListItem>> listener,
                          int maxResultsCount,
                          int totalItemsOffset,
                          double latitude,
                          double longitude,
                          Boolean isPromoted,
                          String query,
                          ArrayList<String> phaseFilterIds,
                          ArrayList<String> regionFilterIds,
                          ArrayList<String> spectrumFilterIds,
                          ArrayList<String> allergensFilterIds,
                          Integer localDayOfWeek,
                          String localTime)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        SearchMenuListItemsBindingModel model = new SearchMenuListItemsBindingModel(
                authTokenHeader,
                maxResultsCount,
                totalItemsOffset,
                latitude,
                longitude,
                isPromoted,
                query,
                phaseFilterIds,
                regionFilterIds,
                spectrumFilterIds,
                allergensFilterIds,
                localDayOfWeek,
                localTime);

        new LoadMenuListItemsAsyncTask(listener).execute(model);
    }


    public void getFilters(Context applicationContext,
                           AsyncTaskListener<RequestableFiltersResult> listener)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        GetRequestableFiltersBindingModel model = new GetRequestableFiltersBindingModel(authTokenHeader);
        new LoadMenuListFiltersAsyncTask(listener).execute(model);
    }

}