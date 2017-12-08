package com.mosy.kalin.mosy.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_dish)
public class DishItemView
        extends RelativeLayout {

    String ImageId;
    Boolean IsUsingDefaultImageThumbnail;

    @Bean
    public com.mosy.kalin.mosy.Services.VenuesService VenuesService;

    @ViewById(resName = "menuListItem_tvName")
    TextView Name;
    @ViewById(resName = "menuListItem_tvVenueName")
    TextView VenueName;
    @ViewById(resName = "menuListItem_ivThumbnail")
    ImageView ImageThumbnail;
    //@ViewById(resName = "menuListItem_tvOpenedSinceUntil")
    //TextView OpenedSinceUntil;
    @ViewById(resName = "menuListItem_tvDistance")
    TextView DistanceFromDevice;
    @ViewById(resName = "menuListItem_tvWalkingTime")
    TextView WalkingTime;
    @ViewById(resName = "menuListItem_tvPriceTag")
    TextView PriceTag;
    @ViewById(resName = "menuListItem_tvRatingTag")
    TextView RatingTag;


    public DishItemView(Context context) {
        super(context);
    }

    public void bind(MenuListItem menuListItem) {
        this.Name.setText(menuListItem.Name);
        this.VenueName.setText(menuListItem.VenueName);
        if (menuListItem.ImageThumbnail != null) this.ImageId = menuListItem.ImageThumbnail.Id;

        if (menuListItem.DistanceToCurrentDeviceLocation > 0)
        {
            String distance = LocationHelper.buildDistanceText(menuListItem.DistanceToCurrentDeviceLocation);
            this.DistanceFromDevice.setText(distance);

            String timeWalking = LocationHelper.buildMinutesWalkingText(menuListItem.DistanceToCurrentDeviceLocation);
            timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());
            this.WalkingTime.setText(timeWalking);
        }

        if (menuListItem.ImageThumbnail != null && ArrayHelper.hasValidBitmapContent(menuListItem.ImageThumbnail.Bytes)){
            Bitmap bmp = BitmapFactory.decodeByteArray(menuListItem.ImageThumbnail.Bytes, 0, menuListItem.ImageThumbnail.Bytes.length);
            this.ImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            IsUsingDefaultImageThumbnail = false;
        }
        else {
            IsUsingDefaultImageThumbnail = true;
            this.ImageThumbnail.setImageResource(R.drawable.eat_paprika);
        }
    }

    @Click(resName = "menuListItem_ivThumbnail")
    public void ImageClick()
    {
        if (! IsUsingDefaultImageThumbnail){
            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            if (this.ImageId != null && this.ImageId.length() > 0) {
                byte[] byteArray = new AzureBlobService().GetBlob(this.ImageId, "userimages\\requestablealbums\\original");

                if (byteArray != null && byteArray.length > 0) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                    ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                    ivPreview.setImageBitmap(bmp);
                    nagDialog.show();
                }
            }
        }
    }

}
