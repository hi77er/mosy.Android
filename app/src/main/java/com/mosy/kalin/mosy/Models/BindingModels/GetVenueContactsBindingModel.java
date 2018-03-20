package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueContactsBindingModel {

    public String VenueId;
    public String getVenueId() { return VenueId; }
    public void setVenueId(String venueId) { this.VenueId = venueId; }

    public GetVenueContactsBindingModel() { }

    public GetVenueContactsBindingModel(String venueId) {
        this.VenueId = venueId;
    }

}
