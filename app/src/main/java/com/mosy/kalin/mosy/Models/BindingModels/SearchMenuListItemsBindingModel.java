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
    public List<String> CuisinePhaseIds;
    public List<String> CuisineRegionIds;
    public List<String> CuisineSpectrumIds;
    public List<String> CuisineAllergensIds;
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
            ArrayList<String> phaseFilterIds,
            ArrayList<String> regionFilterIds,
            ArrayList<String> spectrumFilterIds,
            ArrayList<String> allergensFilterIds,
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
        this.CuisinePhaseIds = phaseFilterIds;
        this.CuisineRegionIds = regionFilterIds;
        this.CuisineSpectrumIds = spectrumFilterIds;
        this.CuisineAllergensIds = allergensFilterIds;
        this.LocalDayOfWeek = localDayOfWeek;
        this.LocalTime= localTime;
        this.SearchedDistanceMeters = searchedDistanceMeters;
    }

}
