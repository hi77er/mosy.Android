package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_dish)
public class DishWallItemView
        extends WallItemViewBase {

    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private MenuListItem MenuListItem;


    @ViewById(resName = "menuListItem_tvName")
    TextView nameTextView;
    @ViewById(resName = "menuListItem_tvVenueName")
    TextView venueNameTextView;
    @ViewById(resName = "menuListItem_ivThumbnail")
    ImageView imageThumbnail;

    @ViewById(resName = "menuListItem_tvWorkingStatus")
    TextView workingStatusTextView;
    @ViewById(resName = "menuListItem_tvDistance")
    TextView distanceFromDeviceTextView;
    @ViewById(resName = "menuListItem_tvWalkingTime")
    TextView walkingTimeTextView;
    @ViewById(resName = "menuListItem_tvPriceTag")
    TextView priceTagTextVIew;
//    @ViewById(resName = "menuListItem_tvRatingTag")
//    TextView ratingTagTextView;

    public DishWallItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(MenuListItem menuListItem) {
        this.imageThumbnail.setImageDrawable(null);

        if (menuListItem != null) {
            this.MenuListItem = menuListItem;
            this.nameTextView.setText(menuListItem.Name);
            this.venueNameTextView.setText(menuListItem.VenueName);

            if (menuListItem.ImageThumbnail != null && menuListItem.ImageThumbnail.Bitmap != null)
                this.imageThumbnail.setImageBitmap(menuListItem.ImageThumbnail.Bitmap);

            if (menuListItem.ImageThumbnail != null && menuListItem.ImageThumbnail.Bitmap != null) {
                this.imageThumbnail.setImageBitmap(menuListItem.ImageThumbnail.Bitmap);
                IsUsingDefaultThumbnail = false;
            }
            else {
                Bitmap defaultImageBitmap = BitmapFactory.decodeResource(this.baseContext.getResources(), R.drawable.eat_paprika_100x100);
                this.imageThumbnail.setImageBitmap(defaultImageBitmap);
                IsUsingDefaultThumbnail = true;
            }

            if (menuListItem.DistanceToCurrentDeviceLocation > 0)
            {
                String distance = LocationHelper.buildDistanceText(menuListItem.DistanceToCurrentDeviceLocation);
                this.distanceFromDeviceTextView.setText(distance);

                String timeWalking = LocationHelper.buildMinutesWalkingText(menuListItem.DistanceToCurrentDeviceLocation);
                timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());
                if (!timeWalking.equals(StringHelper.empty())) {
                    this.walkingTimeTextView.setText(timeWalking);
                    this.walkingTimeTextView.setVisibility(VISIBLE);
                } else {
                    this.walkingTimeTextView.setVisibility(GONE);
                }
            }

            if (menuListItem.PriceDisplayText != null) {
                this.priceTagTextVIew.setText(menuListItem.PriceDisplayText);
                this.priceTagTextVIew.setVisibility(VISIBLE);
            }

            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(menuListItem.VenueBusinessHours);
            switch (status){
                case Open:
                    this.workingStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.open_moss_nopadding, 0, 0, 0);
                    break;
                case Open247:
                    this.workingStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.open247_emerald_nopadding, 0, 0, 0);
                    break;
                case Closed:
                    this.workingStatusTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.closed_salmon_nopadding, 0, 0, 0);
                    break;
                case Unknown: break;
            }
        }
    }

    @Click(resName = "menuListItem_ivThumbnail")
    public void ImageClick()
    {
        if (!this.IsUsingDefaultThumbnail && this.MenuListItem != null && this.MenuListItem.ImageThumbnail != null && StringHelper.isNotNullOrEmpty(this.MenuListItem.ImageThumbnail.Id)){

            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);
            nagDialog.show();

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
//                    progressBar.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                        ivPreview.setImageBitmap(bmp);
                    } else
                        throw new NullPointerException("Image not found");
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.MenuListItem.ImageThumbnail.Id, originalBlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

}
