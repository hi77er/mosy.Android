package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

/**
 * Created by kkras on 8/9/2017.
 */

public class VenueImage extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("ContentType")
    public String ContentType;

    @SerializedName("Description")
    public String Description;

    @SerializedName("IsOutdoors")
    public boolean IsOutdoors;

    @SerializedName("Bytes")
    public String Bytes;

}
