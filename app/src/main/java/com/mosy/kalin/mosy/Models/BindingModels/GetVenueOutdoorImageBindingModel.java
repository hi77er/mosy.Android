package com.mosy.kalin.mosy.Models.BindingModels;

/**
 * Created by kkras on 8/9/2017.
 */

public class GetVenueOutdoorImageBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueOutdoorImageBindingModel() { }

    public GetVenueOutdoorImageBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
