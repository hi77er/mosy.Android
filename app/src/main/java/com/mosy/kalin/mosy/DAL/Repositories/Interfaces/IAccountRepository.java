package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.facebook.AccessToken;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.AddExternalLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterExternalBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RemoveLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.ExternalLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.FacebookLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.RegisterHttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.TokenHttpResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.CheckEmailAvailableResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.UserInfoResult;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterBindingModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IAccountRepository {

    //region LOGIN/REGISTER
    @POST("account/registerconsumer")
    Call<RegisterHttpResult> register(@Header("Authorization") String authorization, @Body RegisterBindingModel model);

    @FormUrlEncoded
    @POST(ServiceEndpointFactory.apiTokenEndpoint)
    Call<TokenHttpResult> tokenLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);
    //endregion

    //region EXTERNAL LOGIN
    @GET("account/externallogins")
    Call<ArrayList<ExternalLoginResult>> getExternalLogins(@Query("returnUrl")String returnUrl, @Query("generateState") boolean generateState);

    @GET("account/externallogin")
    Call<Void> externalLogin(@Query("provider")String provider, @Query("error") String error);

    @GET("account/userinfo")
    Call<UserInfoResult> getUserInfo(@Header("Authorization") String authorization);

    @POST("account/registerexternal")
    Call<Void> registerExternal(@Header("Authorization") String authorization, @Body RegisterExternalBindingModel model);

    @POST("account/addexternallogin")
    Call<Void> addExternalLogin(@Body AddExternalLoginBindingModel model);

    @POST("account/removelogin")
    Call<Void> removeExternalLogin(@Body RemoveLoginBindingModel model);

    @POST("account/facebooklogin")
    Call<FacebookLoginResult> facebookLogin(@Query("facebookAccessToken") String facebookAccessToken, @Query("password") String password);

    //endregion

    //region WORKING WITH EMAILS
    @POST("account/emailavailable")
    Call<CheckEmailAvailableResult> checkEmailAvailable(@Header("Authorization") String authorization, @Body CheckEmailAvailableBindingModel model);

    @GET("account/confirmemail")
    Call<Void> confirmEmail(@Header("Authorization") String authorization, @Query("userId") String userId, @Query("code") String code);
    //endregion

}
