package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.MenuListsPagerAdapter;
import com.mosy.kalin.mosy.DTOs.MenuList;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueMenuBindingModel;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueIndoorImageMetadataAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenueMenuAsyncTask;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venue)
public class VenueActivity
        extends BaseActivity {

    private static final String storageContainer = "userimages\\fboalbums\\200x200";

    private Context applicationContext;

    @Bean
    VenuesService venueService;

    @Extra
    Venue Venue;
    @Extra
    String SelectedMenuListId; //if the page is navigated via Dishes ListView, this should have value

    @ViewById(resName = "venues_llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "venue_tvName")
    TextView Name;
    @ViewById(resName = "venue_tvClass")
    TextView Class;
    @ViewById(resName = "venue_ivIndoor")
    ImageView IndoorImage;
    @ViewById(resName = "venue_vpMenu")
    ViewPager Menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        try {
            Name.setText(this.Venue.Name);
            Class.setText(this.Venue.Class);

            loadIndoorImageMeta();
            loadMenu();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadIndoorImageMeta() {
        AsyncTaskListener<VenueImage> listener = new AsyncTaskListener<VenueImage>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(VenueImage result) {
                Venue.IndoorImage = result;
                populateIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.venueService.getImageMetaIndoor(this.applicationContext, listener, this.Venue.Id);
    }

    private void loadMenu(){
        AsyncTaskListener<ArrayList<MenuList>> listener = new AsyncTaskListener<ArrayList<MenuList>>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }

            @Override public void onPostExecute(ArrayList<MenuList> result) {
                ArrayList<MenuList> menuLists = result;
                MenuListsPagerAdapter adapter = new MenuListsPagerAdapter(getSupportFragmentManager(), menuLists, SelectedMenuListId);
                Menu.setAdapter(adapter);

                if (SelectedMenuListId != null && !SelectedMenuListId.equals(StringHelper.empty())) {
                    int selectedMenuListIndex = 0;
                    for (MenuList list : menuLists) {
                        if (SelectedMenuListId.equals(list.Id))
                            selectedMenuListIndex = menuLists.indexOf(list);
                    }
                    Menu.setCurrentItem(selectedMenuListIndex, false);
                }
                centralProgress.setVisibility(View.GONE);
            }
        };
        this.venueService.getMenu(this.applicationContext, listener, this.Venue.Id);
    }

    private void populateIndoorImageThumbnail() {
        if (this.Venue.IndoorImage != null && this.Venue.IndoorImage.Id != null && this.Venue.IndoorImage.Id.length() > 0) {
            //INFO: TryGet Venue Image
            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
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
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }

    @Click(R.id.venue_lVenueTitle)
    void venueTitle_Click() {
        Intent intent = new Intent(VenueActivity.this, VenueDetailsActivity_.class);
        this.Venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.Venue.Location = null;
        intent.putExtra("Venue", this.Venue);
        startActivity(intent);
        overridePendingTransition( R.transition.slide_in_right, R.transition.slide_out_right );
    }

}