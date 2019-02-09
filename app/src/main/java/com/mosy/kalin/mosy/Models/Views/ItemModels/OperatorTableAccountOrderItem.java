package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.OrderMenuItem;
import com.mosy.kalin.mosy.DTOs.TableAccount;

public class OperatorTableAccountOrderItem extends WallItemBase{

    public OrderMenuItem orderMenuItem;

    @Override
    public int getType() {
        return 0;
    }
}
