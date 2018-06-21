package com.mosy.kalin.mosy.Models.BindingModels;

import java.util.ArrayList;
import java.util.List;

public class SearchMenuListItemsBindingModel {

    public String AuthTokenHeader;
    public int MaxResultsCount;
    public int TotalItemsOffset;
    public String Query;
    public double Latitude;
    public double Longitude;
    public Boolean IsPromoted;
    public List<String> SelectedDishTypeFilterIds;
    public List<String> SelectedDishRegionFilterIds;
    public List<String> SelectedDishMainIngredientFilterIds;
    public List<String> SelectedDishAllergenFilterIds;
    public Integer LocalDayOfWeek;
    public String LocalTime;
    public int SearchedDistanceMeters;

    public SearchMenuListItemsBindingModel() { }
    public SearchMenuListItemsBindingModel(
            String authTokenHeader,
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            Boolean isPromoted,
            String query,
            ArrayList<String> selectedDishTypeFilterIds,
            ArrayList<String> selectedDishRegionFilterIds,
            ArrayList<String> selectedDishMainIngredientFilterIds,
            ArrayList<String> selectedDishAllergenFilterIds,
            Integer localDayOfWeek,
            String localTime,
            int searchedDistanceMeters)
    {
        this.AuthTokenHeader = authTokenHeader;
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.IsPromoted = isPromoted;
        this.Query = query;
        this.SelectedDishTypeFilterIds = selectedDishTypeFilterIds;
        this.SelectedDishRegionFilterIds = selectedDishRegionFilterIds;
        this.SelectedDishMainIngredientFilterIds = selectedDishMainIngredientFilterIds;
        this.SelectedDishAllergenFilterIds = selectedDishAllergenFilterIds;
        this.LocalDayOfWeek = localDayOfWeek;
        this.LocalTime= localTime;
        this.SearchedDistanceMeters = searchedDistanceMeters;
    }

}
