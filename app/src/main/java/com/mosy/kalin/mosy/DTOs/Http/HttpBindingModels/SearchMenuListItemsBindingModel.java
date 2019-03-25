package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

import java.util.ArrayList;
import java.util.List;

public class SearchMenuListItemsBindingModel {

    public String AuthTokenHeader;
    public int MaxResultsCount;
    public int TotalItemsOffset;
    public String Query;
    public double Latitude;
    public double Longitude;
    public List<String> SelectedDishTypeFilterIds;
    public List<String> SelectedDrinksFilterIds;
    public List<String> SelectedDishRegionFilterIds;
    public List<String> SelectedDishMainIngredientFilterIds;
    public List<String> SelectedDishAllergenFilterIds;
    public String LocalDateTimeOffset;
    public int SearchedDistanceMeters;
    public boolean ShowNotRecommendedDishes;
    public boolean ShowNotWorkingVenues;
    public boolean IsDevModeActivated;

    public SearchMenuListItemsBindingModel() { }
    public SearchMenuListItemsBindingModel(
            String authTokenHeader,
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            String query,
            ArrayList<String> selectedDishTypeFilterIds,
            ArrayList<String> selectedDrinksFilterIds,
            ArrayList<String> selectedDishRegionFilterIds,
            ArrayList<String> selectedDishMainIngredientFilterIds,
            ArrayList<String> selectedDishAllergenFilterIds,
            boolean showNotRecommendedDishes,
            boolean showNotWorkingVenues,
            String localDateTimeOffset,
            int searchedDistanceMeters,
            boolean isDevModeActivated)
    {
        this.AuthTokenHeader = authTokenHeader;
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Query = query;
        this.SelectedDishTypeFilterIds = selectedDishTypeFilterIds;
        this.SelectedDrinksFilterIds = selectedDrinksFilterIds;
        this.SelectedDishRegionFilterIds = selectedDishRegionFilterIds;
        this.SelectedDishMainIngredientFilterIds = selectedDishMainIngredientFilterIds;
        this.SelectedDishAllergenFilterIds = selectedDishAllergenFilterIds;
        this.ShowNotRecommendedDishes = showNotRecommendedDishes;
        this.ShowNotWorkingVenues = showNotWorkingVenues;
        this.LocalDateTimeOffset = localDateTimeOffset;
        this.SearchedDistanceMeters = searchedDistanceMeters;
        this.IsDevModeActivated = isDevModeActivated;

    }

}
