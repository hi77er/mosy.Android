package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IVenuesRepository;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.PublicMenuResult;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersHttpResult;

import org.androidannotations.annotations.EBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class VenuesService {

    private AccountService accountService = new AccountService();

    public void getById(Context applicationContext,
                        AsyncTaskListener<WallVenue> apiCallResultListener,
                        Runnable onInvalidHost,
                        String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);

                    try {
                        Call<WallVenue> callResult =  repository.getById(authTokenHeader, venueId);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<WallVenue>() {
                            @Override public void onResponse(@NonNull Call<WallVenue> call, @NonNull Response<WallVenue> response) {
                                WallVenue wallVenue = response.body();
                                apiCallResultListener.onPostExecute(wallVenue);
                            }
                            @Override public void onFailure(@NonNull Call<WallVenue> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getVenues(Context applicationContext,
                          AsyncTaskListener<ArrayList<WallVenue>> apiCallResultListener,
                          Runnable onInvalidHost,
                          int maxResultsCount,
                          int totalItemsOffset,
                          double latitude,
                          double longitude,
                          String query,
                          List<String> venueAccessibilityFilterIds,
                          List<String> venueAvailabilityFilterIds,
                          List<String> venueAtmosphereFilterIds,
                          List<String> venueCultureFilterIds,
                          boolean showNotWorkingVenues,
                          String localDateTimeOffset,
                          int distanceFilterValue,
                          boolean isDevModeActivated)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                 apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    SearchVenuesBindingModel model = new SearchVenuesBindingModel(
                            authTokenHeader, maxResultsCount, totalItemsOffset,
                            latitude, longitude, query, venueAccessibilityFilterIds, venueAvailabilityFilterIds, venueAtmosphereFilterIds, venueCultureFilterIds,
                            showNotWorkingVenues, localDateTimeOffset, distanceFilterValue, isDevModeActivated);

                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);

                    try {
                        Call<ArrayList<WallVenue>> callResult =  repository.loadVenues(authTokenHeader, model);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<ArrayList<WallVenue>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<WallVenue>> call, @NonNull Response<ArrayList<WallVenue>> response) {
                                ArrayList<WallVenue> wallVenues = response.body();
                                 apiCallResultListener.onPostExecute(wallVenues);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<WallVenue>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                null);
    }

    public void getContacts(Context applicationContext,
                            AsyncTaskListener<VenueContacts> apiCallResultListener,
                            Runnable onInvalidHost,
                            String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);

                    try {
                        Call<VenueContacts> call = repository.getContacts(authToken, venueId);
                        apiCallResultListener.onPreExecute();
                        call.enqueue(new Callback<VenueContacts>() {
                            @Override public void onResponse(@NonNull Call<VenueContacts> call, @NonNull Response<VenueContacts> response) {
                                VenueContacts result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<VenueContacts> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getBusinessHours(Context applicationContext,
                                 AsyncTaskListener<VenueBusinessHours> apiCallResultListener,
                                 Runnable onInvalidHost,
                                 String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);

                    try {
                        Call<VenueBusinessHours> callResult =  repository.getBusinessHours(authToken, venueId);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<VenueBusinessHours>() {
                            @Override public void onResponse(@NonNull Call<VenueBusinessHours> call, @NonNull Response<VenueBusinessHours> response) {
                                VenueBusinessHours venues = response.body();
                                apiCallResultListener.onPostExecute(venues);
                            }
                            @Override public void onFailure(@NonNull Call<VenueBusinessHours> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getLocation(Context applicationContext,
                            AsyncTaskListener<VenueLocation> apiCallResultListener,
                            Runnable onInvalidHost,
                            String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);

                    try {
                        Call<VenueLocation> callLocation = repository.getLocation(authToken, venueId);
                        apiCallResultListener.onPreExecute();
                        callLocation.enqueue(new Callback<VenueLocation>() {
                            @Override public void onResponse(@NonNull Call<VenueLocation> call, @NonNull Response<VenueLocation> response) {
                                VenueLocation result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<VenueLocation> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                },
                onInvalidHost);
    }

    public void getImageMeta(Context applicationContext,
                                     AsyncTaskListener<VenueImage> apiCallResultListener,
                                     Runnable onInvalidHost,
                                     String venueId,
                                     boolean isExterior)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);
                    try {
                        Call<VenueImage> callImage = repository.getImageMeta(authToken, venueId, isExterior);
                        apiCallResultListener.onPreExecute();
                        callImage.enqueue(new Callback<VenueImage>() {
                            @Override public void onResponse(@NonNull Call<VenueImage> call, @NonNull Response<VenueImage> response) {
                                VenueImage result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<VenueImage> call, @NonNull Throwable t) {
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

    public void getMenu(Context applicationContext,
                        AsyncTaskListener<PublicMenuResult> apiCallResultListener,
                        Runnable onInvalidHost,
                        String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);
                    try {
                        String localDateTimeOffset = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format(Calendar.getInstance().getTime());

                        Call<PublicMenuResult> callMenu = repository.getMenu(authToken, venueId, localDateTimeOffset);
                        apiCallResultListener.onPreExecute();
                        callMenu.enqueue(new Callback<PublicMenuResult>() {
                            @Override public void onResponse(@NonNull Call<PublicMenuResult> call, @NonNull Response<PublicMenuResult> response) {
                                PublicMenuResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<PublicMenuResult> call, @NonNull Throwable t) {
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

    public void getFilters(Context applicationContext,
                           AsyncTaskListener<VenueFiltersHttpResult> apiCallResultListener,
                           boolean devMode)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);
                    try {
                        Call<VenueFiltersHttpResult> callFilters = repository.getFilters(authTokenHeader, devMode);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<VenueFiltersHttpResult>() {
                            @Override public void onResponse(@NonNull Call<VenueFiltersHttpResult> call, @NonNull Response<VenueFiltersHttpResult> response) {
                                VenueFiltersHttpResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<VenueFiltersHttpResult> call, @NonNull Throwable t) {
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

    public void checkAddView(Context applicationContext, String itemId) {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                null,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient(ServiceEndpointFactory.apiEndpoint).create(IVenuesRepository.class);
                    try {
                        Call<Void> call = repository.checkAddView(authToken, itemId);
                        call.enqueue(new Callback<Void>() {
                            @Override public void onResponse(Call<Void> call, Response<Void> response) {

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