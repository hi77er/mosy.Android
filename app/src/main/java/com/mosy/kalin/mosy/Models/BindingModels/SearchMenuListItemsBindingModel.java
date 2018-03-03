package com.mosy.kalin.mosy.Models.BindingModels;

import java.util.ArrayList;
import java.util.List;

public class SearchMenuListItemsBindingModel {

    public int MaxResultsCount;
    public int TotalItemsOffset;
    public String Query;
    public double Latitude;
    public double Longitude;
    public Boolean IsPromoted;
    public List<String> CuisinePhaseIds;
    public List<String> CuisineRegionIds;
    public List<String> CuisineSpectrumIds;
    public Integer LocalDayOfWeek;
    public String LocalTime;

    public SearchMenuListItemsBindingModel() { }

    public SearchMenuListItemsBindingModel(
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            Boolean isPromoted,
            String query,
            ArrayList<String> phaseFilterIds,
            ArrayList<String> regionFilterIds,
            ArrayList<String> spectrumFilterIds,
            Integer localDayOfWeek,
            String localTime)
    {
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.IsPromoted = isPromoted;
        this.Query = query;
        this.CuisinePhaseIds = phaseFilterIds;
        this.CuisineRegionIds = regionFilterIds;
        this.CuisineSpectrumIds = spectrumFilterIds;
        this.LocalDayOfWeek = localDayOfWeek;
        this.LocalTime= localTime;
    }

}
