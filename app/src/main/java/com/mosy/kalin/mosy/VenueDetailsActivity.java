package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
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
import com.mosy.kalin.mosy.DTOs.Contact;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.DateHelper;
import com.mosy.kalin.mosy.Helpers.DimensionsHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueContactsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueContactsAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueEndorsementsAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueIndoorImageMetadataAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Services.VenueService;

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
        extends AppCompatActivity
        implements OnMapReadyCallback {

    private static final String x200BlobStorageContainerPath = "userimages\\fboalbums\\200x200";
    private static final String originalBlobStorageContainerPath = "userimages\\fboalbums\\original";
    boolean IsUsingDefaultIndoorImageThumbnail;

    @Extra
    public Venue Venue;
    @Bean
    public VenueService VenuesService;

    @FragmentById(R.id.venueDetails_frMap)
    SupportMapFragment VenueLocationMap;

    @ViewById(resName = "venueDetails_lBadges")
    LinearLayout LayoutBadges;
    @ViewById(resName = "venueDetails_lContacts")
    LinearLayout LayoutContacts;
    @ViewById(resName = "venueDetails_lBusinessHours")
    LinearLayout LayoutBusinessHours;

    @ViewById(resName = "venueDetails_svMain")
    ScrollView ScrollViewMain;
    @ViewById(resName = "venueDetails_tvName")
    TextView Name;
    @ViewById(resName = "venueDetails_tvClass")
    TextView Class;
    @ViewById(resName = "venueDetails_ivIndoorThumbnail")
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
    TextView Saturday;
    @ViewById(resName = "venueDetails_tvBHSundayTime")
    TextView Sunday;

    @ViewById(resName = "venueDetails_ivMapTransparent")
    ImageView TransparentImage;
    @ViewById(resName = "venueDetailsBadge_freeWiFi")
    ImageView FreeWiFi;
    @ViewById(resName = "venueDetailsBadge_workingFriendly")
    ImageView WorkingFriendly;
    @ViewById(resName = "venueDetailsBadge_bikeFriendly")
    ImageView BikeFriendly;
    @ViewById(resName = "venueDetailsBadge_childFriendly")
    ImageView ChildFriendly;
    @ViewById(resName = "venueDetailsBadge_funPlace")
    ImageView FunPlace;
    @ViewById(resName = "venueDetailsBadge_parkingSign")
    ImageView ParkingSign;
    @ViewById(resName = "venueDetailsBadge_petFriendly")
    ImageView PetFriendly;
    @ViewById(resName = "venueDetailsBadge_romanticPlace")
    ImageView RomanticPlace;
    @ViewById(resName = "venueDetailsBadge_wheelchairFriendly")
    ImageView WheelchairFriendly;
    @ViewById(resName = "venueDetailsBadge_noSmoking")
    ImageView NoSmoking;

    @AfterViews
    void updateVenueWithData() {
        try {
            Name.setText(this.Venue.Name);
            Class.setText(this.Venue.Class);

            loadIndoorImage();
            loadContacts();
            loadBusinessHours();
            loadLocation();
            loadEndorsements();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadIndoorImage(){
        AsyncTaskListener<VenueImage> listener = new AsyncTaskListener<VenueImage>() {
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
        GetVenueIndoorImageMetaBindingModel model = new GetVenueIndoorImageMetaBindingModel(this.Venue.Id);
        new LoadVenueIndoorImageMetadataAsyncTask(listener).execute(model);
    }

    private void loadContacts(){
        AsyncTaskListener<ArrayList<Contact>> listener = new AsyncTaskListener<ArrayList<Contact>>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(ArrayList<Contact> result) {
                Venue.Contacts = result;
                populateContacts();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        GetVenueContactsBindingModel model = new GetVenueContactsBindingModel(this.Venue.Id);
        new LoadVenueContactsAsyncTask(listener).execute(model);

    }

    private void loadBusinessHours(){
        AsyncTaskListener<VenueBusinessHours> listener = new AsyncTaskListener<VenueBusinessHours>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(VenueBusinessHours result) {
                Venue.VenueBusinessHours = result;
                populateBusinessHours();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        GetVenueBusinessHoursBindingModel model = new GetVenueBusinessHoursBindingModel(this.Venue.Id);
        new LoadVenueBusinessHoursAsyncTask(listener).execute(model);
    }

    private void loadLocation(){
        AsyncTaskListener<VenueLocation> listener = new AsyncTaskListener<VenueLocation>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(VenueLocation result) {
                Venue.Location = result;
                populateGoogleMap();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        GetVenueLocationBindingModel model = new GetVenueLocationBindingModel(this.Venue.Id);
        new LoadVenueLocationAsyncTask(listener).execute(model);

        // TODO: Decide weather this should be called here on onPostExecute of the upper task
        populateGoogleMap();
    }

    private void loadEndorsements(){
        AsyncTaskListener<ArrayList<VenueBadgeEndorsement>> listener = new AsyncTaskListener<ArrayList<VenueBadgeEndorsement>>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(ArrayList<VenueBadgeEndorsement> result) {
                Venue.Endorsements =  result;
                populateBadges();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        GetVenueBadgeEndorsementsBindingModel model = new GetVenueBadgeEndorsementsBindingModel(this.Venue.Id);
        new LoadVenueEndorsementsAsyncTask(listener).execute(model);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (this.Venue != null && this.Venue.Location != null) {
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

    private void populateIndoorImageThumbnail() {
        if (this.Venue.IndoorImage != null && this.Venue.IndoorImage.Id != null && this.Venue.IndoorImage.Id.length() > 0) {
            //INFO: TryGet Venue Image
            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override
                public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        IndoorImageThumbnail.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                        IsUsingDefaultIndoorImageThumbnail = false;
                    }
                    else
                        IsUsingDefaultIndoorImageThumbnail = true;
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.Venue.IndoorImage.Id, x200BlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

    private void populateContacts() {
        ArrayList<Contact> contacts = this.Venue.Contacts;
        if (contacts != null && contacts.size() > 0){
            this.LayoutContacts.setVisibility(View.VISIBLE);
            for (Contact contact : contacts) {
                switch (contact.Type){
                    case 1: //email
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 2: //telephone
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 3: //instagram
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 4: //facebook
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 5: //skype
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 6: //twitter
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 7: //forsquare
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 8: //google+
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                    case 9: //address
                        addContact(contact.Value, R.drawable.venue_default_thumbnail);
                        break;
                }
            }
        }
        else{
            this.LayoutContacts.setVisibility(View.GONE);
        }
    }

    private void addContact(String value, @DrawableRes int iconId) {
        LinearLayout contactLayout = new LinearLayout(this);
        contactLayout.setOrientation(LinearLayout.HORIZONTAL);
        contactLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new LinearLayout.LayoutParams(DimensionsHelper.dpToPx(25, this), DimensionsHelper.dpToPx(25, this)));
        iv.setImageResource(iconId);

        TextView tv = new TextView(this);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(15);
        tv.setText(value);

        contactLayout.addView(iv);
        contactLayout.addView(tv);
        this.LayoutContacts.addView(contactLayout);
    }

    private void populateBusinessHours() {
        VenueBusinessHours businessHours = this.Venue.VenueBusinessHours;
        SimpleDateFormat formatter =  new SimpleDateFormat("HH:mm");
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
                    ((businessHours.WednesdayFrom != null ? DateHelper.ToString(businessHours.WednesdayFrom, formatter) : StringHelper.empty())+ " - " +
                            (businessHours.WednesdayTo != null ? DateHelper.ToString(businessHours.WednesdayTo, formatter) : StringHelper.empty()));
            String d4 = businessHours.IsThursdayDayOff ? "Day Off" :
                    ((businessHours.ThursdayFrom != null ? DateHelper.ToString(businessHours.ThursdayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.ThursdayTo != null ? DateHelper.ToString(businessHours.ThursdayTo, formatter) : StringHelper.empty()));
            String d5 = businessHours.IsFridayDayOff ? "Day Off" :
                    ((businessHours.FridayFrom != null ? DateHelper.ToString(businessHours.FridayFrom, formatter) : StringHelper.empty()) + " - " +
                            (businessHours.FridayTo != null ? DateHelper.ToString(businessHours.FridayTo, formatter) : StringHelper.empty()));
            String d6 = businessHours.IsSaturdayDayOff ? "Day Off" :
                    ((businessHours.SaturdayFrom != null ? DateHelper.ToString(businessHours.SaturdayFrom, formatter) : StringHelper.empty())+ " - " +
                            (businessHours.SaturdayTo != null ? DateHelper.ToString(businessHours.SaturdayTo, formatter) : StringHelper.empty()));
            String d7 = businessHours.IsSundayDayOff ? "Day Off" :
                    ((businessHours.SundayFrom != null ? DateHelper.ToString(businessHours.SundayFrom, formatter) : StringHelper.empty())+ " - " +
                            (businessHours.SundayTo != null ? DateHelper.ToString(businessHours.SundayTo, formatter) : StringHelper.empty()));

            this.Monday.setText(d1);
            this.Tuesday.setText(d2);
            this.Wednesday.setText(d3);
            this.Thursday.setText(d4);
            this.Friday.setText(d5);
            this.Saturday.setText(d6);
            this.Sunday.setText(d7);
            this.LayoutBusinessHours.setVisibility(View.VISIBLE);
        }
    }

    public void populateGoogleMap() {
        this.VenueLocationMap.getMapAsync(this);
    }

    private void populateBadges() {
        ArrayList<VenueBadgeEndorsement> badgeEndorsements = this.Venue.Endorsements;

        if (badgeEndorsements != null && badgeEndorsements.size() > 0)
        {
            boolean any = false;
            //INFO: Weak code! Strong dependency to badge ids here.
            for (VenueBadgeEndorsement endorsement : badgeEndorsements) {
                if (endorsement.BadgeId.toUpperCase().equals("E48D0871-500E-4D41-8620-840308901970")){
                    this.FreeWiFi.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("93252A33-D2A7-44A6-B375-0496EB3B5F9E")){
                    this.WorkingFriendly.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("602F1863-A209-4E34-BC5E-871AE52AE684")) {
                    this.BikeFriendly.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("4F952337-5F15-4EFA-934A-7A948800B93F")){
                    this.ChildFriendly.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("BE06BC04-CB07-4884-866B-907132DE2944")) {
                    this.FunPlace.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("245733F1-35C4-4497-B188-59B1A69480AA")){
                    this.ParkingSign.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("5CF4A8FA-CA93-4D36-BD67-E4E6A26D751E")) {
                    this.PetFriendly.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("D7C4C6BB-A717-4774-B3B3-E4C23893D2BF")) {
                    this.RomanticPlace.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("C8C0E9CA-4F73-4A01-93DB-00573BD2E7F0")) {
                    this.WheelchairFriendly.setVisibility(View.VISIBLE);
                    any = true;
                }
                if (endorsement.BadgeId.toUpperCase().equals("39814B44-BC9A-4172-B91A-D190213DB112")) {
                    this.NoSmoking.setVisibility(View.VISIBLE);
                    any = true;
                }
            }
            if (any) this.LayoutBadges.setVisibility(View.VISIBLE);
        }
    }

    @Click(resName = "venueDetails_ivIndoorThumbnail")
    public void ImageClick()
    {
        if (!IsUsingDefaultIndoorImageThumbnail
                && this.Venue.IndoorImage != null
                && this.Venue.IndoorImage.Id != null
                && this.Venue.IndoorImage.Id.length() > 0) {
            final Dialog nagDialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            nagDialog.setCancelable(true);
            nagDialog.setContentView(R.layout.image_preview_dialog);

            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override
                public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
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

}
