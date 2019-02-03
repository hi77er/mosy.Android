package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.TableAccount;
import com.mosy.kalin.mosy.DTOs.Venue;

public class OperatorTableAccountItem extends WallItemBase{

    public TableAccount tableAccount;

    @Override
    public int getType() {
        return 0;
    }
}
