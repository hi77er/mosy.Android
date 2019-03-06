package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.CheckEmailAvailableBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.LoginBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels.RegisterBindingModel;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.AuthorizationTokensResource;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.CheckEmailAvailableResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.FacebookLoginResult;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.RegisterHttpResult;
import com.mosy.kalin.mosy.Helpers.ServiceEndpointFactory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ITokenRepository {

    @POST(ServiceEndpointFactory.apiTokenEndpoint)
    Call<AuthorizationTokensResource> tokenLogin(@Body LoginBindingModel model);

    @POST(ServiceEndpointFactory.apiTokenEndpoint + "/facebook")
    Call<FacebookLoginResult> facebookLogin(@Query("facebookAccessToken") String facebookAccessToken, @Query("password") String password);

}
