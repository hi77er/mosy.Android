package com.mosy.kalin.mosy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.BooleanResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_venue_details)
public class VenueDetailsActivity
        extends AppCompatActivity
        implements OnMapReadyCallback {

    Boolean IsUsingDefaultIndoorImageThumbnail;

    @Extra
    public Venue Venue;

    @Bean
    public VenuesService VenuesService;

    @FragmentById(R.id.venueDetails_frMap)
    SupportMapFragment VenueLocationMap;

    @ViewById(resName = "venueDetails_lBadges")
    LinearLayout LayoutBadges;
    @ViewById(resName = "venueDetails_lContacts")
    LinearLayout LayoutContacts;
    @ViewById(resName = "venueDetails_lBusinessHours")
    LinearLayout LayoutBusinessHours;
    @ViewById(resName = "venueDetails_lFacebook")
    LinearLayout LayoutFacebook;
    @ViewById(resName = "venueDetails_lAddress")
    LinearLayout LayoutAddress;
    @ViewById(resName = "venueDetails_lTelephone")
    LinearLayout LayoutTelephone;
    @ViewById(resName = "venueDetails_lEmail")
    LinearLayout LayoutEmail;

    @ViewById(resName = "venueDetails_svMain")
    ScrollView ScrollViewMain;
    @ViewById(resName = "venueDetails_ivMapTransparent")
    ImageView TransparentImage;
    @ViewById(resName = "venueDetails_tvName")
    TextView Name;
    @ViewById(resName = "venueDetails_tvClass")
    TextView Class;
    @ViewById(resName = "venueDetails_ivIndoor")
    ImageView IndoorImageThumbnail;
    @ViewById(resName = "venueDetails_tvBHMondayTime")
    TextView Monday;
    @ViewById(resName = "venueDetails_tvBHTuesdayTime")
    TextView Tuesday;
    @ViewById(resName = "venueDetails_tvBHWednesdayTime")
    TextView Wednesday;
    @ViewById(resName = "venueDetails_tvBHThursdayTime")
    TextView Thursday;
    @ViewById(resName = "venueDetails_tvBHFridayTime")
    TextView Friday;
    @ViewById(resName = "venueDetails_tvBHSaturdayTime")
    TextView Saturnday;
    @ViewById(resName = "venueDetails_tvBHSundayTime")
    TextView Sunday;

    @AfterViews
    void updateVenueWithData() {
        Context context = getApplicationContext();

        Name.setText(this.Venue.Name);
        Class.setText(this.Venue.Class);

        try {
            VenueImage image = this.VenuesService.downloadVenueIndoorImageThumbnails(this.Venue, this);
            populateIndoorImage(image);

            populateContacts();

            this.Venue.BusinessHours = this.VenuesService.downloadVenuesBusinessHours(this.Venue.Id, this);
            populateBusinessHours(this.Venue.BusinessHours);

            this.Venue.Location = this.VenuesService.downloadVenueLocation(this.Venue.Id, this);
            this.VenueLocationMap.getMapAsync(this);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.Venue != null & this.Venue.Location != null) {
            LatLng venueLocation = new LatLng(this.Venue.Location.Latitude, this.Venue.Location.Longitude);
            googleMap.addMarker(
                new MarkerOptions()
                    .position(venueLocation)
                    .title(this.Venue.Name)
            );
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 16.0f));
        }
    }

    @Touch(R.id.venueDetails_ivMapTransparent)
    boolean transparentImage_Touch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // Disallow ScrollView to intercept touch events.
                this.ScrollViewMain.requestDisallowInterceptTouchEvent(true);
                // Disable touch on transparent view
                return false;
            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                this.ScrollViewMain.requestDisallowInterceptTouchEvent(false);
                return true;
            case MotionEvent.ACTION_MOVE:
                this.ScrollViewMain.requestDisallowInterceptTouchEvent(true);
                return false;
            default:
                return true;
        }
    }

    private void populateIndoorImage(VenueImage image) {
        if (image != null && image.Bytes != null) {
            byte[] byteArray = Base64.decode(image.Bytes, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            this.IndoorImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
            this.IsUsingDefaultIndoorImageThumbnail = false;
        }
        else
            IsUsingDefaultIndoorImageThumbnail = true;
    }

    private void populateContacts() {

    }

    private void populateBusinessHours(VenueBusinessHours businessHours) {
        if (businessHours != null // and a single day has business hours set ->
            && (businessHours.IsMondayDayOff || businessHours.IsTuesdayDayOff || businessHours.IsWednesdayDayOff || businessHours.IsThursdayDayOff || businessHours.IsFridayDayOff || businessHours.IsSaturdayDayOff || businessHours.IsSundayDayOff || businessHours.MondayFrom != null || businessHours.MondayTo != null || businessHours.TuesdayFrom != null || businessHours.TuesdayTo != null || businessHours.WednesdayFrom != null || businessHours.WednesdayTo != null || businessHours.ThursdayFrom != null || businessHours.ThursdayTo != null || businessHours.FridayFrom != null || businessHours.FridayTo != null || businessHours.SaturdayFrom != null || businessHours.SaturdayTo != null || businessHours.SundayFrom != null || businessHours.SundayTo != null)) {
            String d1 = businessHours.IsMondayDayOff ? "Day Off" :
                    ((businessHours.MondayFrom != null ? businessHours.MondayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.MondayTo != null ? businessHours.MondayTo.substring(0, 5) : ""));
            String d2 = businessHours.IsTuesdayDayOff ? "Day Off" :
                    ((businessHours.TuesdayFrom != null ? businessHours.TuesdayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.TuesdayTo != null ? businessHours.ThursdayTo.substring(0, 5) : ""));
            String d3 = businessHours.IsWednesdayDayOff ? "Day Off" :
                    ((businessHours.WednesdayFrom != null ? businessHours.WednesdayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.WednesdayTo != null ? businessHours.WednesdayTo.substring(0, 5) : ""));
            String d4 = businessHours.IsThursdayDayOff ? "Day Off" :
                    ((businessHours.ThursdayFrom != null ? businessHours.TuesdayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.ThursdayTo != null ? businessHours.ThursdayTo.substring(0, 5) : ""));
            String d5 = businessHours.IsFridayDayOff ? "Day Off" :
                    ((businessHours.FridayFrom != null ? businessHours.FridayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.FridayTo != null ? businessHours.FridayTo.substring(0, 5) : ""));
            String d6 = businessHours.IsSaturdayDayOff ? "Day Off" :
                    ((businessHours.SaturdayFrom != null ? businessHours.SaturdayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.SaturdayTo != null ? businessHours.SaturdayTo.substring(0, 5) : ""));
            String d7 = businessHours.IsSundayDayOff ? "Day Off" :
                    ((businessHours.SundayFrom != null ? businessHours.SundayFrom.substring(0, 5) : "") + " - " +
                     (businessHours.SundayTo != null ? businessHours.SundayTo.substring(0, 5) : ""));

            this.Monday.setText(d1);
            this.Tuesday.setText(d2);
            this.Wednesday.setText(d3);
            this.Thursday.setText(d4);
            this.Friday.setText(d5);
            this.Saturnday.setText(d6);
            this.Sunday.setText(d7);
            this.LayoutBusinessHours.setVisibility(View.VISIBLE);
        }
    }

    @Click(resName = "venueDetails_ivIndoor")
    public void ImageClick()
    {
        if (!IsUsingDefaultIndoorImageThumbnail) {
            final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            VenueImage image = this.VenuesService.downloadVenueIndoorImage(this.Venue.Id, this);
            byte[] byteArray = Base64.decode(image.Bytes, Base64.DEFAULT);
            Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            ImageView ivPreview = (ImageView) nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
            ivPreview.setImageBitmap(bmp);
            nagDialog.show();
        }
    }

}
