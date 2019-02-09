package com.mosy.kalin.mosy.DTOs.Http.HttpResults;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CheckEmailAvailableResult
    implements Serializable {

    @SerializedName("IsAvailable")
    public boolean IsAvailable;

}