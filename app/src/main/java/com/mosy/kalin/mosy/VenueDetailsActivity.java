package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.DateHelper;
import com.mosy.kalin.mosy.Helpers.DimensionsHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venue_details)
public class VenueDetailsActivity
        extends BaseActivity
        implements OnMapReadyCallback {

    private static final int REQUEST_PHONE_CALL = 1;
    private static final String x200BlobStorageContainerPath = "userimages\\fboalbums\\200x200";
    private static final String originalBlobStorageContainerPath = "userimages\\fboalbums\\original";

    boolean IsUsingDefaultIndoorImageThumbnail;
    public String PhoneNumber;

    @Bean
    VenuesService venueService;

    @Extra
    public Venue Venue;

    @FragmentById(R.id.venueDetails_frMap)
    SupportMapFragment VenueLocationMap;

    @ViewById(resName = "venueDetails_lBadges")
    LinearLayout badgesLayout;
    @ViewById(resName = "venueDetails_lContacts")
    LinearLayout contactsLayout;
    @ViewById(resName = "venueDetails_lBusinessHours")
    LinearLayout businessHoursLayout;

    @ViewById(resName = "venueDetails_lBadgesContainer")
    LinearLayout badgesContainerLayout;
    @ViewById(resName = "venueDetails_lContactsContainer")
    LinearLayout contactsContainerLayout;
    @ViewById(resName = "venueDetails_lBusinessHoursContainer")
    LinearLayout getBusinessHoursContainerLayout;

    @ViewById(resName = "venueDetails_lBadgesProgress")
    LinearLayout badgesProgressLayout;
    @ViewById(resName = "venueDetails_lContactsProgress")
    LinearLayout contactsProgressLayout;
    @ViewById(resName = "venueDetails_lBusinessHoursProgress")
    LinearLayout getBusinessHoursProgressLayout;

    @ViewById(resName = "venueDetails_svMain")
    ScrollView mainScrollView;
    @ViewById(resName = "venueDetails_tvName")
    TextView nameTextView;
    @ViewById(resName = "venueDetails_tvClass")
    TextView classTextView;
    @ViewById(resName = "venueDetails_btnPhone")
    Button phoneButton;
    @ViewById(resName = "venueDetails_ivIndoorThumbnail")
    ImageView indoorImageThumbnailView;
    @ViewById(resName = "venueDetails_tvBHMondayTime")
    TextView mondayTextView;
    @ViewById(resName = "venueDetails_tvBHTuesdayTime")
    TextView tuesdayTextView;
    @ViewById(resName = "venueDetails_tvBHWednesdayTime")
    TextView wednesdayTextView;
    @ViewById(resName = "venueDetails_tvBHThursdayTime")
    TextView thursdayTextView;
    @ViewById(resName = "venueDetails_tvBHFridayTime")
    TextView fridayTextView;
    @ViewById(resName = "venueDetails_tvBHSaturdayTime")
    TextView saturdayTextView;
    @ViewById(resName = "venueDetails_tvBHSundayTime")
    TextView sundayTextView;

    @ViewById(resName = "venueDetails_ivMapTransparent")
    ImageView TransparentImage;
    @ViewById(resName = "venueDetailsBadge_freeWiFi")
    ImageView FreeWiFiImageView;
    @ViewById(resName = "venueDetailsBadge_workingFriendly")
    ImageView WorkingFriendlyImageView;
    @ViewById(resName = "venueDetailsBadge_bikeFriendly")
    ImageView BikeFriendlyImageView;
    @ViewById(resName = "venueDetailsBadge_childFriendly")
    ImageView ChildFriendlyImageView;
    @ViewById(resName = "venueDetailsBadge_funPlace")
    ImageView FunPlaceImageView;
    @ViewById(resName = "venueDetailsBadge_parkingSign")
    ImageView ParkingSignImageView;
    @ViewById(resName = "venueDetailsBadge_petFriendly")
    ImageView PetFriendlyImageView;
    @ViewById(resName = "venueDetailsBadge_romanticPlace")
    ImageView RomanticPlaceImageView;
    @ViewById(resName = "venueDetailsBadge_wheelchairFriendly")
    ImageView WheelchairFriendlyImageView;
    @ViewById(resName = "venueDetailsBadge_noSmoking")
    ImageView NoSmokingImageView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        try {
            nameTextView.setText(this.Venue.Name);
            classTextView.setText(this.Venue.Class);

            loadIndoorImage();
            loadContacts();
            loadBusinessHours();
            loadLocation();
            populateFilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIndoorImage() {
        AsyncTaskListener<VenueImage> apiCallResultListener = new AsyncTaskListener<VenueImage>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(VenueImage result) {
                Venue.IndoorImage = result;
                populateIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getImageMetaIndoor(this.applicationContext, apiCallResultListener, null, this.Venue.Id);
    }

    private void loadContacts() {
        AsyncTaskListener<VenueContacts> listener = new AsyncTaskListener<VenueContacts>() {
            @Override public void onPreExecute() {
                showContactsLoading();
            }
            @Override public void onPostExecute(VenueContacts result) {
                Venue.VenueContacts = result;
                populateContacts();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getContacts(this.applicationContext, listener, null, this.Venue.Id);
    }

    private void loadBusinessHours() {
        AsyncTaskListener<VenueBusinessHours> apiCallResultListener = new AsyncTaskListener<VenueBusinessHours>() {
            @Override public void onPreExecute() {
                showBusinessHoursLoading();
            }

            @Override public void onPostExecute(VenueBusinessHours result) {
                Venue.VenueBusinessHours = result;
                populateBusinessHours();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getBusinessHours(applicationContext, apiCallResultListener, null, this.Venue.Id);
    }

    private void loadLocation() {
        AsyncTaskListener<VenueLocation> listener = new AsyncTaskListener<VenueLocation>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override public void onPostExecute(VenueLocation result) {
                Venue.Location = result;
                populateGoogleMap();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getLocation(applicationContext, listener, null, this.Venue.Id);

        // TODO: Decide weather this should be called here on onPostExecute of the upper task
        populateGoogleMap();
    }
//
//    private void showBadgesLoading() {
//        this.badgesLayout.setVisibility(View.GONE);
//        this.badgesProgressLayout.setVisibility(View.VISIBLE);
//        this.badgesContainerLayout.setVisibility(View.VISIBLE);
//    }
//
    private void showContactsLoading() {
        this.contactsLayout.setVisibility(View.GONE);
        this.contactsProgressLayout.setVisibility(View.VISIBLE);
    }

    private void showBusinessHoursLoading() {
        this.businessHoursLayout.setVisibility(View.GONE);
        this.getBusinessHoursProgressLayout.setVisibility(View.VISIBLE);
    }

    private void populateIndoorImageThumbnail() {
        if (this.Venue.IndoorImage != null && this.Venue.IndoorImage.Id != null && this.Venue.IndoorImage.Id.length() > 0) {
            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        indoorImageThumbnailView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                        IsUsingDefaultIndoorImageThumbnail = false;
                    } else
                        IsUsingDefaultIndoorImageThumbnail = true;
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.Venue.IndoorImage.Id, x200BlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

    private void populateContacts() {
        VenueContacts venueContacts = this.Venue.VenueContacts;
        if (venueContacts != null) {
            boolean any = false;
            if (StringHelper.isNotNullOrEmpty(venueContacts.Phone)) {
                this.PhoneNumber = venueContacts.Phone;
                this.phoneButton.setText(venueContacts.Phone);
                this.phoneButton.setVisibility(View.VISIBLE);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.Address)) {
                addContact(venueContacts.Address, R.drawable.contact_address_paprica, false);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.WebPage)) {
                addContact(venueContacts.WebPage, R.drawable.contact_webpage_paprica, true);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.FacebookUrl)) {
                addContact(venueContacts.FacebookUrl, R.drawable.contact_facebook_paprica, true);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.Foursquare)) {
                addContact(venueContacts.Foursquare, R.drawable.contact_foursquare_paprica, true);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.Instagram)) {
                addContact(venueContacts.Instagram, R.drawable.contact_instagram_paprica, true);
                any = true;
            }
            if (StringHelper.isNotNullOrEmpty(venueContacts.Email)) {
                addContact(venueContacts.Email, R.drawable.contact_email_paprica, true);
                any = true;
            }
            if (any) {
                showContactsContainer();
            } else {
                hideContactsContainer();
            }

        } else {
            hideContactsContainer();
        }
    }

    private void addContact(String value, @DrawableRes int iconId, boolean isLink) {
        LinearLayout contactLayout = new LinearLayout(this);
        contactLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, DimensionsHelper.dpToPx(5, this));
        contactLayout.setLayoutParams(layoutParams);

        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(DimensionsHelper.dpToPx(25, this), DimensionsHelper.dpToPx(25, this));
        ivLayoutParams.setMargins(0, 0, DimensionsHelper.dpToPx(3, this), 0);
        iv.setLayoutParams(ivLayoutParams);
        iv.setImageResource(iconId);

        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(15);
        tv.setText(value);
        if (isLink) {
            tv.setClickable(true);
            tv.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "<a href='" + value + "'> " + value + "</a>";
            tv.setText(Html.fromHtml(text));
        }

        contactLayout.addView(iv);
        contactLayout.addView(tv);
        this.contactsLayout.addView(contactLayout);
    }

    private void showContactsContainer() {
        this.contactsProgressLayout.setVisibility(View.GONE);
        this.contactsLayout.setVisibility(View.VISIBLE);
        this.contactsContainerLayout.setVisibility(View.VISIBLE);
    }

    private void hideContactsContainer() {
        this.contactsProgressLayout.setVisibility(View.GONE);
        this.contactsLayout.setVisibility(View.GONE);
        this.contactsContainerLayout.setVisibility(View.GONE);
    }

    private void populateFilters() {
        ArrayList<Filter> filters = this.Venue.Filters;

        if (filters != null && filters.size() > 0) {
            boolean any = false;
            //INFO: Weak code! Strong dependency to badge ids here.
            for (Filter filter : filters) {
                if (filter.Id.toUpperCase().equals("E48D0871-500E-4D41-8620-840308901970")) {
                    this.FreeWiFiImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("93252A33-D2A7-44A6-B375-0496EB3B5F9E")) {
                    this.WorkingFriendlyImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("602F1863-A209-4E34-BC5E-871AE52AE684")) {
                    this.BikeFriendlyImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("4F952337-5F15-4EFA-934A-7A948800B93F")) {
                    this.ChildFriendlyImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("BE06BC04-CB07-4884-866B-907132DE2944")) {
                    this.FunPlaceImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("245733F1-35C4-4497-B188-59B1A69480AA")) {
                    this.ParkingSignImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("5CF4A8FA-CA93-4D36-BD67-E4E6A26D751E")) {
                    this.PetFriendlyImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("D7C4C6BB-A717-4774-B3B3-E4C23893D2BF")) {
                    this.RomanticPlaceImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("C8C0E9CA-4F73-4A01-93DB-00573BD2E7F0")) {
                    this.WheelchairFriendlyImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (filter.Id.toUpperCase().equals("39814B44-BC9A-4172-B91A-D190213DB112")) {
                    this.NoSmokingImageView.setVisibility(View.VISIBLE);
                    any = true;
                }
            }
            if (any) {
                showBadgesContainer();
            }
            else {
                hideBadgesContainer();
            }
        }
        else
        {
            hideBadgesContainer();
        }
    }

    private void showBadgesContainer() {
        this.contactsProgressLayout.setVisibility(View.GONE);
        this.badgesLayout.setVisibility(View.VISIBLE);
        this.badgesContainerLayout.setVisibility(View.VISIBLE);
    }

    private void hideBadgesContainer() {
        this.contactsProgressLayout.setVisibility(View.GONE);
        this.badgesLayout.setVisibility(View.GONE);
        this.badgesContainerLayout.setVisibility(View.GONE);
    }

    private void populateBusinessHours() {
        VenueBusinessHours businessHours = this.Venue.VenueBusinessHours;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (businessHours != null // and a single day has business hours set ->
                && (businessHours.IsMondayDayOff || businessHours.IsTuesdayDayOff || businessHours.IsWednesdayDayOff ||
                businessHours.IsThursdayDayOff || businessHours.IsFridayDayOff || businessHours.IsSaturdayDayOff ||
                businessHours.IsSundayDayOff || businessHours.MondayFrom != null || businessHours.MondayTo != null
                || businessHours.TuesdayFrom != null || businessHours.TuesdayTo != null || businessHours.WednesdayFrom != null
                || businessHours.WednesdayTo != null || businessHours.ThursdayFrom != null || businessHours.ThursdayTo != null
                || businessHours.FridayFrom != null || businessHours.FridayTo != null || businessHours.SaturdayFrom != null
                || businessHours.SaturdayTo != null || businessHours.SundayFrom != null || businessHours.SundayTo != null)) {

            String d1 = businessHours.IsMondayDayOff ? "Day Off" :
                    ((businessHours.MondayFrom != null ? DateHelper.ToString(businessHours.MondayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.MondayTo != null ? DateHelper.ToString(businessHours.MondayTo, formatter) : StringHelper.empty()));
            String d2 = businessHours.IsTuesdayDayOff ? "Day Off" :
                    ((businessHours.TuesdayFrom != null ? DateHelper.ToString(businessHours.TuesdayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.TuesdayTo != null ? DateHelper.ToString(businessHours.TuesdayTo, formatter) : StringHelper.empty()));
            String d3 = businessHours.IsWednesdayDayOff ? "Day Off" :
                    ((businessHours.WednesdayFrom != null ? DateHelper.ToString(businessHours.WednesdayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.WednesdayTo != null ? DateHelper.ToString(businessHours.WednesdayTo, formatter) : StringHelper.empty()));
            String d4 = businessHours.IsThursdayDayOff ? "Day Off" :
                    ((businessHours.ThursdayFrom != null ? DateHelper.ToString(businessHours.ThursdayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.ThursdayTo != null ? DateHelper.ToString(businessHours.ThursdayTo, formatter) : StringHelper.empty()));
            String d5 = businessHours.IsFridayDayOff ? "Day Off" :
                    ((businessHours.FridayFrom != null ? DateHelper.ToString(businessHours.FridayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.FridayTo != null ? DateHelper.ToString(businessHours.FridayTo, formatter) : StringHelper.empty()));
            String d6 = businessHours.IsSaturdayDayOff ? "Day Off" :
                    ((businessHours.SaturdayFrom != null ? DateHelper.ToString(businessHours.SaturdayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.SaturdayTo != null ? DateHelper.ToString(businessHours.SaturdayTo, formatter) : StringHelper.empty()));
            String d7 = businessHours.IsSundayDayOff ? "Day Off" :
                    ((businessHours.SundayFrom != null ? DateHelper.ToString(businessHours.SundayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.SundayTo != null ? DateHelper.ToString(businessHours.SundayTo, formatter) : StringHelper.empty()));

            this.mondayTextView.setText(d1);
            this.tuesdayTextView.setText(d2);
            this.wednesdayTextView.setText(d3);
            this.thursdayTextView.setText(d4);
            this.fridayTextView.setText(d5);
            this.saturdayTextView.setText(d6);
            this.sundayTextView.setText(d7);

            showBHContainer();
        }
        else {
            hideBHContainer();
        }
    }

    private void showBHContainer() {
        this.getBusinessHoursProgressLayout.setVisibility(View.GONE);
        this.businessHoursLayout.setVisibility(View.VISIBLE);
        this.getBusinessHoursContainerLayout.setVisibility(View.VISIBLE);
    }

    private void hideBHContainer() {
        this.getBusinessHoursProgressLayout.setVisibility(View.GONE);
        this.businessHoursLayout.setVisibility(View.GONE);
        this.getBusinessHoursContainerLayout.setVisibility(View.GONE);
    }

    private void populateGoogleMap() {
        this.VenueLocationMap.getMapAsync(this);
    }

    private boolean hasValidIndoorImageMetadata() {
        return this.Venue.IndoorImage != null
                && this.Venue.IndoorImage.Id != null
                && this.Venue.IndoorImage.Id.length() > 0;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.Venue != null && this.Venue.Location != null) {
            LatLng venueLocation = new LatLng(this.Venue.Location.Latitude, this.Venue.Location.Longitude);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
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
                this.mainScrollView.requestDisallowInterceptTouchEvent(true);
                // Disable touch on transparent view
                return false;
            case MotionEvent.ACTION_UP:
                // Allow ScrollView to intercept touch events.
                this.mainScrollView.requestDisallowInterceptTouchEvent(false);
                return true;
            case MotionEvent.ACTION_MOVE:
                this.mainScrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            default:
                return true;
        }
    }

    @Click(resName = "venueDetails_ivIndoorThumbnail")
    public void ImageClick() {
        if (!IsUsingDefaultIndoorImageThumbnail && hasValidIndoorImageMetadata()) {
            final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        ImageView ivPreview = nagDialog.findViewById(R.id.imagePreviewDialog_ivPreview);
                        ivPreview.setImageBitmap(bmp);
                        nagDialog.show();
                    } else
                        throw new NullPointerException("Image not found");
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.Venue.IndoorImage.Id, originalBlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

    @Click(resName = "venueDetails_btnPhone")
    public void phoneCall() {
        if (StringHelper.isNotNullOrEmpty(this.PhoneNumber))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", this.PhoneNumber, null));
            startActivity(intent);
        }
    }

}
