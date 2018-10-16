package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuPagerAdapter;
import com.mosy.kalin.mosy.DTOs.HttpResponses.PublicMenuResponse;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.LocaleHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Models.Views.SpinnerLocale;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venue_menu)
public class VenueMenuActivity
        extends BaseActivity {

    private static final String storageContainer = "userimages\\fboalbums\\200x200";

    @Bean
    VenuesService venueService;

    @Extra
    Venue Venue;
    @Extra
    String SelectedMenuListId; //if the page is navigated via Dishes ListView, this should have value

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout CentralProgress;
    @ViewById(R.id.venueMenu_llToolbox)
    LinearLayout MenuToolboxLayout;
    @ViewById(R.id.venue_tvName)
    TextView NameTextView;
    @ViewById(R.id.venue_tvClass)
    TextView ClassTextView;
    @ViewById(R.id.venue_ivIndoor)
    ImageView IndoorImageView;
    @ViewById(R.id.venue_vpMenu)
    ViewPager MenuViewPager;
    @ViewById(R.id.venue_llNoMenu)
    RelativeLayout NoMenuLayout;

    @ViewById(R.id.venueMenu_spLanguage)
    Spinner LanguagesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        try {
            this.NameTextView.setText(this.Venue.Name);
            this.ClassTextView.setText(this.Venue.Class);
            this.MenuViewPager.setVisibility(View.GONE);
            this.NoMenuLayout.setVisibility(View.VISIBLE);

            loadIndoorImageMeta();
            loadMenu();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadIndoorImageMeta() {
        AsyncTaskListener<VenueImage> apiCallResultListener = new AsyncTaskListener<VenueImage>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(VenueImage result) {
                Venue.IndoorImage = result;
                populateIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getImageMetaIndoor(this.applicationContext, apiCallResultListener, null, this.Venue.Id);
    }

    private void loadMenu(){
        AsyncTaskListener<PublicMenuResponse> apiCallResultListener = new AsyncTaskListener<PublicMenuResponse>() {
            @Override public void onPreExecute() {
                CentralProgress.setVisibility(View.VISIBLE);
            }

            @Override public void onPostExecute(PublicMenuResponse result) {
                PublicMenuResponse publicMenu = result;
                if (publicMenu != null && publicMenu.MenuLists != null && publicMenu.MenuLists.size() > 0) {
                    MenuViewPager.setVisibility(View.VISIBLE);
                    NoMenuLayout.setVisibility(View.GONE);

                    if (publicMenu.MenuCultures != null && publicMenu.MenuCultures.size() > 1)
                    {
                        ArrayList<SpinnerLocale> spinnerLocalesList = new ArrayList<>();
                        for (String menuCulture : result.MenuCultures) {
                            String localeId = menuCulture.length() > 2 ? menuCulture.substring(0,2) : StringHelper.empty();
                            if (StringHelper.isNotNullOrEmpty(localeId)){
                                int localeResourceId = 0;

                                try { localeResourceId = LocaleHelper.SUPPORTED_LOCALES.get(localeId); }
                                catch(Exception e) { /* Do nothing. */ }

                                if (localeResourceId != 0)
                                    spinnerLocalesList.add(new SpinnerLocale(localeId, constructLanguageSpinnerText(localeResourceId)));
                            }
                        }
                        if (spinnerLocalesList.size() > 1) {
                            setupLanguagesSpinner(spinnerLocalesList);
                            MenuToolboxLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    MenuPagerAdapter adapter = new MenuPagerAdapter(getSupportFragmentManager(), publicMenu.MenuLists, SelectedMenuListId);
                    MenuViewPager.setAdapter(adapter);

                    if (SelectedMenuListId != null && !SelectedMenuListId.equals(StringHelper.empty())) {
                        int selectedMenuListIndex = 0;
                        for (MenuList list : publicMenu.MenuLists) {
                            if (SelectedMenuListId.equals(list.Id))
                                selectedMenuListIndex = publicMenu.MenuLists.indexOf(list);
                        }
                        MenuViewPager.setCurrentItem(selectedMenuListIndex, false);
                    }
                }
                CentralProgress.setVisibility(View.GONE);
            }
        };
        this.venueService.getMenu(this.applicationContext, apiCallResultListener, null, this.Venue.Id);
    }

    private void populateIndoorImageThumbnail() {
        if (this.Venue.IndoorImage != null && this.Venue.IndoorImage.Id != null && this.Venue.IndoorImage.Id.length() > 0) {
            //INFO: TryGet Venue Image
            AsyncTaskListener<byte[]> apiCallResultListener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)){
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        IndoorImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                    }
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.Venue.IndoorImage.Id, storageContainer);
            new LoadAzureBlobAsyncTask(apiCallResultListener).execute(model);
        }
    }

    @Click(R.id.venue_lVenueTitle)
    void venueTitle_Click() {
        Intent intent = new Intent(VenueMenuActivity.this, VenueDetailsActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        intent.putExtra("Venue", this.Venue);
        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_right, R.transition.slide_out_right );
    }

    private void setupLanguagesSpinner(ArrayList<SpinnerLocale> localesList) {
        //fill data in spinner
        ArrayAdapter<SpinnerLocale> adapter = new ArrayAdapter<>(this.applicationContext, R.layout.languages_spinner_activity_venue_menu, localesList);
        adapter.setDropDownViewResource(R.layout.languages_spinner_activity_venue_menu);
        this.LanguagesSpinner.setAdapter(adapter);

        String currentDefaultSpinnerLocale = LocaleHelper.getLanguage(applicationContext);

        //TODO: Linq-like syntax needed!
        //Stream.of(cuisineRegionFilters).filter(filter -> filter.Id.equals(filterId)).single();
        for(SpinnerLocale sLocale : localesList){
            if (sLocale.getId().equals(currentDefaultSpinnerLocale)){
                this.LanguagesSpinner.setSelection(adapter.getPosition(sLocale));
                break;
            }
        }

        this.LanguagesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newLocaleId = localesList.get(i).getId();
                if (!currentDefaultSpinnerLocale.equals(newLocaleId)){
                    LocaleHelper.setLocale(applicationContext, newLocaleId);
                    recreate();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private String constructLanguageSpinnerText(int resourceId) {
        String applicationDefaultLocaleTranslation = StringHelper.getStringAppDefaultLocale(this, resourceId);
        String deviceDefaultLocaleTranslation = StringHelper.getStringDeviceDefaultLocale(this, resourceId);
        String label = applicationDefaultLocaleTranslation + " (" + deviceDefaultLocaleTranslation + ")";
        return label;
    }

}