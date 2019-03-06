package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.AddExternalLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterExternalBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RemoveLoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.AuthorizationTokensResource;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.ExternalLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.FacebookLoginResult;
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

    @POST("account/registerconsumer")
    Call<RegisterHttpResult> register(@Header("Authorization") String authorization, @Body RegisterBindingModel model);

//    @FormUrlEncoded
//    @POST(ServiceEndpointFactory.apiTokenEndpoint)
//    Call<TokenHttpResult> tokenLogin(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @POST("account/emailavailable")
    Call<CheckEmailAvailableResult> checkEmailAvailable(@Header("Authorization") String authorization, @Body CheckEmailAvailableBindingModel model);

}
