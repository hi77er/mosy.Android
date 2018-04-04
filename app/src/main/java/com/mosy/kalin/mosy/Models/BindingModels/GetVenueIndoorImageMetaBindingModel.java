package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueIndoorImageMetaBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueIndoorImageMetaBindingModel() { }
    public GetVenueIndoorImageMetaBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
