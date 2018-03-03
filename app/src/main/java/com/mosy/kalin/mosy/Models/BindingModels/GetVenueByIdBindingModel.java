package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueByIdBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueByIdBindingModel() { }

    public GetVenueByIdBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
