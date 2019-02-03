package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

public class RemoveLoginBindingModel {

    public String LoginProvider;
    public String ProviderKey;

    public RemoveLoginBindingModel() { }
    public RemoveLoginBindingModel(String loginProvider, String providerKey) {
        this.LoginProvider = loginProvider;
        this.ProviderKey = providerKey;
    }

}
