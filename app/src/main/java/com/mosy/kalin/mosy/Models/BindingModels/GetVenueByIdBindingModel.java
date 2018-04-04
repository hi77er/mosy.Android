package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueByIdBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueByIdBindingModel() { }
    public GetVenueByIdBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
