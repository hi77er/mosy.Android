package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;

public class FacebookLoginResult {

    @SerializedName("ResultMessage")
    public String ResultMessage;

    @SerializedName("AccessToken")
    public TokenHttpResult AccessToken;

}
