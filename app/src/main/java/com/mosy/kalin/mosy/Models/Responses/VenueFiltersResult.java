package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;
import com.mosy.kalin.mosy.DTOs.Filter;

import java.io.Serializable;
import java.util.ArrayList;

public class VenueFiltersResult //INFO: Name coming from WebApi specifics
        extends ResultBase
        implements Serializable {

    @SerializedName("VenueBadgeFilters")
    public ArrayList<Filter> VenueBadgeFilters;

    @SerializedName("VenueCultureFilters")
    public ArrayList<Filter> VenueCultureFilters;

}
