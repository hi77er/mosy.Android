package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;

public class DishWallItem extends WallItemBase {

    public WallMenuListItem WallMenuListItem;

    @Override
    public int getType() {
        return ITEM_TYPE_DISH_TILE;
    }
}
