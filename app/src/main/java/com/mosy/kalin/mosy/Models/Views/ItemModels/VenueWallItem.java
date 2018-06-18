package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Venue;

public class VenueWallItem extends WallItemBase{

    public Venue Venue;

    @Override
    public int getType() {
        return ITEM_TYPE_VENUE_TILE;
    }
}
