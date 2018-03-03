package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueBusinessHoursBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueBusinessHoursBindingModel() { }

    public GetVenueBusinessHoursBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
