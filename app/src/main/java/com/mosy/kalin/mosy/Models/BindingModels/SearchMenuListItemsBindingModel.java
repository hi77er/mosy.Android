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
    public Boolean FilterByWorkingStatus;
    public List<String> SelectedCuisinePhaseIds;
    public List<String> SelectedCuisineRegionIds;
    public List<String> SelectedCuisineSpectrumIds;

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
            ArrayList<String> spectrumFilterIds)
    {
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.IsPromoted = isPromoted;
        this.Query = query;
        this.SelectedCuisinePhaseIds = phaseFilterIds;
        this.SelectedCuisineRegionIds = regionFilterIds;
        this.SelectedCuisineSpectrumIds = spectrumFilterIds;
    }

}
