package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
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

    @GET("fbo/endorsements")
    Call<ArrayList<VenueBadgeEndorsement>> getBadgeEndorsements(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/location")
    Call<VenueLocation> getLocation(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/images/metadataindoor")
    Call<VenueImage> getImageMetaIndoor(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @GET("fbo/publicmenu")
    Call<ArrayList<MenuList>> getMenu(@Header("Authorization") String authorization, @Query("fboId") String venueId);

}
