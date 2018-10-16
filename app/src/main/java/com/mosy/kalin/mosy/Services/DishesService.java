package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IDishesRepository;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersResult;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.List;

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
                           boolean showNotWorkingVenues,
                           String localDateTimeOffset,
                           int searchedDistanceMeters,
                           boolean isDevModeActivated)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);

                    SearchMenuListItemsBindingModel model = new SearchMenuListItemsBindingModel(authTokenHeader,
                            maxResultsCount, totalItemsOffset, latitude, longitude, isPromoted, query, phaseFilterIds,
                            regionFilterIds, spectrumFilterIds, allergensFilterIds, showNotWorkingVenues, localDateTimeOffset, searchedDistanceMeters, isDevModeActivated);

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
                           AsyncTaskListener<DishFiltersResult> apiCallResultListener)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient().create(IDishesRepository.class);
                    try {
                        Call<DishFiltersResult> callFilters = repository.getFilters(authTokenHeader);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<DishFiltersResult>() {
                            @Override public void onResponse(@NonNull Call<DishFiltersResult> call, @NonNull Response<DishFiltersResult> response) {
                                DishFiltersResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<DishFiltersResult> call, @NonNull Throwable t) {
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