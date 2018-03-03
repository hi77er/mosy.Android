package com.mosy.kalin.mosy.Views;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.mosy.kalin.mosy.Services.VenueService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_dish)
public class DishItemView extends RelativeLayout {

    private static final String thumbnailBlobStorageContainerPath = "userimages\\requestablealbums\\100x100";
    private static final String originalBlobStorageContainerPath = "userimages\\requestablealbums\\original";
    private LruCache<String, Bitmap> inMemoryCache;
    private String ImageId;
    private boolean IsUsingDefaultImageThumbnail;

    @Bean
    public VenueService VenuesService;

    @ViewById(resName = "menuListItem_tvName")
    TextView Name;
    @ViewById(resName = "menuListItem_tvVenueName")
    TextView VenueName;
    @ViewById(resName = "menuListItem_ivThumbnail")
    ImageView ImageThumbnail;

    @ViewById(resName = "menuListItem_tvWorkingStatus")
    TextView WorkingStatus;
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

    public void bind(MenuListItem menuListItem, LruCache<String, Bitmap> cache) {
        this.inMemoryCache = cache;
        this.Name.setText(menuListItem.Name);
        this.VenueName.setText(menuListItem.VenueName);
        if (menuListItem.ImageThumbnail != null) this.ImageId = menuListItem.ImageThumbnail.Id;

        if (menuListItem.DistanceToCurrentDeviceLocation > 0)
        {
            String distance = LocationHelper.buildDistanceText(menuListItem.DistanceToCurrentDeviceLocation);
            this.DistanceFromDevice.setText(distance);

            String timeWalking = LocationHelper.buildMinutesWalkingText(menuListItem.DistanceToCurrentDeviceLocation);
            timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());
            if (!timeWalking.equals(StringHelper.empty())) {
                this.WalkingTime.setText(timeWalking);
                this.WalkingTime.setVisibility(VISIBLE);
            } else {
                this.WalkingTime.setVisibility(GONE);
            }
        }

        if (menuListItem.PriceDisplayText != null) {
            this.PriceTag.setText(menuListItem.PriceDisplayText);
            this.PriceTag.setVisibility(VISIBLE);
        }

        WorkingStatus status = BusinessHoursHelper.getWorkingStatus(menuListItem.VenueBusinessHours);
        switch (status){
            case Open:
                this.WorkingStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.open_moss_nopadding, 0, 0, 0);
                break;
            case Open247:
                this.WorkingStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.open247_emerald_nopadding, 0, 0, 0);
                break;
            case Closed:
                this.WorkingStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.closed_salmon_nopadding, 0, 0, 0);
                break;
            case Unknown: break;
        }

        final String imageKey = menuListItem.ImageThumbnail != null ? menuListItem.ImageThumbnail.Id : "default";
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null) {
            ImageThumbnail.setImageBitmap(bitmap);
        } else {
            ImageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
            this.downloadMenuListItemThumbnail(imageKey);
        }
    }

    private void downloadMenuListItemThumbnail(String thumbnailId) {
        AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(byte[] bytes) {
                if (ArrayHelper.hasValidBitmapContent(bytes)){
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    ImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    IsUsingDefaultImageThumbnail = false;
                    addBitmapToMemoryCache(thumbnailId, bmp);
                }
                else {
                    IsUsingDefaultImageThumbnail = true;
                    ImageThumbnail.setImageResource(R.drawable.eat_paprika_100x100);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        DownloadBlobModel model = new DownloadBlobModel(thumbnailId, thumbnailBlobStorageContainerPath);
        new LoadAzureBlobAsyncTask(listener).execute(model);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            inMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return inMemoryCache.get(key);
    }

    @Click(resName = "menuListItem_ivThumbnail")
    public void ImageClick()
    {
        if (!IsUsingDefaultImageThumbnail
                && this.ImageId != null
                && !this.ImageId.equals(StringHelper.empty())){

            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);
            nagDialog.show();

            if (this.ImageId != null && this.ImageId.length() > 0) {
                AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                    @Override
                    public void onPreExecute() {
//                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPostExecute(byte[] bytes) {
                        if (ArrayHelper.hasValidBitmapContent(bytes)){
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                            ivPreview.setImageBitmap(bmp);
                        } else
                            throw new NullPointerException("Image not found");
                    }
                };

                DownloadBlobModel model = new DownloadBlobModel(this.ImageId, originalBlobStorageContainerPath);
                new LoadAzureBlobAsyncTask(listener).execute(model);
            }
        }
    }

}
