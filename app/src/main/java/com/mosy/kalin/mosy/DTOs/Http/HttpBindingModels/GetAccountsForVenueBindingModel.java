package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

import java.util.ArrayList;

public class GetAccountsForVenueBindingModel {


    public String FBOId;

    public GetAccountsForVenueBindingModel() { }

    public GetAccountsForVenueBindingModel(String venueId) {
        this.FBOId = venueId;
    }

}
