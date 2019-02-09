package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.GetItemPreferredCultureBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LocalizedInfoModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersHttpResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IDishesRepository {

    @POST("dishes/closest")
    Call<ArrayList<WallMenuListItem>> loadDishes(@Header("Authorization") String authorization, @Body SearchMenuListItemsBindingModel model);

    @GET("dishes/filters/all")
    Call<DishFiltersHttpResult> loadAllFilters(@Header("Authorization") String authorization, @Query("devMode") boolean devMode);

    @GET("dishes/images/meta")
    Call<MenuListItemImage> getImageMeta(@Header("Authorization") String authorization, @Query("itemId") String itemId);

    @POST("dishes/info/localized")
    Call<ArrayList<WallMenuListItem>> loadLocalizedInfo(@Header("Authorization") String authorization, @Body LocalizedInfoModel model);

    @POST("dishes/filters")
    Call<ArrayList<Filter>> loadFilters(@Header("Authorization") String authorization, @Query("itemId") String itemId);

    @POST("dishes/culture/preferred")
    Call<MenuListItemCulture> getItemPreferredCulture(@Header("Authorization") String authorization, @Body GetItemPreferredCultureBindingModel model);

    @GET("dishes/view/checkadd")
    Call<Void> checkAddView(@Header("Authorization") String authorization, @Query("itemId") String itemId);

}
