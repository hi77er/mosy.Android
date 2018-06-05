package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IDishesRepository {

    @POST("dishes/closest")
    Call<ArrayList<MenuListItem>> loadDishes(@Header("Authorization") String authorization, @Body SearchMenuListItemsBindingModel model);

    @GET("dishes/filters/all")
    Call<RequestableFiltersResult> getFilters (@Header("Authorization") String authorization);

}
