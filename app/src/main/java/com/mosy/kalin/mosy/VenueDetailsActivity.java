package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.DateHelper;
import com.mosy.kalin.mosy.Helpers.DrawableHelper;
import com.mosy.kalin.mosy.Helpers.MetricsHelper;
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

    boolean descriptionExpanded = false;
    boolean isUsingDefaultIndoorImageThumbnail;
    public String phoneNumber;


    @Bean
    VenuesService venueService;

    @Extra
    public Venue Venue;

    @FragmentById(R.id.venueDetails_frMap)
    SupportMapFragment VenueLocationMap;

    @ViewById(R.id.venueDetails_lFilters)
    LinearLayout filtersLayout;
    @ViewById(R.id.venueDetails_lCultureFilters)
    LinearLayout cultureFiltersLayout;
    @ViewById(R.id.venueDetails_lContacts)
    LinearLayout contactsLayout;
    @ViewById(R.id.venueDetails_lBusinessHours)
    LinearLayout businessHoursLayout;

    @ViewById(R.id.venueDetails_lDescriptionContainer)
    LinearLayout descriptionContainerLayout;
    @ViewById(R.id.venueDetails_lFiltersContainer)
    LinearLayout filtersContainerLayout;
    @ViewById(R.id.venueDetails_lCultureFiltersContainer)
    LinearLayout cultureFiltersContainerLayout;
    @ViewById(R.id.venueDetails_lContactsContainer)
    LinearLayout contactsContainerLayout;
    @ViewById(R.id.venueDetails_lBusinessHoursContainer)
    LinearLayout getBusinessHoursContainerLayout;

    @ViewById(R.id.venueDetails_lFiltersProgress)
    LinearLayout filtersProgressLayout;
    @ViewById(R.id.venueDetails_lCultureFiltersProgress)
    LinearLayout cultureFiltersProgressLayout;
    @ViewById(R.id.venueDetails_lContactsProgress)
    LinearLayout contactsProgressLayout;
    @ViewById(R.id.venueDetails_lBusinessHoursProgress)
    LinearLayout getBusinessHoursProgressLayout;

    @ViewById(R.id.venueDetails_svMain)
    ScrollView mainScrollView;
    @ViewById(R.id.venueDetails_tvName)
    TextView nameTextView;
    @ViewById(R.id.venueDetails_tvClass)
    TextView classTextView;
    @ViewById(R.id.venueDetails_tvDescription)
    TextView descripionTextView;

    @ViewById(R.id.venueDetails_btnPhone)
    Button phoneButton;
    @ViewById(R.id.venueDetails_btnDirections)
    Button directionsButton;

    @ViewById(R.id.venueDetails_ivIndoorThumbnail)
    ImageView indoorImageThumbnailView;
    @ViewById(R.id.venueDetails_tvBHMondayTime)
    TextView mondayTextView;
    @ViewById(R.id.venueDetails_tvBHTuesdayTime)
    TextView tuesdayTextView;
    @ViewById(R.id.venueDetails_tvBHWednesdayTime)
    TextView wednesdayTextView;
    @ViewById(R.id.venueDetails_tvBHThursdayTime)
    TextView thursdayTextView;
    @ViewById(R.id.venueDetails_tvBHFridayTime)
    TextView fridayTextView;
    @ViewById(R.id.venueDetails_tvBHSaturdayTime)
    TextView saturdayTextView;
    @ViewById(R.id.venueDetails_tvBHSundayTime)
    TextView sundayTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        this.applicationContext = getApplicationContext();
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        try {
            nameTextView.setText(this.Venue.Name);
            classTextView.setText(this.Venue.Class);
            if (StringHelper.isNotNullOrEmpty(this.Venue.Description)) {
                descriptionContainerLayout.setVisibility(View.VISIBLE);
                descripionTextView.setText(this.Venue.Description.substring(0, 40) + " ...");
            }

            loadIndoorImage();
            loadContacts();
            loadBusinessHours();
            loadLocation();
            populateFilters();
            populateCultureFilters();
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
                        isUsingDefaultIndoorImageThumbnail = false;
                    } else
                        isUsingDefaultIndoorImageThumbnail = true;
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
                this.phoneNumber = venueContacts.Phone;
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
        layoutParams.setMargins(0, 0, 0, (int)MetricsHelper.convertDpToPixel(5));
        contactLayout.setLayoutParams(layoutParams);

        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams((int)MetricsHelper.convertDpToPixel(25), (int)MetricsHelper.convertDpToPixel(25));
        ivLayoutParams.setMargins(0, 0, (int)MetricsHelper.convertDpToPixel(3), 0);
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
            ArrayList<Filter> accessibilityFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.VenueAccessibility).toList());
            ArrayList<Filter> availabilityFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.VenueAvailability).toList());
            ArrayList<Filter> atmosphereFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.VenueAtmosphere).toList());

            boolean anyAccessibilityFilter = this.iterateFiltersHasAny(accessibilityFilters, true, this.filtersLayout);
            boolean anyAvailabilityFilter = this.iterateFiltersHasAny(availabilityFilters, true, this.filtersLayout);
            boolean anyAtmosphereFilter = this.iterateFiltersHasAny(atmosphereFilters, true, this.filtersLayout);

            if (anyAccessibilityFilter || anyAvailabilityFilter || anyAtmosphereFilter)
                this.showFiltersContainer();
            else
                this.hideFiltersContainer();
        }
        else
            this.hideFiltersContainer();
    }

    private void populateCultureFilters() {
        ArrayList<Filter> filters = this.Venue.Filters;
        if (filters != null && filters.size() > 0) {
            ArrayList<Filter> cultureFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.VenueCulture).toList());

            boolean anyCultureFilter = this.iterateFiltersHasAny(cultureFilters, false, this.cultureFiltersLayout);

            if (anyCultureFilter)
                this.showCultureFiltersContainer();
            else
                this.hideCultureFiltersContainer();
        }
        else
            this.hideCultureFiltersContainer();
    }

    private boolean iterateFiltersHasAny(ArrayList<Filter> filters, boolean needSquareIcon, LinearLayout container) {
        boolean hasAny = false;
        for (Filter filter : filters) {
            int drawableId = DrawableHelper.getDrawableIdByFilterId(filter.Id);
            if (drawableId != 0) {
                ImageView filterImageView = this.createFilterImage(drawableId, needSquareIcon ? 31 : 46, filter.I18nResourceDescription, filter.Description);
                container.addView(filterImageView);
                hasAny = true;
            }
        }
        return hasAny;
    }

    private ImageView createFilterImage(int drawableId, int widthDp, String descriptionResourceI18nId, String defaultDescriptionText) {
        ImageView filterImageView = new ImageView(this.baseContext);
        filterImageView.setImageDrawable(getResources().getDrawable(drawableId));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width = (int)MetricsHelper.convertDpToPixel(widthDp);
        lp.height = (int)MetricsHelper.convertDpToPixel(31);
        lp.setMargins((int)MetricsHelper.convertDpToPixel(6), 0, 0, 0);
        filterImageView.setLayoutParams(lp);

        filterImageView.setOnClickListener(view -> this.filterClick(descriptionResourceI18nId, defaultDescriptionText));

        filterImageView.setVisibility(View.VISIBLE);
        return filterImageView;
    }

    private void showFiltersContainer() {
        this.filtersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.VISIBLE);
        this.filtersContainerLayout.setVisibility(View.VISIBLE);
    }

    private void hideFiltersContainer() {
        this.filtersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.GONE);
        this.filtersContainerLayout.setVisibility(View.GONE);
    }


    public  void showCultureFiltersContainer() {
        this.cultureFiltersProgressLayout.setVisibility(View.GONE);
        this.cultureFiltersLayout.setVisibility(View.VISIBLE);
        this.cultureFiltersContainerLayout.setVisibility(View.VISIBLE);
    }

    public  void hideCultureFiltersContainer() {
        this.cultureFiltersProgressLayout.setVisibility(View.GONE);
        this.cultureFiltersLayout.setVisibility(View.GONE);
        this.cultureFiltersContainerLayout.setVisibility(View.GONE);
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
        if (this.Venue != null && this.Venue.Location != null) {
            this.directionsButton.setOnClickListener(view -> directionsButtonClicked());
            this.directionsButton.setVisibility(View.VISIBLE);
        }
        this.VenueLocationMap.getMapAsync(this);
    }

    private boolean hasValidIndoorImageMetadata() {
        return this.Venue.IndoorImage != null
                && this.Venue.IndoorImage.Id != null
                && this.Venue.IndoorImage.Id.length() > 0;
    }

    private void directionsButtonClicked() {
        String parameters = "destination=" + this.Venue.Location.Latitude + "," + this.Venue.Location.Longitude;
        Uri uri = Uri.parse("https://www.google.com/maps/dir/?api=1&" + parameters);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(uri);
        startActivity(intent);
    }

    private void filterClick(String descriptionResourceI18nId, String defaultDescriptionText) {
        String filterDescriptionLocalized = StringHelper.getStringAppDefaultLocale(this, getResources(), descriptionResourceI18nId, defaultDescriptionText);
        if (StringHelper.isNotNullOrEmpty(filterDescriptionLocalized))
            new AlertDialog.Builder(this)
                    .setTitle(StringHelper.getStringAppDefaultLocale(this, getResources(), "info_dialog_title", "Info"))
                    .setMessage(filterDescriptionLocalized)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->  dialog.cancel())
                    .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.Venue != null && this.Venue.Location != null) {
            LatLng venueLocation = new LatLng(this.Venue.Location.Latitude, this.Venue.Location.Longitude);

//            googleMap.getUiSettings().setZoomControlsEnabled(true);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);

            Marker marker = googleMap.addMarker(
                    new MarkerOptions()
                            .position(venueLocation)
                            .title(this.Venue.Name) //  + (" - " + "Click the marker to get directions")
            );
            marker.showInfoWindow();

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 16.0f));
        }
    }

    //INFO: We use that when no Lite Mode is activated
