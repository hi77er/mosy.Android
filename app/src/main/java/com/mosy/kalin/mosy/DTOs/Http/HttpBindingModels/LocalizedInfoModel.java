package com.mosy.kalin.mosy.DTOs.Http.HttpBindingModels;

import java.util.ArrayList;

public class LocalizedInfoModel {

    public String ItemId;
    ArrayList<String> CultureIds;

    public LocalizedInfoModel() { }
    public LocalizedInfoModel(
            String itemId,
            ArrayList<String> cultureIds)
    {
        this.ItemId = itemId;
        this.CultureIds = cultureIds;

    }

}
