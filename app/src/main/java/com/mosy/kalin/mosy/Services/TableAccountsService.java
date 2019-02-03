package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.ITablesAccountsRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetAccountsForVenueBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetOrdersBindingModel;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class TableAccountsService {

    private AccountService accountService = new AccountService();

    public void getTableAccountVenues(Context applicationContext,
                                      AsyncTaskListener<ArrayList<Venue>> apiCallResultListener,
                                      Runnable onInvalidHost)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient().create(ITablesAccountsRepository.class);

                    try {
                        Call<ArrayList<Venue>> callResult =  repository.getTAOperatorVenues(authTokenHeader);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<Venue>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<Venue>> call, @NonNull Response<ArrayList<Venue>> response) {
                                ArrayList<Venue> venues = response.body();
                                apiCallResultListener.onPostExecute(venues);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<Venue>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getTableAccounts(Context applicationContext,
                                 AsyncTaskListener<ArrayList<TableAccount>> apiCallResultListener,
                                 Runnable onInvalidHost,
                                 String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient().create(ITablesAccountsRepository.class);

                    try {
                        GetAccountsForVenueBindingModel model = new GetAccountsForVenueBindingModel(venueId);
                        Call<ArrayList<TableAccount>> callResult =  repository.getAccountsForVenue(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<TableAccount>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<TableAccount>> call, @NonNull Response<ArrayList<TableAccount>> response) {
                                ArrayList<TableAccount> results = response.body();
                                apiCallResultListener.onPostExecute(results);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<TableAccount>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getOrders(Context applicationContext,
                                 AsyncTaskListener<ArrayList<Order>> apiCallResultListener,
                                 Runnable onInvalidHost,
                                 String tableAccountId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient().create(ITablesAccountsRepository.class);

                    try {
                        GetOrdersBindingModel model = new GetOrdersBindingModel(tableAccountId);
                        Call<ArrayList<Order>> callResult =  repository.getOrders(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<Order>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<Order>> call, @NonNull Response<ArrayList<Order>> response) {
                                ArrayList<Order> results = response.body();
                                apiCallResultListener.onPostExecute(results);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<Order>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }




}