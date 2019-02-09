package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

public class PercentagePromotion {

    @SerializedName("Id")
    public String Id;
    @SerializedName("MenuItemName")
    public String MenuItemName;
    @SerializedName("PercentageOff")
    public int PercentageOff;
    @SerializedName("TillDisplayText")
    public String TillDisplayText;

}
