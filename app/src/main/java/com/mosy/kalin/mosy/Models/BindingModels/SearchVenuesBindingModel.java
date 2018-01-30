package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/7/2017.
 */

public class SearchVenuesBindingModel {

    public int MaxResultsCount;
    public int getMaxResultsCount() { return MaxResultsCount; }
    public void setMaxResultsCount(int maxResultsCount) { this.MaxResultsCount = maxResultsCount; }

    public int TotalItemsOffset;
    public int getTotalItemsOffset() { return TotalItemsOffset; }
    public void setTotalItemsOffset(int totalItemsOffset) { this.TotalItemsOffset = totalItemsOffset; }

    public String Query;
    public String getQuery() { return Query; }
    public void setQuery(String query) { this.Query = query; }

    public double Latitude;
    public double getLatitude() { return Latitude; }
    public void setLatitude(double latitude) { this.Latitude = latitude; }

    public double Longitude;
    public double getLongitude() { return Longitude; }
    public void setLongitude(double longitude) { this.Longitude = longitude; }

    public SearchVenuesBindingModel() { }

    public SearchVenuesBindingModel(
            int maxResultsCount,
            int totalItemsOffset,
            double latitude,
            double longitude,
            String query) {
        this.MaxResultsCount = maxResultsCount;
        this.TotalItemsOffset = totalItemsOffset;
        this.Query = query;
        this.Latitude = latitude;
        this.Longitude = longitude;
    }

}
