package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Results.RegisterResult;
import com.mosy.kalin.mosy.DTOs.Results.TokenResult;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IAccountRepository {
//    @Header("Authorization") String authorization,
    @POST("account/register")
    Call<RegisterResult> register(@Body RegisterBindingModel model);

    @FormUrlEncoded
    @POST("http://mosyws.azurewebsites.net/token")
    Call<TokenResult> tokenLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);
}
