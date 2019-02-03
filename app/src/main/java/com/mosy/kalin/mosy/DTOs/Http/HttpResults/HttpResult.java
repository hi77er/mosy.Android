package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kkras on 8/9/2017.
 */

public class HttpResult {

    @SerializedName("IsSuccessful")
    public boolean isSuccessful;

    @SerializedName("ErrorMessage")
    public String errorMessage;


    public HttpResult(){ }

    public HttpResult(boolean isSuccessful){
        this.isSuccessful = isSuccessful;
    }

    public HttpResult(boolean isSuccessful, String errorMessage){
        this.isSuccessful = isSuccessful;
        this.errorMessage = errorMessage;
    }
}
