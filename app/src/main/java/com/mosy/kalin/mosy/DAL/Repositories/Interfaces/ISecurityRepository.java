package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ISecurityRepository {

    @GET("security/ping")
    Call<String> ping(@Header("Authorization") String authorization);

}
