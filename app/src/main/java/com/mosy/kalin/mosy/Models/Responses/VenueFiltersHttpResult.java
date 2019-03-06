package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;
import com.mosy.kalin.mosy.DTOs.Filter;

import java.io.Serializable;
import java.util.ArrayList;

public class VenueFiltersHttpResult //INFO: Name coming from WebApi specifics
        extends HttpResult
        implements Serializable {

    @SerializedName("venueAccessibilityFilters")
    public ArrayList<Filter> VenueAccessibilityFilters;

    @SerializedName("venueAvailabilityFilters")
    public ArrayList<Filter> VenueAvailabilityFilters;

    @SerializedName("venueAtmosphereFilters")
    public ArrayList<Filter> VenueAtmosphereFilters;

    @SerializedName("venueCultureFilters")
    public ArrayList<Filter> VenueCultureFilters;

}
