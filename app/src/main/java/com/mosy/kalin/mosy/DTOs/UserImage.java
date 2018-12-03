package com.mosy.kalin.mosy.DTOs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

/**
 * Created by kkras on 8/9/2017.
 */

public class UserImage
        extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("ContentType")
    public String ContentType;

    @SerializedName("Description")
    public String Description;

    @SerializedName("Bytes")
    public byte[] Bytes;

    public Bitmap Bitmap;

}
