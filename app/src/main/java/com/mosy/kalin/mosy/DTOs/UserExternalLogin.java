package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserExternalLogin
        implements Serializable {

    @SerializedName("UserId")
    public String UserId;

    @SerializedName("LoginProvider")
    public String LoginProvider;

    @SerializedName("ProviderKey")
    public String ProviderKey;

}
