package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;

public class TokenHttpResult
        extends HttpResult {
    public TokenResultStatus Status;

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
