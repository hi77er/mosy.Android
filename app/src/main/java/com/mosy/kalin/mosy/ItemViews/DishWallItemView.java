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
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.MenuListItemHelper;
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


    @ViewById(R.id.menuListItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.menuListItem_tvVenueName)
    TextView venueNameTextView;
    @ViewById(R.id.menuListItem_ivThumbnail)
    ImageView imageThumbnail;

    @ViewById(R.id.menuListItem_tvWorkingStatusLabel)
    TextView workingStatusLabel;
    @ViewById(R.id.menuListItem_tvRecommendedLabel)
    TextView recommendedLabel;
    @ViewById(R.id.menuListItem_tvNewLabel)
    TextView newLabel;

    @ViewById(R.id.menuListItem_tvDistance)
    TextView distanceFromDeviceTextView;
    @ViewById(R.id.menuListItem_tvWalkingTime)
    TextView walkingTimeTextView;
    @ViewById(R.id.menuListItem_tvPriceTag)
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

            MenuListItemCulture selectedCulture = MenuListItemHelper.getMenuListItemCulture(this.baseContext, menuListItem);

            this.nameTextView.setText(selectedCulture.MenuListItemName);
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

            this.distanceFromDeviceTextView.setVisibility(INVISIBLE);
            this.walkingTimeTextView.setVisibility(INVISIBLE);
            if (menuListItem.DistanceToCurrentDeviceLocation > 0)
            {
                String distance = LocationHelper.buildDistanceText(menuListItem.DistanceToCurrentDeviceLocation);
                if (StringHelper.isNotNullOrEmpty(distance)){
                    this.distanceFromDeviceTextView.setText(distance);
                    this.distanceFromDeviceTextView.setVisibility(VISIBLE);

                    String timeWalking = LocationHelper.buildMinutesWalkingText(menuListItem.DistanceToCurrentDeviceLocation);
                    if (StringHelper.isNotNullOrEmpty(timeWalking)) {
                        this.walkingTimeTextView.setText(timeWalking);
                        this.walkingTimeTextView.setVisibility(VISIBLE);
                    }
                }
            }

            if (menuListItem.PriceDisplayText != null) {
                this.priceTagTextVIew.setText(menuListItem.PriceDisplayText);
                this.priceTagTextVIew.setVisibility(VISIBLE);
            }

            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(menuListItem.VenueBusinessHours);
            this.workingStatusLabel.setVisibility(VISIBLE);
            switch (status){
                case Open:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusOpenedLabelTextView));
                    break;
                case Open247:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatus247LabelTextView));
                    break;
                case Closed:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusClosedLabelTextView));
                    break;
                case Unknown:
                    this.workingStatusLabel.setVisibility(GONE);
                    break;
            }
            this.recommendedLabel.setVisibility(menuListItem.IsRecommended ? VISIBLE : GONE);
            this.newLabel.setVisibility(menuListItem.IsNew ? VISIBLE : GONE);
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
