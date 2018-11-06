package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemDetailed;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueContacts;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.DrawableHelper;
import com.mosy.kalin.mosy.Helpers.MetricsHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
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

import java.util.ArrayList;

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

    @ViewById(R.id.details_item_ivThumbnail)
    ImageView itemThumbnailView;

    @ViewById(R.id.details_item_svMain)
    ScrollView mainScrollView;
    @ViewById(R.id.details_item_tvName)
    TextView nameTextView;
//    @ViewById(R.id.details_item_tvIngredients)
//    TextView ingredientsTextView;

    @ViewById(R.id.details_item_lFiltersContainer)
    LinearLayout itemFiltersLayout;

    @ViewById(R.id.details_item_lFiltersProgress)
    LinearLayout itemFiltersProgressLayout;

    @ViewById(R.id.details_item_lFilters)
    LinearLayout filtersLayout;




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
            this.loadFilters();
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


    private void loadFilters() {
        AsyncTaskListener<ArrayList<Filter>> listener = new AsyncTaskListener<ArrayList<Filter>>() {
            @Override public void onPreExecute() {
                showFiltesLoading();
            }
            @Override public void onPostExecute(ArrayList<Filter> result) {
                item.Filters = result;
                populateFilters();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.dishesService.getFilters(this.applicationContext, listener, item.Id);
    }

    private void populateFilters() {
        ArrayList<Filter> filters = this.item.Filters;

        if (filters != null && filters.size() > 0) {
            ArrayList<Filter> typeFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.DishType).toList());
            ArrayList<Filter> mainIngredientFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.DishMainIngredient).toList());
            ArrayList<Filter> regionFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.DishRegion).toList());

            boolean anyTypeFilter = this.iterateFiltersHasAny(typeFilters, true, this.filtersLayout);
            boolean anyMainIngredientFilter = this.iterateFiltersHasAny(mainIngredientFilters, true, this.filtersLayout);
            boolean anyRegionFilter = this.iterateFiltersHasAny(regionFilters, true, this.filtersLayout);

            if (anyTypeFilter || anyMainIngredientFilter || anyRegionFilter)
                this.showFiltersContainer();
            else
                this.hideFiltersContainer();
        }
        else
            this.hideFiltersContainer();

    }

    private void showFiltersContainer() {
        this.itemFiltersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.VISIBLE);
        this.itemFiltersLayout.setVisibility(View.VISIBLE);
    }

    private void hideFiltersContainer() {
        this.itemFiltersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.GONE);
        this.itemFiltersLayout.setVisibility(View.GONE);
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
        lp.width = (int) MetricsHelper.convertDpToPixel(widthDp);
        lp.height = (int)MetricsHelper.convertDpToPixel(31);
        lp.setMargins((int)MetricsHelper.convertDpToPixel(6), 0, 0, 0);
        filterImageView.setLayoutParams(lp);

        filterImageView.setOnClickListener(view -> this.filterClick(descriptionResourceI18nId, defaultDescriptionText));

        filterImageView.setVisibility(View.VISIBLE);
        return filterImageView;
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

    private boolean hasValidIndoorImageMetadata() {
        return this.item.ImageThumbnail!= null
                && this.item.ImageThumbnail.Id != null
                && this.item.ImageThumbnail.Id.length() > 0;
    }

    private void showFiltesLoading() {
        this.itemFiltersLayout.setVisibility(View.GONE);
        this.itemFiltersProgressLayout.setVisibility(View.VISIBLE);
    }


    @Click(R.id.details_item_ivThumbnail)
    public void ImageClick() {
        if (!isUsingDefaultThumbnail && hasValidIndoorImageMetadata()) {
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

            DownloadBlobModel model = new DownloadBlobModel(this.item.ImageThumbnail.Id, itemOriginalBlobStorageContainerPath);
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
    public void showMenu_Clicked(){
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
