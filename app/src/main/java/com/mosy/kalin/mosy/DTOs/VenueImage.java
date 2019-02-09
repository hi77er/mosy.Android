package com.mosy.kalin.mosy.DTOs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

/**
 * Created by kkras on 8/9/2017.
 */

public class VenueImage
        extends HttpResult {

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
    public byte[] Bytes;

    public Bitmap Bitmap;

}
