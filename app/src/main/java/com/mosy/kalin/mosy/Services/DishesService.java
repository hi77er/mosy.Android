package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IDishesRepository;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class DishesService {

    private AccountService accountService = new AccountService();

    public void loadDishes(Context applicationContext,
                           AsyncTaskListener<ArrayList<MenuListItem>> apiCallResultListener,
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
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getAuthTokenHeader(applicationContext);
                    SearchMenuListItemsBindingModel model = new SearchMenuListItemsBindingModel(authTokenHeader,
                            maxResultsCount, totalItemsOffset, latitude, longitude, isPromoted, query, phaseFilterIds,
                            regionFilterIds, spectrumFilterIds, allergensFilterIds, localDayOfWeek, localTime);

                    IDishesRepository repository = RetrofitAPIClientFactory.getClient().create(IDishesRepository.class);

                    try {
                        Call<ArrayList<MenuListItem>> callResult =  repository.loadDishes(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<MenuListItem>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<MenuListItem>> call, @NonNull Response<ArrayList<MenuListItem>> response) {
                                ArrayList<MenuListItem> dishes = response.body();
                                apiCallResultListener.onPostExecute(dishes);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<MenuListItem>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                null);
    }

    public void getFilters(Context applicationContext,
                            AsyncTaskListener<RequestableFiltersResult> apiCallResultListener)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                        () -> {
                    String authTokenHeader = this.accountService.getAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient().create(IDishesRepository.class);
                    try {
                        Call<RequestableFiltersResult> callFilters = repository.getFilters(authTokenHeader);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<RequestableFiltersResult>() {
                            @Override public void onResponse(@NonNull Call<RequestableFiltersResult> call, @NonNull Response<RequestableFiltersResult> response) {
                                RequestableFiltersResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<RequestableFiltersResult> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                },
                null);
    }

}