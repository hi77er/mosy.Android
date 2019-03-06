package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;

public class FacebookLoginResult {

    @SerializedName("resultMessage")
    public String ResultMessage;

    @SerializedName("accessToken")
    public TokenHttpResult AccessToken;

}
