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

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.BusinessHoursHelper;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.activity_item_venue)
public class VenueItemView
        extends RelativeLayout {

    String VenueId;
    Boolean IsUsingDefaultOutdoorImageThumbnail;

    @Bean
    public com.mosy.kalin.mosy.Services.VenuesService VenuesService;

    @ViewById(resName = "venueItem_tvName")
    TextView Name;
    @ViewById(resName = "venueItem_tvClass")
    TextView Class;
    @ViewById(resName = "venueItem_tvOpenedSinceUntil")
    TextView OpenedSinceUntil;
    @ViewById(resName = "venueItem_tvDistance")
    TextView DistanceFromDevice;
    @ViewById(resName = "venueItem_ivOutdoorThumbnail")
    ImageView OutdoorImageThumbnail;

    public VenueItemView(Context context) {
        super(context);
    }

    public void bind(Venue venue) {
        this.VenueId = venue.Id;
        this.Name.setText(venue.Name);
        this.Class.setText(venue.Class);

        if (venue.VenueBusinessHours != null) {
            String sinceUntil = BusinessHoursHelper.buildBusinessHoursText(venue.VenueBusinessHours);
            this.OpenedSinceUntil.setText(sinceUntil);
            if (sinceUntil.equals(StringHelper.empty()))
                this.OpenedSinceUntil.setVisibility(GONE);
            else
                this.OpenedSinceUntil.setVisibility(VISIBLE);
        }

        if (venue.Location != null) {
            String distance = LocationHelper.buildDistanceText(venue.Location.DistanceToCurrentLocationMeters);
            String timeWalking = LocationHelper.buildMinutesWalkingText(venue.Location.DistanceToCurrentLocationMeters);
            String text = distance + (timeWalking.length() > 0 ? " | " + timeWalking : StringHelper.empty());
            this.DistanceFromDevice.setText(text);
        }

        if (venue.OutdoorImage != null){
//            byte[] byteArray = Base64.decode(venue.OutdoorImage.Bytes, Base64.DEFAULT);
//            byte[] byteArray = Base64.decode(venue.OutdoorImage.Bytes, Base64.DEFAULT);
            AzureBlobService service = new AzureBlobService();
            byte[] byteArray = service.GetBlob(venue.OutdoorImage.Id, "userimages\\fboalbums\\200x200");
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            this.OutdoorImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            IsUsingDefaultOutdoorImageThumbnail = false;
        }
        else {
            IsUsingDefaultOutdoorImageThumbnail = true;
            this.OutdoorImageThumbnail.setImageResource(R.drawable.venue_default_thumbnail);
        }
    }

    @Click(resName = "venueItem_ivOutdoorThumbnail")
    public void ItemClick()
    {
        if (! IsUsingDefaultOutdoorImageThumbnail){
            final Dialog nagDialog = new Dialog(this.getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            VenueImage image = this.VenuesService.downloadVenueOutdoorImage(this.VenueId);
            if (image.Bytes != null) {
                byte[] byteArray = Base64.decode(image.Bytes, Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

                ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                ivPreview.setImageBitmap(bmp);
                nagDialog.show();
            }
        }
    }

}
