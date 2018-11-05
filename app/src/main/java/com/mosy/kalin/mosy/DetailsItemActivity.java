package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemDetailed;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.DishesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_details_item)
public class DetailsItemActivity
        extends BaseActivity {

    private static final String itemX200BlobStorageContainerPath = "userimages\\requestablealbums\\200x200";
    private static final String itemOriginalBlobStorageContainerPath = "userimages\\requestablealbums\\original";

    boolean descriptionExpanded = false;
    boolean isUsingDefaultThumbnail;

    @Bean
    DishesService dishesService;

    @Extra
    public Venue venue;
    @Extra
    public MenuListItemDetailed item;

    @ViewById(R.id.details_item_ivIndoorThumbnail)
    ImageView itemThumbnailView;

    @ViewById(R.id.details_item_svMain)
    ScrollView mainScrollView;
    @ViewById(R.id.details_item_tvName)
    TextView nameTextView;
//    @ViewById(R.id.details_item_tvIngredients)
//    TextView ingredientsTextView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        try {
            nameTextView.setText(this.item.Name);
//            ingredientsTextView.setText(this.Venue.Class);
//            if (StringHelper.isNotNullOrEmpty(this.Venue.Description)) {
//                descriptionContainerLayout.setVisibility(View.VISIBLE);
//                descriptionTextView.setText(this.Venue.Description.substring(0, 40) + " ...");
//            }

            this.loadIndoorImage();
//            loadContacts();
//            populateFilters();
//            populateCultureFilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadIndoorImage() {
        AsyncTaskListener<MenuListItemImage> apiCallResultListener = new AsyncTaskListener<MenuListItemImage>() {
            @Override
            public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostExecute(MenuListItemImage result) {
                item.ImageThumbnail = result;
                populateIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.dishesService.getImageMeta(this.applicationContext, apiCallResultListener, null, this.item.Id);
    }

    private void populateIndoorImageThumbnail() {
        if (this.item.ImageThumbnail != null && this.item.ImageThumbnail.Id != null && this.item.ImageThumbnail.Id.length() > 0) {
            AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                @Override public void onPreExecute() {
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                }

                @Override public void onPostExecute(byte[] bytes) {
                    if (ArrayHelper.hasValidBitmapContent(bytes)) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        itemThumbnailView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 200, 200, false));
                        isUsingDefaultThumbnail = false;
                    } else
                        isUsingDefaultThumbnail = true;
                    //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                }
            };

            DownloadBlobModel model = new DownloadBlobModel(this.item.ImageThumbnail.Id, itemX200BlobStorageContainerPath);
            new LoadAzureBlobAsyncTask(listener).execute(model);
        }
    }


    @Click(R.id.details_item_btnShowVenue)
    public void showVenue_Clicked(){
        Intent intent = new Intent(DetailsItemActivity.this, DetailsVenueActivity_.class);
        this.venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.venue.Location = null;
        this.venue.VenueBusinessHours = null;
        intent.putExtra("Venue", this.venue);
        this.baseContext.startActivity(intent);
    }

    @Click(R.id.details_item_btnShowMenu)
    public void showMenue_Clicked(){
        Intent intent = new Intent(DetailsItemActivity.this, VenueMenuActivity_.class);
        this.venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        this.venue.Location = null;
        this.venue.VenueBusinessHours = null;
        this.venue.VenueContacts = null;
        intent.putExtra("SelectedMenuListId", this.item.Id);
        intent.putExtra("Venue", this.venue);
        startActivity(intent);
    }

    @Click(R.id.details_item_btnShare)
    public void share_Clicked(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Check out this: " + item.Name;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

}
