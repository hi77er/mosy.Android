package com.mosy.kalin.mosy.DTOs.HttpResponses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;

import java.io.Serializable;

public class CheckEmailAvailableResponse
        implements Serializable {

    @SerializedName("IsAvailable")
    public boolean IsAvailable;

}