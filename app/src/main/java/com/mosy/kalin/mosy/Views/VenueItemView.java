package com.mosy.kalin.mosy.Views;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.VenueService;
import com.mosy.kalin.mosy.VenueActivity_;
import com.mosy.kalin.mosy.VenueDetailsActivity_;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_venue)
public class VenueItemView
        extends RelativeLayout {

    private static final String originalBlobStorageContainerPath = "userimages\\fboalbums\\original";
    private static final String thumbnailBlobStorageContainerPath = "userimages\\fboalbums\\100x100";
    private Venue Venue;
    private String ImageId;
    private boolean IsUsingDefaultThumbnail;
    private LruCache<String, Bitmap> mMemoryCache;

    @Bean
    public VenueService VenuesService;

    @ViewById(resName = "venueItem_tvName")
    TextView Name;
    @ViewById(resName = "venueItem_tvClass")
    TextView Class;
    @ViewById(resName = "menuListItem_tvWorkingStatus")
    TextView WorkingStatus;
    @ViewById(resName = "venueItem_tvDistance")
    TextView DistanceFromDevice;
    @ViewById(resName = "venueItem_tvWalkingMinutes")
    TextView WalkingMinutes;
    @ViewById(resName = "venueItem_ivOutdoorThumbnail")
    ImageView OutdoorImageThumbnail;

    public VenueItemView(Context context) {
        super(context);
    }

    public void bind(Venue venue, LruCache<String, Bitmap> cache) {
        this.mMemoryCache = cache;
        this.Venue = venue;
        if (venue.OutdoorImage != null)
            this.ImageId = venue.OutdoorImage.Id;

        this.Name.setText(venue.Name);
        this.Class.setText(venue.Class);

        com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus status = BusinessHoursHelper.getWorkingStatus(venue.VenueBusinessHours);
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

        if (venue.DistanceToCurrentDeviceLocation > 0)
        {
            String distance = LocationHelper.buildDistanceText(venue.DistanceToCurrentDeviceLocation);
            this.DistanceFromDevice.setText(distance);

            String timeWalking = LocationHelper.buildMinutesWalkingText(venue.DistanceToCurrentDeviceLocation);
            timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());

            if (!timeWalking.equals(StringHelper.empty())) {
                this.WalkingMinutes.setText(timeWalking);
                this.WalkingMinutes.setVisibility(VISIBLE);
            } else {
                this.WalkingMinutes.setVisibility(GONE);
            }
        }

        final String imageKey = venue.OutdoorImage != null ? venue.OutdoorImage.Id : "default";
        final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null) {
            OutdoorImageThumbnail.setImageBitmap(bitmap);
        } else {
            OutdoorImageThumbnail.setImageResource(R.drawable.venue_default_thumbnail);
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
                    OutdoorImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    IsUsingDefaultThumbnail = false;
                    addBitmapToMemoryCache(thumbnailId, bmp);
                }
                else {
                    IsUsingDefaultThumbnail = true;
                    OutdoorImageThumbnail.setImageResource(R.drawable.venue_default_thumbnail);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        DownloadBlobModel model = new DownloadBlobModel(thumbnailId, thumbnailBlobStorageContainerPath);
        new LoadAzureBlobAsyncTask(listener).execute(model);
    }

    @Click(resName = "venueItem_ivOutdoorThumbnail")
    public void OutdoorThumbnailClick()
    {
        if (!IsUsingDefaultThumbnail
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

    @Click(resName = "venueItem_ivMenu")
    public void MenuLinkClick()
    {
        Intent intent = new Intent(this.getContext(), VenueActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        this.Venue.VenueBusinessHours = null;
        intent.putExtra("Venue", this.Venue);
        this.getContext().startActivity(intent);
    }

    @Click(resName = "venueItem_ivInfo")
    public void InfoLinkClick()
    {
        Intent intent = new Intent(this.getContext(), VenueDetailsActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        this.Venue.VenueBusinessHours = null;
        intent.putExtra("Venue", this.Venue);
        this.getContext().startActivity(intent);
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}
