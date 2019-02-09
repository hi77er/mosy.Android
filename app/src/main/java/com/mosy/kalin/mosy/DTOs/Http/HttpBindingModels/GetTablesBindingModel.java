package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

public class GetTablesBindingModel {

    public String FBOId; //venueId

    public GetTablesBindingModel() { }

    public GetTablesBindingModel(String venueId) {
        this.FBOId = venueId;
    }

}
