package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DAL.Http.Results.TokenResult;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IAccountRepository {
//
//    @POST("account/register")
//    Call<RegisterResult> register(@Header("Authorization") String authorization, @Body SearchMenuListItemsBindingModel model);

    @FormUrlEncoded
    @POST("http://mosyws.azurewebsites.net/token")
    Call<TokenResult> tokenLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);
}
