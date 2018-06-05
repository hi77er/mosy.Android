package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClientFactory;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IVenuesRepository;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@EBean
public class VenuesService {

    private AccountService accountService = new AccountService();

    public void getById(Context applicationContext, AsyncTaskListener<Venue> venueResultListener, String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                venueResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<Venue> callResult =  repository.getById(authTokenHeader, venueId);
                        callResult.enqueue(new Callback<Venue>() {
                            @Override public void onResponse(@NonNull Call<Venue> call, @NonNull Response<Venue> response) {
                                Venue venue = response.body();
                                venueResultListener.onPostExecute(venue);
                            }
                            @Override public void onFailure(@NonNull Call<Venue> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                null);
    }

    public void getVenues(Context applicationContext,
                          AsyncTaskListener<ArrayList<Venue>> venuesResultListener,
                          int maxResultsCount,
                          int totalItemsOffset,
                          double latitude,
                          double longitude,
                          String query,
                          Integer localDayOfWeek,
                          String localTime)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                venuesResultListener::onPreExecute,
                () -> {
                    String authTokenHeader = this.accountService.getAuthTokenHeader(applicationContext);
                    SearchVenuesBindingModel model = new SearchVenuesBindingModel(
                            authTokenHeader, maxResultsCount, totalItemsOffset,
                            latitude, longitude, query, localDayOfWeek, localTime);

                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<ArrayList<Venue>> callResult =  repository.loadVenues(authTokenHeader, model);
                        callResult.enqueue(new Callback<ArrayList<Venue>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<Venue>> call, @NonNull Response<ArrayList<Venue>> response) {
                                ArrayList<Venue> venues = response.body();
                                venuesResultListener.onPostExecute(venues);
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

//    public void getImageMetaIndoor(Context applicationContext, AsyncTaskListener<VenueImage> listener, String venueId)
//    {
//        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
//
//        GetVenueIndoorImageMetaBindingModel iiModel = new GetVenueIndoorImageMetaBindingModel(authTokenHeader, venueId);
//        new LoadVenueIndoorImageMetadataAsyncTask(listener).execute(iiModel);
//    }

//    public void getMenu(Context applicationContext, AsyncTaskListener<ArrayList<MenuList>> listener, String venueId)
//    {
//        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
//
//        GetVenueMenuBindingModel mModel = new GetVenueMenuBindingModel(authTokenHeader, venueId);
//        new LoadVenueMenuAsyncTask(listener).execute(mModel);
//    }

//    public void getLocation(Context applicationContext, AsyncTaskListener<VenueLocation> listener, String venueId)
//    {
//        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
//
//        GetVenueLocationBindingModel mModel = new GetVenueLocationBindingModel(authTokenHeader, venueId);
//        new LoadVenueLocationAsyncTask(listener).execute(mModel);
//    }

    public void getContacts(Context applicationContext,
                                     AsyncTaskListener<VenueContacts> contactsResultListener,
                                     String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                contactsResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<VenueContacts> call = repository.getContacts(authToken, venueId);
                        call.enqueue(new Callback<VenueContacts>() {
                            @Override public void onResponse(@NonNull Call<VenueContacts> call, @NonNull Response<VenueContacts> response) {
                                VenueContacts result = response.body();
                                contactsResultListener.onPostExecute(result);
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
                null);
    }

    public void getBusinessHours(Context applicationContext,
                                 AsyncTaskListener<VenueBusinessHours> businessHoursResultListener,
                                 String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                businessHoursResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<VenueBusinessHours> callResult =  repository.getBusinessHours(authToken, venueId);
                        callResult.enqueue(new Callback<VenueBusinessHours>() {
                            @Override public void onResponse(@NonNull Call<VenueBusinessHours> call, @NonNull Response<VenueBusinessHours> response) {
                                VenueBusinessHours venues = response.body();
                                businessHoursResultListener.onPostExecute(venues);
                            }
                            @Override public void onFailure(@NonNull Call<VenueBusinessHours> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                null);
    }

    public void getBadgeEndorsements(Context applicationContext,
                                     AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> badgeEndorsementsResultListener,
                                     String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                badgeEndorsementsResultListener::onPreExecute,
                () -> {
                    try {
                        String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                        Retrofit retrofitClient = RetrofitAPIClientFactory.getClient();
                        IVenuesRepository repository = retrofitClient.create(IVenuesRepository.class);

                        Call<ArrayList<VenueBadgeEndorsement>> callBadge = repository.getBadgeEndorsements(authToken, venueId);
                        callBadge.enqueue(new Callback<ArrayList<VenueBadgeEndorsement>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<VenueBadgeEndorsement>> call, @NonNull Response<ArrayList<VenueBadgeEndorsement>> response) {
                                ArrayList<VenueBadgeEndorsement> result = response.body();
                                badgeEndorsementsResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<VenueBadgeEndorsement>> call, @NonNull Throwable t) {
                                call.cancel();
                            }
                        });
                    }
                    catch(Exception e) {
                        e.printStackTrace();
                    }
                },
                null);

    }

    public void getLocation(Context applicationContext,
                             AsyncTaskListener<VenueLocation> locationResultListener,
                             String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                locationResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

                    try {
                        Call<VenueLocation> callLocation = repository.getLocation(authToken, venueId);
                        callLocation.enqueue(new Callback<VenueLocation>() {
                            @Override public void onResponse(@NonNull Call<VenueLocation> call, @NonNull Response<VenueLocation> response) {
                                VenueLocation result = response.body();
                                locationResultListener.onPostExecute(result);
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
                null);
    }

    public void getImageMetaIndoor(Context applicationContext,
                                   AsyncTaskListener<VenueImage> imageMetadataResultListener,
                                   String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                imageMetadataResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
                    try {
                        Call<VenueImage> callImage = repository.getImageMetaIndoor(authToken, venueId);
                        callImage.enqueue(new Callback<VenueImage>() {
                            @Override public void onResponse(@NonNull Call<VenueImage> call, @NonNull Response<VenueImage> response) {
                                VenueImage result = response.body();
                                imageMetadataResultListener.onPostExecute(result);
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
                null);
    }

    public void getMenu(Context applicationContext,
                        AsyncTaskListener<ArrayList<MenuList>> menuResultListener,
                        String venueId)
    {
        this.accountService.executeAssuredTokenValidOrRefreshed(applicationContext,
                menuResultListener::onPreExecute,
                () -> {
                    String authToken = this.accountService.getAuthTokenHeader(applicationContext);
                    IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
                    try {
                        Call<ArrayList<MenuList>> callMenu = repository.getMenu(authToken, venueId);
                        callMenu.enqueue(new Callback<ArrayList<MenuList>>() {
                            @Override public void onResponse(@NonNull Call<ArrayList<MenuList>> call, @NonNull Response<ArrayList<MenuList>> response) {
                                ArrayList<MenuList> result = response.body();
                                menuResultListener.onPostExecute(result);
                            }
                            @Override public void onFailure(@NonNull Call<ArrayList<MenuList>> call, @NonNull Throwable t) {
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