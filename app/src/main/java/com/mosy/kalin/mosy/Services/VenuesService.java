package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IVenuesRepository;
import com.mosy.kalin.mosy.DTOs.HttpResponses.PublicMenuResponse;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersResult;

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
                        AsyncTaskListener<Venue> apiCallResultListener,
                        Runnable onInvalidHost,
                        String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<Venue> callResult =  repository.getById(authTokenHeader, venueId);
                        apiCallResultListener.onPreExecute();
                        callResult.enqueue(new Callback<Venue>() {
                            @Override public void onResponse(@NonNull Call<Venue> call, @NonNull Response<Venue> response) {
                                Venue venue = response.body();
                                apiCallResultListener.onPostExecute(venue);
                            }
                            @Override public void onFailure(@NonNull Call<Venue> call, @NonNull Throwable t) {
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
                          AsyncTaskListener<ArrayList<Venue>> apiCallResultListener,
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

                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<ArrayList<Venue>> callResult =  repository.loadVenues(authTokenHeader, model);
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
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

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
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

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
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

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

    public void getImageMetaIndoor(Context applicationContext,
                                   AsyncTaskListener<VenueImage> apiCallResultListener,
                                   Runnable onInvalidHost,
                                   String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
                    try {
                        Call<VenueImage> callImage = repository.getImageMetaIndoor(authToken, venueId);
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
                        AsyncTaskListener<PublicMenuResponse> apiCallResultListener,
                        Runnable onInvalidHost,
                        String venueId)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
                    try {
                        String localDateTimeOffset = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format(Calendar.getInstance().getTime());

                        Call<PublicMenuResponse> callMenu = repository.getMenu(authToken, venueId, localDateTimeOffset);
                        apiCallResultListener.onPreExecute();
                        callMenu.enqueue(new Callback<PublicMenuResponse>() {
                            @Override public void onResponse(@NonNull Call<PublicMenuResponse> call, @NonNull Response<PublicMenuResponse> response) {
                                PublicMenuResponse result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<PublicMenuResponse> call, @NonNull Throwable t) {
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
                           AsyncTaskListener<VenueFiltersResult> apiCallResultListener)
    {
        this.accountService.executeAssuredWebApiTokenValidOrRefreshed(applicationContext,
                apiCallResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getWebApiAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
                    try {
                        Call<VenueFiltersResult> callFilters = repository.getFilters(authTokenHeader);
                        apiCallResultListener.onPreExecute();
                        callFilters.enqueue(new Callback<VenueFiltersResult>() {
                            @Override public void onResponse(@NonNull Call<VenueFiltersResult> call, @NonNull Response<VenueFiltersResult> response) {
                                VenueFiltersResult result = response.body();
                                apiCallResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<VenueFiltersResult> call, @NonNull Throwable t) {
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