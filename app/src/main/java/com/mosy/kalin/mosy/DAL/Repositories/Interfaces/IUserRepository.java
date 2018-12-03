package com.mosy.kalin.mosy.DAL.Repositories.Interfaces;

import com.mosy.kalin.mosy.DTOs.User;

import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface IUserRepository {

//    @Header("Authorization") String authorization,

    @POST("user/profile")
    Call<User> getUserProfile(@Header("Authorization") String authorization);

}
