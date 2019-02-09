package com.mosy.kalin.mosy.DTOs.Base;

public abstract class PromotionItemBase {

    public static final int ITEM_TYPE_PERCENTAGE_PROMOTION = 0;
    public static final int ITEM_TYPE_ITEM_PERCENTAGE_PROMOTION = 1;


    abstract public int getType();

}
