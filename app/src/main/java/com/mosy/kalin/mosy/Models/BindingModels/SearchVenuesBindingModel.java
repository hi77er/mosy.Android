package com.mosy.kalin.mosy.Models.BindingModels;

public class SearchVenuesBindingModel {

    public double Latitude;
    public double Longitude;
    public String Query;
    public int MaxResultsCount;
    public int TotalItemsOffset;
    public Integer LocalDayOfWeek;
    public String LocalTime;

//    public boolean ApplyWorkingStatusFilter;

    public SearchVenuesBindingModel() { }

    public SearchVenuesBindingModel(
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            String query,
            Integer localDayOfWeek,
            String localTime) {

        this.Latitude = latitude;
        this.Longitude = longitude;
        this.Query = query;
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.LocalDayOfWeek = localDayOfWeek;
        this.LocalTime = localTime;

//        this.ApplyWorkingStatusFilter = applyWorkingStatusFilter;
    }

}
