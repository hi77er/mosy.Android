package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IDishesRepository;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetItemPreferredCultureBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersHttpResult;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class DishesService {

    private AccountService accountService = new AccountService();

    public void loadDishes(Context applicationContext,
                           AsyncTaskListener<ArrayList<WallMenuListItem>> apiCallResultListener,
                           int maxResultsCount,
                           int totalItemsOffset,
                           double latitude,
                           double longitude,
                           String query,
                           ArrayList<String> phaseFilterIds,
                           ArrayList<String> drinksFilterIds,
                           ArrayList<String> regionFilterIds,
                           ArrayList<String> spectrumFilterIds,
                           ArrayList<String> allergensFilterIds,
                           boolean showNotRecommendedDishes,
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
                            maxResultsCount, totalItemsOffset, latitude, longitude, query, phaseFilterIds, drinksFilterIds,
                            regionFilterIds, spectrumFilterIds, allergensFilterIds, showNotRecommendedDishes, showNotWorkingVenues,
                            localDateTimeOffset, searchedDistanceMeters, isDevModeActivated);

                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);

                    try {
                        Call<ArrayList<WallMenuListItem>> callResult =  repository.loadDishes(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<WallMenuListItem>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<WallMenuListItem>> call, @NonNull Response<ArrayList<WallMenuListItem>> response) {
                                ArrayList<WallMenuListItem> dishes = response.body();
                                apiCallResultListener.onPostExecute(dishes);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<WallMenuListItem>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                null);
    }

    public void getAllFilters(Context applicationContext,
                              AsyncTaskListener<DishFiltersHttpResult> apiCallResultListener,
                              boolean devMode)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);
                    try {
                        Call<DishFiltersHttpResult> callFilters = repository.loadAllFilters(authTokenHeader, devMode);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<DishFiltersHttpResult>() {
                            @Override public void onResponse(@NonNull Call<DishFiltersHttpResult> call, @NonNull Response<DishFiltersHttpResult> response) {
                                DishFiltersHttpResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<DishFiltersHttpResult> call, @NonNull Throwable t) {
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

    public void getFilters(Context applicationContext,
                           AsyncTaskListener<ArrayList<Filter>> apiCallResultListener,
                           String itemId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);
                    try {
                        Call<ArrayList<Filter>> callFilters = repository.loadFilters(authTokenHeader, itemId);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<ArrayList<Filter>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<Filter>> call, @NonNull Response<ArrayList<Filter>> response) {
                                ArrayList<Filter> result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<Filter>> call, @NonNull Throwable t) {
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



    public void getImageMeta(Context applicationContext,
                             AsyncTaskListener<MenuListItemImage> apiCallResultListener,
                             Runnable onInvalidHost,
                             String itemId) {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);
                    try {
                        Call<MenuListItemImage> callImage = repository.getImageMeta(authToken, itemId);
                        apiCallResultListener.onPreExecute();
                        callImage.enqueue(new Callback<MenuListItemImage>() {
                            @Override public void onResponse(@NonNull Call<MenuListItemImage> call, @NonNull Response<MenuListItemImage> response) {
                                MenuListItemImage result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<MenuListItemImage> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getItemPreferredCulture(Context applicationContext, AsyncTaskListener<MenuListItemCulture> apiCallResultListener, Runnable onInvalidHost, String itemId) {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    ArrayList<String> preferredLocales = LocaleHelper.getPreferredLanguages(applicationContext);
                    GetItemPreferredCultureBindingModel model = new GetItemPreferredCultureBindingModel(itemId, preferredLocales);

                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);
                    try {
                        Call<MenuListItemCulture> callImage = repository.getItemPreferredCulture(authToken, model);
                        apiCallResultListener.onPreExecute();
                        callImage.enqueue(new Callback<MenuListItemCulture>() {
                            @Override public void onResponse(@NonNull Call<MenuListItemCulture> call, @NonNull Response<MenuListItemCulture> response) {
                                MenuListItemCulture result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<MenuListItemCulture> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void checkAddView(Context applicationContext, String itemId) {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                null,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IDishesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IDishesRepository.class);
                    try {
                        Call<Void> call = repository.checkAddView(authToken, itemId);
                        call.enqueue(new Callback<Void>() {
                            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                                String asda = "";
                            }
                            @Override public void onFailure(Call<Void> call, Throwable t) { }
                        });
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                },
                null);
    }
}