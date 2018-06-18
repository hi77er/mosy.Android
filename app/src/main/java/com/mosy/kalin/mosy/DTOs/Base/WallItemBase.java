package com.mosy.kalin.mosy.DTOs.Base;

public abstract class WallItemBase {

    public static final int ITEM_TYPE_VENUE_TILE = 0;
    public static final int ITEM_TYPE_DISH_TILE = 1;
    public static final int ITEM_TYPE_FILTERS_INFO_HEADER = 2;

    abstract public int getType();

}
