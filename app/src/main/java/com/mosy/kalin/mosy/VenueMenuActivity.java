package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mosy.kalin.mosy.Adapters.MenuPagerAdapter;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
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
    LinearLayout centralProgress;
    @ViewById(R.id.venue_tvName)
    TextView Name;
    @ViewById(R.id.venue_tvClass)
    TextView Class;
    @ViewById(R.id.venue_ivIndoor)
    ImageView IndoorImage;
    @ViewById(R.id.venue_vpMenu)
    ViewPager Menu;
    @ViewById(R.id.venue_llNoMenu)
    RelativeLayout NoMenuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        try {
            this.Name.setText(this.Venue.Name);
            this.Class.setText(this.Venue.Class);
            this.Menu.setVisibility(View.GONE);
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
        AsyncTaskListener<ArrayList<MenuList>> apiCallResultListener = new AsyncTaskListener<ArrayList<MenuList>>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }

            @Override public void onPostExecute(ArrayList<MenuList> result) {
                ArrayList<MenuList> menuLists = result;
                if (menuLists != null && menuLists.size() > 0) {
                    Menu.setVisibility(View.VISIBLE);
                    NoMenuLayout.setVisibility(View.GONE);

                    MenuPagerAdapter adapter = new MenuPagerAdapter(getSupportFragmentManager(), menuLists, SelectedMenuListId);
                    Menu.setAdapter(adapter);

                    if (SelectedMenuListId != null && !SelectedMenuListId.equals(StringHelper.empty())) {
                        int selectedMenuListIndex = 0;
                        for (MenuList list : menuLists) {
                            if (SelectedMenuListId.equals(list.Id))
                                selectedMenuListIndex = menuLists.indexOf(list);
                        }
                        Menu.setCurrentItem(selectedMenuListIndex, false);
                    }
                }
                centralProgress.setVisibility(View.GONE);
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
                        IndoorImage.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
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

}