//    @Touch(R.id.venueDetails_ivMapTransparent)
//    boolean transparentImage_Touch(View v, MotionEvent event) {
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                // Disallow ScrollView to intercept touch events.
//                this.mainScrollView.requestDisallowInterceptTouchEvent(true);
//                // Disable touch on transparent view
//                return false;
//            case MotionEvent.ACTION_UP:
//                // Allow ScrollView to intercept touch events.
//                this.mainScrollView.requestDisallowInterceptTouchEvent(false);
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                this.mainScrollView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            default:
//                return true;
//        }
//    }

    @Click(R.id.venueDetails_lDescriptionContainer)
    public void descriptionClicked() {
        descriptionExpanded = !descriptionExpanded;
        if (descriptionExpanded)
            descripionTextView.setText(this.Venue.Description);
        else
            descripionTextView.setText(this.Venue.Description.substring(0, 40) + " ...");
    }

    @Click(R.id.venueDetails_ivIndoorThumbnail)
    public void ImageClick() {
        if (!isUsingDefaultIndoorImageThumbnail && hasValidIndoorImageMetadata()) {
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

    @Click(R.id.venueDetails_btnPhone)
    public void phoneCall() {
        if (StringHelper.isNotNullOrEmpty(this.phoneNumber))
        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", this.phoneNumber, null));
            startActivity(intent);
        }
    }

}
