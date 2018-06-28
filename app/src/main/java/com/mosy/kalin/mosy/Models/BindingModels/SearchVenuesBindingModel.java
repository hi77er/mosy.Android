package com.mosy.kalin.mosy.Models.BindingModels;

import java.util.List;

public class SearchVenuesBindingModel {

    public String AuthTokenHeader;
    public double Latitude;
    public double Longitude;
    public String Query;
    public int MaxResultsCount;
    public int TotalItemsOffset;
    public List<String> SelectedVenueAccessibilityFilterIds;
    public List<String> SelectedVenueAvailabilityFilterIds;
    public List<String> SelectedVenueAtmosphereFilterIds;
    public List<String> SelectedVenueCultureFilterIds;
    public Integer LocalDayOfWeek;
    public String LocalTime;
    public int SearchedDistanceMeters;

//    public boolean ApplyWorkingStatusFilter;

    public SearchVenuesBindingModel() { }

    public SearchVenuesBindingModel(
            String authTokenHeader,
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            String query,
            List<String> selectedVenueAccessibilityFilterIds,
            List<String> selectedVenueAvailabilityFilterIds,
            List<String> selectedVenueAtmosphereFilterIds,
            List<String> selectedVenueCultureFilterIds,
            Integer localDayOfWeek,
            String localTime,
            int searchedDistanceMeters) {

        this.AuthTokenHeader = authTokenHeader;
        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Query = query;
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.SelectedVenueAccessibilityFilterIds = selectedVenueAccessibilityFilterIds;
        this.SelectedVenueAvailabilityFilterIds = selectedVenueAvailabilityFilterIds;
        this.SelectedVenueAtmosphereFilterIds = selectedVenueAtmosphereFilterIds;
        this.SelectedVenueCultureFilterIds = selectedVenueCultureFilterIds;
        this.LocalDayOfWeek = localDayOfWeek;
        this.LocalTime = localTime;
        this.SearchedDistanceMeters = searchedDistanceMeters;

//        this.ApplyWorkingStatusFilter = applyWorkingStatusFilter;
    }

}
