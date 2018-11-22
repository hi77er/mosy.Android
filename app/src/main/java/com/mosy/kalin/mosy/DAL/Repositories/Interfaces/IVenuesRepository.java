package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.HttpResponses.PublicMenuResponse;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IVenuesRepository {

    @GET("fbo/id")
    Call<Venue> getById(@Header("Authorization") String authorization, @Query("id") String venueId);

    @GET("fbo/businesshours")
    Call<VenueBusinessHours> getBusinessHours(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @POST("fbo/closest")
    Call<ArrayList<Venue>> loadVenues(@Header("Authorization") String authorization, @Body SearchVenuesBindingModel model);

    @GET("fbo/contacts")
    Call<VenueContacts> getContacts(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/location")
    Call<VenueLocation> getLocation(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/images/metadataindoor")
    Call<VenueImage> getImageMetaIndoor(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/publicmenu")
    Call<PublicMenuResponse> getMenu(@Header("Authorization") String authorization, @Query("fboId") String venueId, @Query("clientDateTimeOffset") String currentDateTimeOffset);

    @GET("fbo/filters/all")
    Call<VenueFiltersResult> getFilters (@Header("Authorization") String authorization, @Query("devMode") boolean devMode);

    @GET("fbo/view/checkadd")
    Call<Void> checkAddView(@Header("Authorization") String authorization, @Query("fboId") String itemId);

}