package com.mosy.kalin.mosy.Models.BindingModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkras on 8/7/2017.
 */

public class SearchMenuListItemsBindingModel {

    public int MaxResultsCount;
    public String Query;
    public double Latitude;
    public double Longitude;
    public List<String> SelectedCuisinePhaseIds;
    public List<String> SelectedCuisineRegionIds;
    public List<String> SelectedCuisineSpectrumIds;

    public SearchMenuListItemsBindingModel() { }
    public SearchMenuListItemsBindingModel(
            int maxResultsCount,
            double latitude,
            double longitude,
            String query,
            ArrayList<String> phaseFilterIds,
            ArrayList<String> regionFilterIds,
            ArrayList<String> spectrumFilterIds)
    {
        this.MaxResultsCount = maxResultsCount;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Query = query;
        this.SelectedCuisinePhaseIds = phaseFilterIds;
        this.SelectedCuisineRegionIds = regionFilterIds;
        this.SelectedCuisineSpectrumIds = spectrumFilterIds;
    }

}
