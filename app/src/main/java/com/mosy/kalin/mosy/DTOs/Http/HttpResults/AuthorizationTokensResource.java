package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;

import java.io.Serializable;

public class AuthorizationTokensResource
        implements Serializable {

    @SerializedName("accessToken")
    public TokenResource AccessToken;

    @SerializedName("RefreshToken")
    public TokenResource RefreshToken;

}
