package com.mosy.kalin.mosy.Models.BindingModels;

public class GetRequestableFiltersBindingModel {

    public String AuthTokenHeader;

    public GetRequestableFiltersBindingModel(){}
    public GetRequestableFiltersBindingModel(String authTokenHeader){
        this.AuthTokenHeader = authTokenHeader;
    }
}
