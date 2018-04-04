package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueContactsBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueContactsBindingModel() { }
    public GetVenueContactsBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
