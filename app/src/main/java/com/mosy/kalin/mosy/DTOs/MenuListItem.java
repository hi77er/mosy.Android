package com.mosy.kalin.mosy.DTOs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class MenuListItem
        extends HttpResult
        implements Serializable {

    @SerializedName("Id")
    public String Id;
    @SerializedName("BrochureId")
    public String BrochureId;
    @SerializedName("Name")
    public String Name;
    @SerializedName("Summary")
    public String Summary;
    @SerializedName("Price")
    public double Price;
    @SerializedName("PriceDisplayText")
    public String PriceDisplayText;
    @SerializedName("CurrencyCode")
    public int CurrencyCode;
    @SerializedName("Quantity")
    public double Quantity;
    @SerializedName("QuantityDisplayText")
    public String QuantityDisplayText;
    @SerializedName("UnitsOfMeasureType")
    public int UnitsOfMeasureType;
    @SerializedName("IsPromoted")
    public boolean IsPromoted;
    @SerializedName("IsPublic")
    public boolean IsPublic;
    @SerializedName("SeenCount")
    public int SeenCount;
    @SerializedName("PreparationEstimateSeconds")
    public int PreparationEstimateSeconds;

    @SerializedName("DefaultMenuCulture")
    public String DefaultMenuCulture;

    @SerializedName("RequestableCultures")
    public ArrayList<MenuListItemCulture> MenuListItemCultures;

}