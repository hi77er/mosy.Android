package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.FilteredType;

import java.io.Serializable;

public class Filter
        implements Serializable {

    @SerializedName("id")
    public String Id;

    @SerializedName("name")
    public String Name;

    @SerializedName("i18nResourceName")
    public String I18nResourceName;

    @SerializedName("description")
    public String Description;

    @SerializedName("i18nResourceDescription")
    public String I18nResourceDescription;

    @SerializedName("filteredType")
    public FilteredType FilteredType;

    @SerializedName("filterType")
    public FilterType FilterType;

    @SerializedName("orderIndex")
    public int OrderIndex;

    @SerializedName("icon")
    public byte[] Icon;

    public boolean IsChecked;


}