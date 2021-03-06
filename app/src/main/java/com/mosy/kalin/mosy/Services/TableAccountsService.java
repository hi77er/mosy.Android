package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.ITablesAccountsRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetAccountsForVenueBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetOrdersBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetTableAccountBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetTablesBindingModel;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
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
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(ITablesAccountsRepository.class);

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
                                t.printStackTrace();
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
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(ITablesAccountsRepository.class);

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
                                t.printStackTrace();
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
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(ITablesAccountsRepository.class);

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
                                t.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void geTables(Context applicationContext,
                          AsyncTaskListener<ArrayList<Table>> apiCallResultListener,
                          Runnable onInvalidHost,
                          Runnable onUnauthorized,
                          String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(ITablesAccountsRepository.class);

                    try {
                        GetTablesBindingModel model = new GetTablesBindingModel(venueId);
                        Call<ArrayList<Table>> callResult =  repository.getTables(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<Table>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<Table>> call, @NonNull Response<ArrayList<Table>> response) {
                                if (response.code() == 401) { // unauthorized
                                    if (onUnauthorized != null)
                                        onUnauthorized.run();
                                }
                                else {
                                    ArrayList<Table> results = response.body();
                                    apiCallResultListener.onPostExecute(results);
                                }
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<Table>> call, @NonNull Throwable t) {
                                call.cancel();
                                t.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }


    public void getTableAccount(Context applicationContext,
                         AsyncTaskListener<TableAccount> apiCallResultListener,
                         Runnable onInvalidHost,
                         Runnable onUnauthorized,
                               String venueId,
                               String openerUsername)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getUserAuthTokenHeader(applicationContext);
                    ITablesAccountsRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(ITablesAccountsRepository.class);

                    try {
                        GetTableAccountBindingModel model = new GetTableAccountBindingModel(venueId, openerUsername);
                        Call<TableAccount> callResult =  repository.getTableAccount(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<TableAccount>() {
                            @Override public void onResponse(@NonNull Call<TableAccount> call, @NonNull Response<TableAccount> response) {
                                if (response.code() == 401) { // unauthorized
                                    if (onUnauthorized != null)
                                        onUnauthorized.run();
                                }
                                else {
                                    TableAccount results = response.body();
                                    apiCallResultListener.onPostExecute(results);
                                }
                            }
                            @Override public void onFailure(@NonNull Call<TableAccount> call, @NonNull Throwable t) {
                                call.cancel();
                                t.printStackTrace();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

}