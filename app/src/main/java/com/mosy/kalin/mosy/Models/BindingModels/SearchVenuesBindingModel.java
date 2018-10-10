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
    public String LocalDateTimeOffset;
    public boolean ShowNotWorkingVenues;
    public int SearchedDistanceMeters;

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
            boolean showNotWorkingVenues,
            String localDateTimeOffset,
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
        this.ShowNotWorkingVenues = showNotWorkingVenues;
        this.LocalDateTimeOffset = localDateTimeOffset;
        this.SearchedDistanceMeters = searchedDistanceMeters;

//        this.ApplyWorkingStatusFilter = applyWorkingStatusFilter;
    }

}
