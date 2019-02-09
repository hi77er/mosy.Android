package com.mosy.kalin.mosy.ItemViews;

import android.content.Context;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.ItemPercentagePromotion;
import com.mosy.kalin.mosy.ItemViews.Base.PromotionItemViewBase;
import com.mosy.kalin.mosy.Models.Views.ItemModels.ItemPercentagePromotionItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.PercentagePromotionItem;
import com.mosy.kalin.mosy.R;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_item_percentage_promotion)
public class ItemPercentagePromotionItemView
        extends PromotionItemViewBase {

    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private ItemPercentagePromotion ItemPercentagePromotion;


    @ViewById(R.id.itemPercentagePromotionItem_tvTitle)
    TextView tvTitle;
    @ViewById(R.id.itemPercentagePromotionItem_tvValidTill)
    TextView tvValidTill;


    public ItemPercentagePromotionItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(ItemPercentagePromotion itemPercentagePromotion) {

        if (itemPercentagePromotion != null) {
            this.ItemPercentagePromotion = itemPercentagePromotion;

            String title = itemPercentagePromotion.MenuItemName + ": " + itemPercentagePromotion.PercentageOff + "%";
            this.tvTitle.setText(title);

            String validTill =  "Valid before: " + itemPercentagePromotion.TillDisplayText;
            this.tvValidTill.setText(validTill);
        }
    }



}
