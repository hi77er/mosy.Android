package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

public class AddExternalLoginBindingModel {

    public String ExternalAccessToken;

    public AddExternalLoginBindingModel() { }
    public AddExternalLoginBindingModel(String externalAccessToken) {
        this.ExternalAccessToken = externalAccessToken;
    }

}
