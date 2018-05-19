package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.mosy.kalin.mosy.DAL.Http.RetrofitAPIClient;
import com.mosy.kalin.mosy.DAL.Repositories.Interfaces.IVenuesRepository;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueByIdBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueContactsAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueEndorsementsAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueIndoorImageMetadataAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueMenuAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenuesAsyncTask;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@EBean
public class VenuesService {


    public void getById(Context applicationContext, AsyncTaskListener<Venue> listener, String venueId)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClient.getClient().create(IVenuesRepository.class);

        Venue result = null;
        try {
            Call<Venue> callResult =  repository.getById(authTokenHeader, venueId);
            callResult.enqueue(new Callback<Venue>() {
                @Override
                public void onResponse(Call<Venue> call, Response<Venue> response) {
                    Venue venue = response.body();
                    listener.onPostExecute(venue);
                }
                @Override
                public void onFailure(Call<Venue> call, Throwable t) {
                    call.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getVenues(Context applicationContext,
                          AsyncTaskListener<ArrayList<Venue>> listener,
                          int maxResultsCount,
                          int totalItemsOffset,
                          double latitude,
                          double longitude,
                          String query,
                          Integer localDayOfWeek,
                          String localTime) {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
        SearchVenuesBindingModel model = new SearchVenuesBindingModel(
                authTokenHeader,
                maxResultsCount,
                totalItemsOffset,
                latitude,
                longitude,
                query,
                localDayOfWeek,
                localTime);

        new LoadVenuesAsyncTask(listener).execute(model);
    }

    public void getVenuesRetrofit(Context applicationContext,
                          AsyncTaskListener<ArrayList<Venue>> listener,
                          int maxResultsCount,
                          int totalItemsOffset,
                          double latitude,
                          double longitude,
                          String query,
                          Integer localDayOfWeek,
                          String localTime)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
        SearchVenuesBindingModel model = new SearchVenuesBindingModel(
                authTokenHeader,
                maxResultsCount,
                totalItemsOffset,
                latitude,
                longitude,
                query,
                localDayOfWeek,
                localTime);

        IVenuesRepository repository = RetrofitAPIClient.getClient().create(IVenuesRepository.class);

        ArrayList<Venue> result = null;
        try {
            Call<ArrayList<Venue>> callResult =  repository.loadVenuesRetrofit(authTokenHeader, model);
            callResult.enqueue(new Callback<ArrayList<Venue>>() {
                @Override
                public void onResponse(Call<ArrayList<Venue>> call, Response<ArrayList<Venue>> response) {
                    ArrayList<Venue> venues = response.body();
                    listener.onPostExecute(venues);
                }
                @Override
                public void onFailure(Call<ArrayList<Venue>> call, Throwable t) {
                    call.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getImageMetaIndoor(Context applicationContext, AsyncTaskListener<VenueImage> listener, String venueId)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueIndoorImageMetaBindingModel iiModel = new GetVenueIndoorImageMetaBindingModel(authTokenHeader, venueId);
        new LoadVenueIndoorImageMetadataAsyncTask(listener).execute(iiModel);
    }

    public void getMenu(Context applicationContext, AsyncTaskListener<ArrayList<MenuList>> listener, String venueId)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueMenuBindingModel mModel = new GetVenueMenuBindingModel(authTokenHeader, venueId);
        new LoadVenueMenuAsyncTask(listener).execute(mModel);
    }

    public void getLocation(Context applicationContext, AsyncTaskListener<VenueLocation> listener, String venueId)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueLocationBindingModel mModel = new GetVenueLocationBindingModel(authTokenHeader, venueId);
        new LoadVenueLocationAsyncTask(listener).execute(mModel);
    }

    public void getEndorsements(Context applicationContext,
                                AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> listener,
                                String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueBadgeEndorsementsBindingModel model = new GetVenueBadgeEndorsementsBindingModel(authToken, venueId);
        new LoadVenueEndorsementsAsyncTask(listener).execute(model);
    }

    public void getContacts(Context applicationContext,
                                     AsyncTaskListener<VenueContacts> listener,
                                     String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueContactsBindingModel model = new GetVenueContactsBindingModel(authToken, venueId);
        new LoadVenueContactsAsyncTask(listener).execute(model);
    }

    public void getBusinessHours(Context applicationContext,
                            AsyncTaskListener<VenueBusinessHours> listener,
                            String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);

        GetVenueBusinessHoursBindingModel model = new GetVenueBusinessHoursBindingModel(authToken, venueId);
        new LoadVenueBusinessHoursAsyncTask(listener).execute(model);
    }

}