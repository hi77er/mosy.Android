package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Table;
import com.mosy.kalin.mosy.DTOs.TableAccount;

public class TableItem extends WallItemBase{

    public Table table;

    @Override
    public int getType() {
        return 0;
    }
}
