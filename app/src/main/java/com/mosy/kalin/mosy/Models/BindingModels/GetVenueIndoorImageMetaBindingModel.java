package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueIndoorImageMetaBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueIndoorImageMetaBindingModel() { }

    public GetVenueIndoorImageMetaBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
