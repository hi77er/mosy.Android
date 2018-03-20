package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.ContactType;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

public class Contact extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Value")
    public String Value;

    @SerializedName("Type")
    public int Type;

    @SerializedName("FBOId")
    public String VenueId;

}
