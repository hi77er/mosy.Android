package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserExternalLogin
        implements Serializable {

    @SerializedName("userId")
    public String UserId;

    @SerializedName("loginProvider")
    public String LoginProvider;

    @SerializedName("providerKey")
    public String ProviderKey;

}
