package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.DTOs.PercentagePromotion;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.MenuListItemHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.PromotionItemViewBase;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Views.ItemModels.PercentagePromotionItem;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_percentage_promotion)
public class PercentagePromotionItemView
        extends PromotionItemViewBase {

    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private PercentagePromotion PercentagePromotion;


    @ViewById(R.id.percentagePromotionItem_tvTitle)
    TextView tvTitle;
    @ViewById(R.id.percentagePromotionItem_tvValidTill)
    TextView tvValidTill;


    public PercentagePromotionItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(PercentagePromotion percentagePromotion) {

        if (percentagePromotion != null) {
            this.PercentagePromotion = percentagePromotion;

            String title = percentagePromotion.MenuItemName + ": " + percentagePromotion.PercentageOff + "%";
            this.tvTitle.setText(title);

            String validTill =  "Valid before: " + percentagePromotion.TillDisplayText;
            this.tvValidTill.setText(validTill);
        }
    }



}
