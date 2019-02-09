package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Base.PromotionItemBase;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.ItemPercentagePromotion;

public class ItemPercentagePromotionItem extends PromotionItemBase {

    public ItemPercentagePromotion ItemPercentagePromotion;


    @Override
    public int getType() {
        return PromotionItemBase.ITEM_TYPE_ITEM_PERCENTAGE_PROMOTION;
    }
}
