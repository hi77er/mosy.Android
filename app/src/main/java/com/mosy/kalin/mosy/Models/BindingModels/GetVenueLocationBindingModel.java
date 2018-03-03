package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueLocationBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueLocationBindingModel() { }

    public GetVenueLocationBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
