package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueMenuBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueMenuBindingModel() { }
    public GetVenueMenuBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
