package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kkras on 7/25/2017.
 */

public class TokenModel {
    @SerializedName("access_token")
    public String AccessToken;

   @SerializedName(".expires")
    public String ExpiresAt;

   @SerializedName("expires_in")
    public Integer ExpiresIn;

   @SerializedName(".issued")
    public String IssuedAt;

   @SerializedName("token_type")
    public String TokenType;

   @SerializedName("userName")
    public String Username;
}
