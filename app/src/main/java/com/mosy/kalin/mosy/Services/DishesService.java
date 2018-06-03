package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IDishesRepository;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListFiltersAsyncTask;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        IDishesRepository repository = RetrofitAPIClientFactory.getClient().create(IDishesRepository.class);

        try {
            Call<ArrayList<MenuListItem>> callResult =  repository.getDishes(authTokenHeader, model);
            callResult.enqueue(new Callback<ArrayList<MenuListItem>>() {
                @Override
                public void onResponse(Call<ArrayList<MenuListItem>> call, Response<ArrayList<MenuListItem>> response) {
                    ArrayList<MenuListItem> dishes = response.body();
                    listener.onPostExecute(dishes);
                }
                @Override
                public void onFailure(Call<ArrayList<MenuListItem>> call, Throwable t) {
                    call.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getFilters(Context applicationContext,
                           AsyncTaskListener<RequestableFiltersResult> listener)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        GetRequestableFiltersBindingModel model = new GetRequestableFiltersBindingModel(authTokenHeader);
        new LoadMenuListFiltersAsyncTask(listener).execute(model);
    }

}