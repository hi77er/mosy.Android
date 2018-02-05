package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.io.Serializable;


public class MenuListItemImage
        extends ResultBase
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

    @SerializedName("Bytes")
    public byte[] Bytes;

}
