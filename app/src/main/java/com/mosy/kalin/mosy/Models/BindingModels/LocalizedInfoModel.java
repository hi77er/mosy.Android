package com.mosy.kalin.mosy.Models.BindingModels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
