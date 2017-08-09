package com.mosy.kalin.mosy.DTOs.Results;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kkras on 8/9/2017.
 */

public class VenueImageResult extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("ContentType")
    public String ContentType;

    @SerializedName("Description")
    public String Description;

    @SerializedName("Bytes")
    public String Bytes;

}
