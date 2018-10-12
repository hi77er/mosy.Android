package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DAL.Http.Results.RegisterResult;
import com.mosy.kalin.mosy.DAL.Http.Results.TokenResult;
import com.mosy.kalin.mosy.DTOs.HttpResponses.CheckEmailAvailableResponse;
import com.mosy.kalin.mosy.DTOs.User;
import com.mosy.kalin.mosy.Models.BindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.RegisterBindingModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IAccountRepository {

//    @Header("Authorization") String authorization,
    @POST("account/register")
    Call<RegisterResult> register(@Header("Authorization") String authorization, @Body RegisterBindingModel model);

    @POST("account/emailavailable")
    Call<CheckEmailAvailableResponse> checkEmailAvailable(@Header("Authorization") String authorization, @Body CheckEmailAvailableBindingModel model);

    @FormUrlEncoded
    @POST("http://mosyws.azurewebsites.net/token")
    Call<TokenResult> tokenLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @POST("account/userprofile")
    Call<User> getUserProfile(@Header("Authorization") String authorization);

}
