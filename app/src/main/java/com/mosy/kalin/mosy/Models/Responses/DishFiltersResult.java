package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

import java.io.Serializable;
import java.util.ArrayList;

public class DishFiltersResult //INFO: Name coming from WebApi specifics
        extends ResultBase
        implements Serializable {

    @SerializedName("DishTypeFilters")
    public ArrayList<Filter> DishTypeFilters;

    @SerializedName("DishRegionFilters")
    public ArrayList<Filter> DishRegionFilters;

    @SerializedName("DishMainIngredientFilters")
    public ArrayList<Filter> DishMainIngredientFilters;

    @SerializedName("DishAllergenFilters")
    public ArrayList<Filter> DishAllergenFilters;

}
