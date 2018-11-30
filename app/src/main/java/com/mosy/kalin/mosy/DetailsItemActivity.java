package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.annimon.stream.Stream;
import com.mosy.kalin.mosy.DTOs.Enums.FilterType;
import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Ingredient;
import com.mosy.kalin.mosy.DTOs.MenuListItemCulture;
import com.mosy.kalin.mosy.DTOs.MenuListItemDetailed;
import com.mosy.kalin.mosy.DTOs.MenuListItemImage;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.DrawableHelper;
import com.mosy.kalin.mosy.Helpers.MetricsHelper;
import com.mosy.kalin.mosy.Helpers.NetworkHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.AzureModels.DownloadBlobModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadAzureBlobAsyncTask;
import com.mosy.kalin.mosy.Services.AzureBlobService;
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
    private static int SEEN_TIME_OUT = 5000; // 5 seconds

    static boolean resumed = false;

    boolean ingredientsExpanded = false;
    String entireIngredientsText = StringHelper.empty();
    boolean isUsingDefaultThumbnail;

    boolean descriptionExpanded = false;

    public MenuListItemCulture itemCulture;

    @Bean
    DishesService dishesService;

    @Extra
    public Venue venue;
    @Extra
    public MenuListItemDetailed item;

    @ViewById(R.id.details_item_llMain)
    LinearLayout mainLayout;

    @ViewById(R.id.details_item_ivThumbnail)
    ImageView itemThumbnailView;

    @ViewById(R.id.details_item_tvName)
    TextView nameTextView;
    @ViewById(R.id.details_item_tvViews)
    TextView viewsTextView;
    @ViewById(R.id.details_item_tvDescription)
    TextView descriptionTextView;
    @ViewById(R.id.details_item_tvIngredients)
    TextView ingredientsTextView;

    @ViewById(R.id.details_item_lDescriptionContainer)
    LinearLayout descriptionContainerLayout;
    @ViewById(R.id.details_item_lIngredientsContainer)
    LinearLayout ingredientsContainerLayout;
    @ViewById(R.id.details_item_lFiltersContainer)
    LinearLayout itemFiltersContainer;
    @ViewById(R.id.details_item_lAllergenFiltersContainer)
    LinearLayout itemAllergenFiltersContainer;


    @ViewById(R.id.details_item_lMainProgress)
    LinearLayout mainProgressLayout;
    @ViewById(R.id.details_item_lFiltersProgress)
    LinearLayout itemFiltersProgressLayout;
    @ViewById(R.id.details_item_lAllergenFiltersProgress)
    LinearLayout itemAllergenFiltersProgressLayout;

    @ViewById(R.id.details_item_lFilters)
    LinearLayout filtersLayout;
    @ViewById(R.id.details_item_lAllergenFilters)
    LinearLayout allergenFiltersLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        resumed = true;
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        try {
            new Handler().postDelayed(() -> {
                // test functions
//                String asda0 = NetworkHelper.getMACAddress("wlan0");
//                String asda1 = NetworkHelper.getMACAddress("eth0");
//                String asda2 = NetworkHelper.getIPAddress(true); // IPv4
//                String asda3 = NetworkHelper.getIPAddress(false); // IPv6

                if (resumed)
                    this.dishesService.checkAddView(this.applicationContext, item.Id);
            }, SEEN_TIME_OUT);

            this.publishViews();
            this.loadItemCulture();
            this.loadIndoorImage();
            this.loadFilters();
//            loadContacts();
//            publishFilters();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    protected void publishViews(){
        this.viewsTextView.setText(this.item.SeenCount + " " + getString(R.string.activity_itemDetails_viewsTextView));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resumed = false;
    }

    private void loadItemCulture() {
        AsyncTaskListener<MenuListItemCulture> apiCallResultListener = new AsyncTaskListener<MenuListItemCulture>() {
            @Override
            public void onPreExecute() {
                showMainProgress();
            }

            @Override
            public void onPostExecute(MenuListItemCulture result) {
                itemCulture = result;
                publishItemCulture();
                hideMainProgress();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.dishesService.getItemPreferredCulture(this.applicationContext, apiCallResultListener, null, this.item.Id);
    }

    private void showMainProgress() {
        mainProgressLayout.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void hideMainProgress() {
        mainProgressLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

    }

    private void publishItemCulture() {
        if (this.itemCulture != null){
            nameTextView.setText(this.itemCulture.MenuListItemName);

            if (StringHelper.isNotNullOrEmpty(this.itemCulture.MenuListItemDescription)) {
                String descriptionText = this.itemCulture.MenuListItemDescription.length() < 41
                        ? this.itemCulture.MenuListItemDescription
                        : this.itemCulture.MenuListItemDescription.substring(0, 40) + " ...";

                descriptionTextView.setText(descriptionText);
                this.descriptionContainerLayout.setVisibility(View.VISIBLE);
            }

            if (this.itemCulture != null && this.itemCulture.Ingredients != null && this.itemCulture.Ingredients.size() > 0) {
                StringBuilder ingredientsTextBuilder = new StringBuilder(StringHelper.empty());
                boolean firstEntry = true;
                for(Ingredient ingredient : this.itemCulture.Ingredients)
                {
                    String delimiter = firstEntry ? StringHelper.empty() : ", ";
                    firstEntry = false;
                    ingredientsTextBuilder.append(delimiter).append(ingredient.Name);
                }
                this.entireIngredientsText = ingredientsTextBuilder.toString();

                String ingredientsText = this.entireIngredientsText.length() < 41
                        ? this.entireIngredientsText
                        : this.entireIngredientsText.substring(0, 40) + " ...";

                ingredientsTextView.setText(ingredientsText);
                this.ingredientsContainerLayout.setVisibility(View.VISIBLE);
            }

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
                publishIndoorImageThumbnail();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.dishesService.getImageMeta(this.applicationContext, apiCallResultListener, null, this.item.Id);
    }

    private void loadFilters() {
        AsyncTaskListener<ArrayList<Filter>> listener = new AsyncTaskListener<ArrayList<Filter>>() {
            @Override public void onPreExecute() {
                showFiltersLoading();
            }
            @Override public void onPostExecute(ArrayList<Filter> result) {
                item.Filters = result;
                publishFilters();
                publishAllergenFilters();
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };
        this.dishesService.getFilters(this.applicationContext, listener, item.Id);
    }

    private void publishFilters() {
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

    private void publishAllergenFilters() {
        ArrayList<Filter> filters = this.item.Filters;

        if (filters != null && filters.size() > 0) {
            ArrayList<Filter> allergenFilters = new ArrayList<>(Stream.of(filters).filter(x -> x.FilterType == FilterType.DishAllergens).toList());

            boolean anyAllergenFilter = this.iterateFiltersHasAny(allergenFilters, true, this.allergenFiltersLayout);

            if (anyAllergenFilter)
                this.showAllergenFiltersContainer();
            else
                this.hideAllergenFiltersContainer();
        }
        else
            this.hideAllergenFiltersContainer();
    }

    private void showFiltersContainer() {
        this.itemFiltersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.VISIBLE);
        this.itemFiltersContainer.setVisibility(View.VISIBLE);
    }

    private void hideFiltersContainer() {
        this.itemFiltersProgressLayout.setVisibility(View.GONE);
        this.filtersLayout.setVisibility(View.GONE);
        this.itemFiltersContainer.setVisibility(View.GONE);
    }

    public  void showAllergenFiltersContainer() {
        this.itemAllergenFiltersProgressLayout.setVisibility(View.GONE);
        this.allergenFiltersLayout.setVisibility(View.VISIBLE);
        this.itemAllergenFiltersContainer.setVisibility(View.VISIBLE);
    }

    public  void hideAllergenFiltersContainer() {
        this.itemAllergenFiltersProgressLayout.setVisibility(View.GONE);
        this.allergenFiltersLayout.setVisibility(View.GONE);
        this.itemAllergenFiltersContainer.setVisibility(View.GONE);
    }

    private boolean iterateFiltersHasAny(ArrayList<Filter> filters, boolean needSquareIcon, LinearLayout container) {
        boolean hasAny = false;
        for (Filter filter : filters) {
            int drawableId = DrawableHelper.getDrawableIdByFilterId(filter.Id);
            if (drawableId != 0) {
                ImageView filterImageView = this.createFilterImage(drawableId, needSquareIcon ? 31 : 46,
                        filter.I18nResourceName, filter.Name, filter.I18nResourceDescription, filter.Description);
                container.addView(filterImageView);
                hasAny = true;
            }
        }
        return hasAny;
    }

    private ImageView createFilterImage(int drawableId, int widthDp,
            String nameResourceI18nId, String nameDescriptionText, String descriptionResourceI18nId, String defaultDescriptionText) {
        ImageView filterImageView = new ImageView(this.baseContext);
        filterImageView.setImageDrawable(getResources().getDrawable(drawableId));
        filterImageView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        filterImageView.setPadding((int)MetricsHelper.convertDpToPixel(3),
                (int)MetricsHelper.convertDpToPixel(3),
                (int)MetricsHelper.convertDpToPixel(3),
                (int)MetricsHelper.convertDpToPixel(3));

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width = (int) MetricsHelper.convertDpToPixel(widthDp);
        lp.height = (int)MetricsHelper.convertDpToPixel(31);
        lp.setMargins((int)MetricsHelper.convertDpToPixel(6), 0, 0, 0);
        filterImageView.setLayoutParams(lp);

        filterImageView.setOnClickListener(view -> this.filterClick(nameResourceI18nId, nameDescriptionText, descriptionResourceI18nId, defaultDescriptionText));

        filterImageView.setVisibility(View.VISIBLE);
        return filterImageView;
    }

    private void filterClick(String nameResourceI18nId, String defaultNameText, String descriptionResourceI18nId, String defaultDescriptionText) {
        String filterNameLocalized = StringHelper.getStringAppDefaultLocale(this, getResources(), nameResourceI18nId, defaultNameText);
        String filterDescriptionLocalized = StringHelper.getStringAppDefaultLocale(this, getResources(), descriptionResourceI18nId, defaultDescriptionText);
        if (StringHelper.isNotNullOrEmpty(filterDescriptionLocalized))
            new AlertDialog.Builder(this)
                    .setTitle(filterNameLocalized)
                    .setMessage(filterDescriptionLocalized)
                    .setPositiveButton(android.R.string.ok, (dialog, which) ->  dialog.cancel())
                    .show();
    }

    private void publishIndoorImageThumbnail() {
        if (this.item != null &&
                this.item.ImageThumbnail != null &&
                StringHelper.isNotNullOrEmpty(this.item.ImageThumbnail.Id)) {

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

            new AzureBlobService().downloadMenuListItemThumbnail(this.baseContext, this.item.ImageThumbnail.Id, ImageResolution.Format200x200, listener);
        }
    }

    private boolean hasValidIndoorImageMetadata() {
        return this.item.ImageThumbnail!= null
                && this.item.ImageThumbnail.Id != null
                && this.item.ImageThumbnail.Id.length() > 0;
    }

    private void showFiltersLoading() {
        this.itemFiltersContainer.setVisibility(View.GONE);
        this.itemFiltersProgressLayout.setVisibility(View.VISIBLE);
    }

    @Click(R.id.details_item_ivThumbnail)
    public void ImageClick() {
        if (!isUsingDefaultThumbnail &&
                hasValidIndoorImageMetadata() &&
                !isUsingDefaultThumbnail && hasValidIndoorImageMetadata() &&
                this.item != null &&
                this.item.ImageThumbnail != null &&
                StringHelper.isNotNullOrEmpty(this.item.ImageThumbnail.Id)) {

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

            new AzureBlobService().downloadMenuListItemThumbnail(this.baseContext, this.item.ImageThumbnail.Id, ImageResolution.FormatOriginal, listener);
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
        String baseItemUrl = "https://www.treatspark.com/MenuItem/Index?id=";
        String fullItemUrl = baseItemUrl + this.item.Id;

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Check out this " + item.Name + ". " + fullItemUrl;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @SuppressLint("SetTextI18n")
    @Click(R.id.details_item_lIngredientsContainer)
    public void ingredientsClicked() {
        ingredientsExpanded = !ingredientsExpanded;

        if (ingredientsExpanded)
            ingredientsTextView.setText(this.entireIngredientsText);
        else{
            String ingredientsText = this.entireIngredientsText.length() < 41
                    ? this.entireIngredientsText
                    : this.entireIngredientsText.substring(0, 40) + " ...";

            ingredientsTextView.setText(ingredientsText);
        }
    }

    @SuppressLint("SetTextI18n")
    @Click(R.id.details_item_lDescriptionContainer)
    public void descriptionClicked() {
        descriptionExpanded = !descriptionExpanded;

        if (descriptionExpanded)
            descriptionTextView.setText(this.itemCulture.MenuListItemDescription);
        else{
            String descriptionText = this.itemCulture.MenuListItemDescription.length() < 41
                    ? this.itemCulture.MenuListItemDescription
                    : this.itemCulture.MenuListItemDescription.substring(0, 40) + " ...";

            descriptionTextView.setText(descriptionText);
        }
    }
}
