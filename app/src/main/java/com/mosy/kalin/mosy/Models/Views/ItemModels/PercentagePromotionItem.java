package com.mosy.kalin.mosy.Models.Views.ItemModels;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Base.PromotionItemBase;
import com.mosy.kalin.mosy.DTOs.PercentagePromotion;

public class PercentagePromotionItem extends PromotionItemBase {

    public PercentagePromotion PercentagePromotion;


    @Override
    public int getType() {
        return PromotionItemBase.ITEM_TYPE_PERCENTAGE_PROMOTION;
    }
}
