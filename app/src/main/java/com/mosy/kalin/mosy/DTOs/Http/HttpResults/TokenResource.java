package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.TokenResultStatus;

import java.io.Serializable;

public class TokenResource
        implements Serializable {

    @SerializedName("access_token")
    public String AccessToken;

    @SerializedName("token_type")
    public String TokenType;

    // Seconds after 1970/01/01, Utc.
    @SerializedName("expires_in")
    public long ExpiresIn;

    @SerializedName("issued")
    public long Issued;

    @SerializedName("expires")
    public String Expires;

}
