package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetAccountsForVenueBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetOrdersBindingModel;
import com.mosy.kalin.mosy.DTOs.Order;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.WallVenue;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ITablesAccountsRepository {

    @POST("tablesaccounts/taoperator/venues")
    Call<ArrayList<Venue>> getTAOperatorVenues(@Header("Authorization") String authorization);

    @POST("tablesaccounts/forvenue")
    Call<ArrayList<TableAccount>> getAccountsForVenue(@Header("Authorization") String authorization, @Body GetAccountsForVenueBindingModel model);

    @POST("tablesaccounts/orders")
    Call<ArrayList<Order>> getOrders(@Header("Authorization") String authorization, @Body GetOrdersBindingModel model);


}