package com.mosy.kalin.mosy.ItemViews;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DetailsVenueActivity_;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.VenueMenuActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_venue)
public class VenueWallItemView
        extends WallItemViewBase {

    private static final String originalBlobStorageContainerPath = "userimages\\fboalbums\\original";
    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private Venue Venue;


    @ViewById(resName = "venueItem_tvName")
    TextView Name;
    @ViewById(resName = "venueItem_tvClass")
    TextView Class;

    @ViewById(resName = "venueItem_tvDistance")
    TextView DistanceFromDevice;
    @ViewById(resName = "venueItem_tvWalkingMinutes")
    TextView WalkingMinutes;
    @ViewById(resName = "venueItem_ivOutdoorThumbnail")
    ImageView OutdoorImageThumbnail;

    @ViewById(R.id.venueItem_tvWorkingStatusLabel)
    TextView workingStatusLabel;
//    @ViewById(R.id.venueItem_tvRecommendedLabel)
//    TextView newLabel;

    public VenueWallItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(Venue venue) {
        this.OutdoorImageThumbnail.setImageDrawable(null);

        if (venue != null) {
            this.Venue = venue;

            this.Name.setText(venue.Name);
            this.Class.setText(venue.Class);

            if (venue.OutdoorImage != null && venue.OutdoorImage.Bitmap != null) {
                this.OutdoorImageThumbnail.setImageBitmap(venue.OutdoorImage.Bitmap);
                IsUsingDefaultThumbnail = false;
            }
            else {
                Bitmap defaultImageBitmap = BitmapFactory.decodeResource(this.baseContext.getResources(), R.drawable.venue_default_thumbnail);
                this.OutdoorImageThumbnail.setImageBitmap(defaultImageBitmap);
                IsUsingDefaultThumbnail = true;
            }

//            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(venue.VenueBusinessHours);
            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(venue.WorkingStatus);
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

            if (venue.DistanceToCurrentDeviceLocation > 0)
            {
                String distance = LocationHelper.buildDistanceText(venue.DistanceToCurrentDeviceLocation);
                this.DistanceFromDevice.setText(distance);
                this.WalkingMinutes.setVisibility(View.VISIBLE);

                String timeWalking = LocationHelper.buildMinutesWalkingText(venue.DistanceToCurrentDeviceLocation);
                timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());

                if (!timeWalking.equals(StringHelper.empty())) {
                    this.WalkingMinutes.setText(timeWalking);
                    this.WalkingMinutes.setVisibility(View.VISIBLE);
                } else {
                    this.WalkingMinutes.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Click(resName = "venueItem_ivOutdoorThumbnail")
    public void OutdoorThumbnailClick()
    {
        if (!IsUsingDefaultThumbnail && this.Venue != null && this.Venue.OutdoorImage != null && StringHelper.isNotNullOrEmpty(this.Venue.OutdoorImage.Id)){

            final Dialog nagDialog = new Dialog(this.baseContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);
            nagDialog.show();

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override
                public void onPreExecute() {
//                    progressBar.setVisibility(View.VISIBLE);
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

            DownloadBlobModel model = new DownloadBlobModel(this.Venue.OutdoorImage.Id, originalBlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

//    @Click(resName = "venueItem_ivMenu")
    @Click(resName = "venueItem_btnMenu")
    public void MenuLinkClick()
    {
        Intent intent = new Intent(this.baseContext, VenueMenuActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        this.Venue.VenueBusinessHours = null;
        intent.putExtra("Venue", this.Venue);
        this.baseContext.startActivity(intent);
    }

//    @Click(resName = "venueItem_ivInfo")
    @Click(resName = "venueItem_btnInfo")
    public void InfoLinkClick()
    {
        Intent intent = new Intent(this.baseContext, DetailsVenueActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        this.Venue.VenueBusinessHours = null;
        intent.putExtra("Venue", this.Venue);
        this.baseContext.startActivity(intent);
    }

}
