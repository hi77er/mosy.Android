package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueIndoorImageThumbnailBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueIndoorImageThumbnailBindingModel() { }

    public GetVenueIndoorImageThumbnailBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
