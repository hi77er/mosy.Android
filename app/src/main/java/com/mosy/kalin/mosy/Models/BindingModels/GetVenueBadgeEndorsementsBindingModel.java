package com.mosy.kalin.mosy.Models.BindingModels;

public class GetVenueBadgeEndorsementsBindingModel {

    public String AuthTokenHeader;
    public String VenueId;

    public GetVenueBadgeEndorsementsBindingModel() { }

    public GetVenueBadgeEndorsementsBindingModel(String authTokenHeader, String venueId) {
        this.AuthTokenHeader = authTokenHeader;
        this.VenueId = venueId;
    }

}
