package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.WallVenue;

public class OperatorTableAccountsVenueItem extends WallItemBase{

    public Venue Venue;

    @Override
    public int getType() {
        return 0;
    }
}
