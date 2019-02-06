package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;

public class ClientTableAccountItem extends WallItemBase{

    public OrderMenuItem orderMenuItem;

    @Override
    public int getType() {
        return 0;
    }
}
