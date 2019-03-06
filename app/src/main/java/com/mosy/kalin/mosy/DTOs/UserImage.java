package com.mosy.kalin.mosy.DTOs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

/**
 * Created by kkras on 8/9/2017.
 */

public class UserImage
        extends HttpResult {

    @SerializedName("id")
    public String Id;

    @SerializedName("displayName")
    public String DisplayName;

    @SerializedName("contentType")
    public String ContentType;

    @SerializedName("description")
    public String Description;

    @SerializedName("bytes")
    public byte[] Bytes;

    public Bitmap Bitmap;

}
