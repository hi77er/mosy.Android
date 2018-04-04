package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueLocationBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueLocationBindingModel() { }
    public GetVenueLocationBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
