package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.WallVenue;

public class VenueWallItem extends WallItemBase{

    public WallVenue WallVenue;

    @Override
    public int getType() {
        return ITEM_TYPE_VENUE_TILE;
    }
}
