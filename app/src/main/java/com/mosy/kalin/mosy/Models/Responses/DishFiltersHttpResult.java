package com.mosy.kalin.mosy.Models.Responses;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.io.Serializable;
import java.util.ArrayList;

public class DishFiltersHttpResult //INFO: Name coming from WebApi specifics
        extends HttpResult
        implements Serializable {

    @SerializedName("dishTypeFilters")
    public ArrayList<Filter> DishTypeFilters;

    @SerializedName("dishRegionFilters")
    public ArrayList<Filter> DishRegionFilters;

    @SerializedName("dishMainIngredientFilters")
    public ArrayList<Filter> DishMainIngredientFilters;

    @SerializedName("dishAllergenFilters")
    public ArrayList<Filter> DishAllergenFilters;

}
