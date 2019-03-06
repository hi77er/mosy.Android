package com.mosy.kalin.mosy.DTOs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;


public class MenuListItemImage
        extends HttpResult
        implements Serializable {

    @SerializedName("id")
    public String Id;

    @SerializedName("requestableId")
    public String DishId;

    @SerializedName("displayName")
    public String DisplayName;

    @SerializedName("contentType")
    public String ContentType;

    @SerializedName("description")
    public String Description;

    @SerializedName("isOutdoors")
    public boolean IsOutdoors;

    public Bitmap Bitmap;

}
