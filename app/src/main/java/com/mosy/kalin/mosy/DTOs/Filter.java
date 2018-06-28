package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;

import java.io.Serializable;

public class Filter
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Name")
    public String Name;

    @SerializedName("I18nResourceName")
    public String I18nResourceName;

    @SerializedName("Description")
    public String Description;

    @SerializedName("I18nResourceDescription")
    public String I18nResourceDescription;

    @SerializedName("FilteredType")
    public FilteredType FilteredType;

    @SerializedName("FilterType")
    public FilterType FilterType;

    @SerializedName("OrderIndex")
    public int OrderIndex;

    @SerializedName("Icon")
    public byte[] Icon;

    public boolean IsChecked;


}