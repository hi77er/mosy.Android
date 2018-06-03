package com.mosy.kalin.mosy.Services;

import android.content.Context;

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


    public void getById(Context applicationContext, AsyncTaskListener<Venue> listener, String venueId)
    {
        String authTokenHeader = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

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

        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

        try {
            Call<ArrayList<Venue>> callResult =  repository.loadVenues(authTokenHeader, model);
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
                                     AsyncTaskListener<VenueContacts> listener,
                                     String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

        try {
            Call<VenueContacts> call = repository.getContacts(authToken, venueId);
            call.enqueue(new Callback<VenueContacts>() {
                @Override
                public void onResponse(Call<VenueContacts> call, Response<VenueContacts> response) {
                    VenueContacts result = response.body();
                    listener.onPostExecute(result);

                }

                @Override
                public void onFailure(Call<VenueContacts> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getBusinessHours(Context applicationContext,
                                 AsyncTaskListener<VenueBusinessHours> listener,
                                 String venueId)
    {
        String authToken = new AccountService().getAuthTokenHeader(applicationContext);

        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

       try {
            Call<VenueBusinessHours> callResult =  repository.getBusinessHours(authToken, venueId);
            callResult.enqueue(new Callback<VenueBusinessHours>() {
                @Override
                public void onResponse(Call<VenueBusinessHours> call, Response<VenueBusinessHours> response) {
                    VenueBusinessHours venues = response.body();
                    listener.onPostExecute(venues);
                }
                @Override
                public void onFailure(Call<VenueBusinessHours> call, Throwable t) {
                    call.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBadgeEndorsements(Context applicationContext,
                                     AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> listener,
                                     String venueId)
    {

        try {
            String authToken = new AccountService().getAuthTokenHeader(applicationContext);
            Retrofit retrofitClient = RetrofitAPIClientFactory.getClient();
            IVenuesRepository repository = retrofitClient.create(IVenuesRepository.class);

            Call<ArrayList<VenueBadgeEndorsement>> callBadge = repository.getBadgeEndorsements(authToken, venueId);
            callBadge.enqueue(new Callback<ArrayList<VenueBadgeEndorsement>>() {
                @Override public void onResponse(Call<ArrayList<VenueBadgeEndorsement>> call, Response<ArrayList<VenueBadgeEndorsement>> response) {
                    ArrayList<VenueBadgeEndorsement> result = response.body();
                    listener.onPostExecute(result);
                }
                @Override public void onFailure(Call<ArrayList<VenueBadgeEndorsement>> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocation (Context applicationContext,
                             AsyncTaskListener<VenueLocation> listener,
                             String venueId)
    {

        String authToken = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);

        try {
            Call<VenueLocation> callLocation = repository.getLocation(authToken, venueId);
            callLocation.enqueue(new Callback<VenueLocation>() {
                @Override
                public void onResponse(Call<VenueLocation> call, Response<VenueLocation> response) {
                    VenueLocation result = response.body();
                    listener.onPostExecute(result);
                }

                @Override
                public void onFailure(Call<VenueLocation> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        catch(Exception e){

            e.printStackTrace();
        }

    }

    public void getImageMetaIndoor(Context applicationContext,
                                   AsyncTaskListener<VenueImage> listener,
                                   String venueId)

    {

        String authToken = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
        try {
            Call<VenueImage> callImage = repository.getImageMetaIndoor(authToken, venueId);
            callImage.enqueue(new Callback<VenueImage>() {
                @Override
                public void onResponse(Call<VenueImage> call, Response<VenueImage> response) {
                    VenueImage result = response.body();
                    listener.onPostExecute(result);
                }

                @Override
                public void onFailure(Call<VenueImage> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getMenu(Context applicationContext,
                        AsyncTaskListener<ArrayList<MenuList>> listener,
                        String venueId)
    {

        String authToken = new AccountService().getAuthTokenHeader(applicationContext);
        IVenuesRepository repository = RetrofitAPIClientFactory.getClient().create(IVenuesRepository.class);
        try {
            Call<ArrayList<MenuList>> callMenu = repository.getMenu(authToken, venueId);
            callMenu.enqueue(new Callback<ArrayList<MenuList>>() {
                @Override
                public void onResponse(Call<ArrayList<MenuList>> call, Response<ArrayList<MenuList>> response) {
                    ArrayList<MenuList> result = response.body();
                    listener.onPostExecute(result);
                }

                @Override
                public void onFailure(Call<ArrayList<MenuList>> call, Throwable t) {
                    call.cancel();
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}