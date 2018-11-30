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

import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DetailsVenueActivity_;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AzureBlobService;
import com.mosy.kalin.mosy.VenueMenuActivity_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_venue)
public class VenueWallItemView
        extends WallItemViewBase {

    private boolean IsUsingDefaultThumbnail;

    private Context baseContext;
    private Venue Venue;

    @ViewById(R.id.venueItem_ivInteriorThumbnail)
    ImageView interiorImageThumbnail;

    @ViewById(R.id.venueItem_tvName)
    TextView nameTextView;
    @ViewById(R.id.venueItem_tvClass)
    TextView classTextView;
    @ViewById(R.id.venueItem_tvDistance)
    TextView distanceFromDeviceTextView;
    @ViewById(R.id.venueItem_tvWalkingMinutes)
    TextView walkingMinutesTextView;
    @ViewById(R.id.venueItem_tvWorkingStatusLabel)
    TextView workingStatusLabel;

    public VenueWallItemView(Context context) {
        super(context);
        this.baseContext = context;
    }

    public void bind(Venue venue) {
        this.interiorImageThumbnail.setImageDrawable(null);

        if (venue != null) {
            this.Venue = venue;

            this.nameTextView.setText(venue.Name);
            this.classTextView.setText(venue.Class);

            if (venue.IndoorImage != null && venue.IndoorImage.Bitmap != null) {
                this.interiorImageThumbnail.setImageBitmap(venue.IndoorImage.Bitmap);
                IsUsingDefaultThumbnail = false;
            }
            else {
                Bitmap defaultImageBitmap = BitmapFactory.decodeResource(this.baseContext.getResources(), R.drawable.venue_default_thumbnail);
                this.interiorImageThumbnail.setImageBitmap(defaultImageBitmap);
                IsUsingDefaultThumbnail = true;
            }

//            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(venue.VenueBusinessHours);
            WorkingStatus status = BusinessHoursHelper.getWorkingStatus(venue.WorkingStatus);
            this.workingStatusLabel.setVisibility(VISIBLE);
            switch (status){
                case Open:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusOpenedLabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorTertiary);
                    break;
                case Open247:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatus247LabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorTertiaryLight);
                    break;
                case Closed:
                    this.workingStatusLabel.setText(getResources().getString(R.string.item_dish_workingStatusClosedLabelTextView));
                    this.workingStatusLabel.setBackgroundResource(R.color.colorDarkRed);
                    break;
                case Unknown:
                    this.workingStatusLabel.setVisibility(GONE);
                    break;
            }

            if (venue.DistanceToCurrentDeviceLocation > 0)
            {
                String distance = LocationHelper.buildDistanceText(venue.DistanceToCurrentDeviceLocation);
                this.distanceFromDeviceTextView.setText(distance);
                this.walkingMinutesTextView.setVisibility(View.VISIBLE);

                String timeWalking = LocationHelper.buildMinutesWalkingText(venue.DistanceToCurrentDeviceLocation);
                timeWalking = (timeWalking.length() > 0 ? timeWalking : StringHelper.empty());

                if (!timeWalking.equals(StringHelper.empty())) {
                    this.walkingMinutesTextView.setText(timeWalking);
                    this.walkingMinutesTextView.setVisibility(View.VISIBLE);
                } else {
                    this.walkingMinutesTextView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    @Click(R.id.venueItem_ivInteriorThumbnail)
    public void InteriorThumbnailClick()
    {
        if (!IsUsingDefaultThumbnail &&
                this.Venue != null &&
                this.Venue.IndoorImage != null &&
                StringHelper.isNotNullOrEmpty(this.Venue.IndoorImage.Id)){

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

            new AzureBlobService().downloadVenueThumbnail(this.baseContext, this.Venue.IndoorImage.Id, ImageResolution.FormatOriginal, listener);
        }
    }

    @Click(R.id.venueItem_btnMenu)
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

    @Click(R.id.venueItem_btnInfo)
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
