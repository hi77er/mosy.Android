package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueBusinessHoursBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueBusinessHoursBindingModel() { }
    public GetVenueBusinessHoursBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
