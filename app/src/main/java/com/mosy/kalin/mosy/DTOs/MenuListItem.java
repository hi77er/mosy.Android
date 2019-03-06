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

    @SerializedName("id")
    public String Id;
    @SerializedName("brochureId")
    public String BrochureId;
    @SerializedName("name")
    public String Name;
    @SerializedName("summary")
    public String Summary;
    @SerializedName("price")
    public double Price;
    @SerializedName("priceDisplayText")
    public String PriceDisplayText;
    @SerializedName("currencyCode")
    public int CurrencyCode;
    @SerializedName("quantity")
    public double Quantity;
    @SerializedName("quantityDisplayText")
    public String QuantityDisplayText;
    @SerializedName("unitsOfMeasureType")
    public int UnitsOfMeasureType;
    @SerializedName("isPromoted")
    public boolean IsPromoted;
    @SerializedName("isPublic")
    public boolean IsPublic;
    @SerializedName("seenCount")
    public int SeenCount;
    @SerializedName("preparationEstimateSeconds")
    public int PreparationEstimateSeconds;

    @SerializedName("defaultMenuCulture")
    public String DefaultMenuCulture;

    @SerializedName("requestableCultures")
    public ArrayList<MenuListItemCulture> MenuListItemCultures;

}