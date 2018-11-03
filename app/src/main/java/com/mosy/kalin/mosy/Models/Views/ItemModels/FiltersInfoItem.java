package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;

public class FiltersInfoItem extends WallItemBase{

    public String MatchingFiltersInfo;
    public String MismatchingFiltersInfo;

    @Override
    public int getType() {
        return ITEM_TYPE_FILTERS_INFO_HEADER;
    }
}
