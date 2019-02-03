package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

import java.util.ArrayList;

public class GetItemPreferredCultureBindingModel {

    public String ItemId;
    public ArrayList<String> PreferredCultures;

    public GetItemPreferredCultureBindingModel() { }

    public GetItemPreferredCultureBindingModel(String itemId, ArrayList<String> preferredCultures) {
        this.ItemId = itemId;
        this.PreferredCultures = preferredCultures;
    }

}
