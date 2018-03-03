package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueMenuBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueMenuBindingModel() { }
    public GetVenueMenuBindingModel(String venueId) { this.VenueId = venueId; }

}
