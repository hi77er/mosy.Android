package com.mosy.kalin.mosy.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.MenuListItemsService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

/**
 * Created by kkras on 8/21/2017.
 */


@EViewGroup(R.layout.activity_item_menulistitem_details)
public class MenuListItemDetailsView
        extends RelativeLayout {

    private boolean IsUsingDefaultOutdoorImageThumbnail = true;
    private MenuListItem MenuListItem;

    public MenuListItemDetailsView(Context context) {
        super(context);
    }

    @Bean
    public MenuListItemsService MenuListItemsService;

    @ViewById(resName = "menuListItemDetails_ivMainImage")
    ImageView MenuListItemMainImage;

    @ViewById(resName = "menuListItemDetails_tvIngredients")
    TextView Ingredients;
    ///Add more controls here

    public void bind(MenuListItem menuListItem) {
        if (menuListItem != null){
            this.MenuListItem = menuListItem;
            ArrayList<String> toJoin = new ArrayList<String>();
            String joined = StringHelper.empty();
            for (Ingredient ingredient: menuListItem.Ingredients)
                toJoin.add(ingredient.Name);
            joined = StringHelper.join(", ", toJoin);
            this.Ingredients.setText(joined);

            MenuListItemImage mainImage = this.MenuListItemsService.downloadMenuListItemImageThumbnail(menuListItem.Id);
            if (mainImage != null && mainImage.Bytes != null){
                byte[] byteArray = Base64.decode(mainImage.Bytes, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                this.MenuListItemMainImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                this.IsUsingDefaultOutdoorImageThumbnail = false;
            }
            else {
                this.IsUsingDefaultOutdoorImageThumbnail = true;
                this.MenuListItemMainImage.setImageResource(R.drawable.drink_default);
            }
        }
    }


    @Click(resName = "menuListItemDetails_ivMainImage")
    public void ItemClick()
    {
        if (! IsUsingDefaultOutdoorImageThumbnail){
            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            MenuListItemImage image = this.MenuListItemsService.downloadMenuListItemImage(this.MenuListItem.Id);
            if (image.Bytes != null){
                byte[] byteArray = Base64.decode(image.Bytes, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                ivPreview.setImageBitmap(bmp);
                nagDialog.show();
            }
        }
    }

}
