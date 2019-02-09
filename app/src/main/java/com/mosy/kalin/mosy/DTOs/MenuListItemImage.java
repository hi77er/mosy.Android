package com.mosy.kalin.mosy.DTOs;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;


public class MenuListItemImage
        extends HttpResult
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("RequestableId")
    public String DishId;

    @SerializedName("DisplayName")
    public String DisplayName;

    @SerializedName("ContentType")
    public String ContentType;

    @SerializedName("Description")
    public String Description;

    @SerializedName("IsOutdoors")
    public boolean IsOutdoors;

    public Bitmap Bitmap;

}
