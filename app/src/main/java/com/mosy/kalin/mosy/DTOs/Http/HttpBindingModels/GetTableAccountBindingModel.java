package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

import com.mosy.kalin.mosy.Helpers.StringHelper;

public class GetTableAccountBindingModel {

    public String FBOId; //VenueId
    public String OpenerUsername;

    public GetTableAccountBindingModel() { }

    public GetTableAccountBindingModel(String venueId, String openerUsername) {
        this.FBOId = venueId;
        this.OpenerUsername = openerUsername;
    }

}
