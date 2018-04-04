package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.DishFilter;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.io.Serializable;
import java.util.ArrayList;

public class RequestableFiltersResult //INFO: Name coming from WebApi specifics
        extends ResultBase
        implements Serializable {

    @SerializedName("CuisinePhaseFilters")
    public ArrayList<DishFilter> CuisinePhaseFilters;

    @SerializedName("CuisineRegionFilters")
    public ArrayList<DishFilter> CuisineRegionFilters;

    @SerializedName("CuisineSpectrumFilters")
    public ArrayList<DishFilter> CuisineSpectrumFilters;

    @SerializedName("CuisineAllergensFilters")
    public ArrayList<DishFilter> CuisineAllergensFilters;

}
