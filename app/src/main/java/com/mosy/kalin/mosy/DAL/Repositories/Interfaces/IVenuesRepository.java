package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
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
    Call<VenueBusinessHours> getBusinessHoursRetrofit(@Header("Authorization") String authorization, @Query("fboId") String venueId);

    @POST("fbo/closest")
    Call<ArrayList<Venue>> loadVenuesRetrofit(@Header("Authorization") String authorization, @Body SearchVenuesBindingModel model);

}
