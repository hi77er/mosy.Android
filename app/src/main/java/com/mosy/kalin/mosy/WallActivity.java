package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.mosy.kalin.mosy.Adapters.WallDishesAdapter;
import com.mosy.kalin.mosy.Adapters.WallVenuesAdapter;
import com.mosy.kalin.mosy.CustomControls.Support.RecyclerViewItemsClickSupport;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.DTOs.MenuListItemDetailed;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.DrawableHelper;
import com.mosy.kalin.mosy.Helpers.MetricsHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersHttpResult;
import com.mosy.kalin.mosy.Models.Responses.VenueFiltersHttpResult;
import com.mosy.kalin.mosy.Models.Views.ItemModels.VenueWallItem;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.AzureBlobService;
import com.mosy.kalin.mosy.Services.DishesService;
import com.mosy.kalin.mosy.Services.Location.LocationResolver;
import com.mosy.kalin.mosy.Services.VenuesService;
import com.mosy.kalin.mosy.Models.Views.ItemModels.DishWallItem;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

@SuppressLint("Registered")
@EActivity(R.layout.activity_wall)
@OptionsMenu(R.menu.menu)
public class WallActivity
        extends BaseActivity {

    private long timeStarted = 0;
    private String query = "searchall";
    private int itemsInitialLoadCount = 8;
    private int itemsToLoadCountWhenScrolled = 5;
    private boolean afterViewsFinished = false;
    private boolean networkLost = false;
    private boolean venuesLoadingStillInAction; // used to prevent searching while another async search hasn't been finished
    private boolean dishesLoadingStillInAction; // used to prevent searching while another async search hasn't been finished

    private LruCache<String, Bitmap> mMemoryCache;
    private LocationResolver mLocationResolver;
    private Location lastKnownLocation;
    private DishFiltersHttpResult dishFilters;
    private VenueFiltersHttpResult venueFilters;

    @SystemService
    SearchManager searchManager;

    @Bean
    AccountService accountService;
    @Bean
    VenuesService venuesService;
    @Bean
    DishesService dishesService;

    @Bean
    WallVenuesAdapter wallVenuesAdapter;
    @Bean
    WallDishesAdapter wallDishesAdapter;

    @Extra
    static boolean DishesSearchModeActivated;

    @Extra
    static boolean ApplyRecommendedFilterToDishes = DEFAULT_APPLY_RECOMMENDED_FILTER;
    @Extra
    static boolean ApplyWorkingStatusFilterToVenues = DEFAULT_APPLY_WORKING_STATUS_FILTER;
    @Extra
    static int ApplyDistanceFilterToVenues = DEFAULT_MINIMAL_DISTANCE_FILTER_METERS;
    @Extra
    static int ApplyDistanceFilterToDishes = DEFAULT_MINIMAL_DISTANCE_FILTER_METERS;
    @Extra
    static boolean ApplyWorkingStatusFilterToDishes = DEFAULT_APPLY_WORKING_STATUS_FILTER;
    @Extra
    static ArrayList<String> SelectedVenueAccessibilityFilterIds;
    @Extra
    static ArrayList<String> SelectedVenueAvailabilityFilterIds;
    @Extra
    static ArrayList<String> SelectedVenueAtmosphereFilterIds;
    @Extra
    static ArrayList<String> SelectedVenueCultureFilterIds;
    @Extra
    static ArrayList<String> SelectedDishTypeFilterIds;
    @Extra
    static ArrayList<String> SelectedDishRegionFilterIds;
    @Extra
    static ArrayList<String> SelectedDishMainIngredientFilterIds;
    @Extra
    static ArrayList<String> SelectedDishAllergenFilterIds;
    @Extra
    static boolean NavigatedFromFilters = false;

    @ViewById(R.id.llInitialLoadingProgress)
    LinearLayout centralProgress;
    @ViewById(R.id.venues_llInvalidHost)
    LinearLayout invalidHostLayout;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.venues_lvVenues)
    RecyclerView venuesWall;
    @ViewById(R.id.venues_lvDishes)
    RecyclerView dishesWall;
    @ViewById(R.id.venues_lVenuesSwipeContainer)
    SwipeRefreshLayout venuesSwipeContainer;
    @ViewById(R.id.venues_lDishesSwipeContainer)
    SwipeRefreshLayout dishesSwipeContainer;
    @ViewById(R.id.venues_llSwipeToRefresh)
    LinearLayout llSwipeToRefresh;

    @ViewById(R.id.venues_ibFilters)
    FloatingActionButton filtersButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.incrementActivityUsagesCount();
        timeStarted = System.currentTimeMillis();
    }

    @AfterViews
    void afterViews() {
        configureActionBar();
        final int cacheSize = 10 * 1024 * 1024; // 10 MBs
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        if (checkFiltersSelected()){
            this.filtersButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSecondaryAmber)));
            this.filtersButton.setImageResource(R.drawable.ic_filter_24);
            this.filtersButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        }
        else{
            this.filtersButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimarySalmon)));
            this.filtersButton.setImageResource(R.drawable.ic_filter_remove_outline_24);
            this.filtersButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));
        }

          //INFO: Use this code to test initial usage popups!
//        SharedPreferences mPreferences = this.getSharedPreferences(getString(R.string.pref_walls_visited), Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mPreferences.edit();
//        editor.putInt(getString(R.string.pref_walls_visited), 0);
//        editor.apply();

        if (ConnectivityHelper.isConnected(applicationContext)) {

            networkLost = false;
            int activityUsagesCount = this.getActivityUsagesCount();

            this.loadData();

            if (activityUsagesCount <= 3 && !NavigatedFromFilters)
                this.openFilters();

            if (activityUsagesCount <= 7)
                this.showFiltersPopupLabel();
        }
        else {
            networkLost = true;
            showInvalidHostLayout(); // For any case.. But should never happen because landing activity doesn't show links to this activity if no internet.
        }

        this.afterViewsFinished = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNetworkAvailable() {
        if (afterViewsFinished && networkLost) {
            runOnUiThread(this::loadData);
            networkLost = false;
        }
    }

    @Override
    protected void onNetworkLost() {
        if (afterViewsFinished) {
            runOnUiThread(this::showInvalidHostLayout);
        }
        networkLost = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ConnectivityHelper.isConnected(applicationContext)) {
            showInvalidHostLayout();
        }
    }

    private void loadData() {
        this.invalidHostLayout.setVisibility(View.GONE);

        this.mLocationResolver = new LocationResolver(this);
        this.mLocationResolver.onStart();
        refreshLastKnownLocation();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
            query = intent.getStringExtra(SearchManager.QUERY);

        this.loadVenueFilters();

        this.filtersButton.setOnClickListener(v -> openFilters());
    }

    private void loadVenueFilters(){
        AsyncTaskListener<VenueFiltersHttpResult> listener = new AsyncTaskListener<VenueFiltersHttpResult>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(VenueFiltersHttpResult filtersResult) {
                if (filtersResult != null) {
                    venueFilters = filtersResult;
                    loadDishFilters();
                }
            }
        };

        this.venuesService.getFilters(this.applicationContext, listener, this.isDevelopersModeActivated);
    }

    private void loadDishFilters(){
        AsyncTaskListener<DishFiltersHttpResult> listener = new AsyncTaskListener<DishFiltersHttpResult>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(DishFiltersHttpResult filtersResult) {
                if (filtersResult != null) {
                    dishFilters = filtersResult;
                    if (!DishesSearchModeActivated) adaptVenueItems();
                    else adaptDishItems();
                }
            }
        };

        this.dishesService.getAllFilters(this.applicationContext, listener, this.isDevelopersModeActivated);
    }

    //INFO: Called in "afterViews"
    private void adaptVenueItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            RecyclerView.OnScrollListener venuesScrollListener = new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!venuesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext) && wallVenuesAdapter.APICallStillReturnsElements) {
                        venuesLoadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        int totalItemsCount = recyclerView.getAdapter().getItemCount();
                        loadMoreVenues(itemsToLoadCountWhenScrolled, totalItemsCount, query,
                                SelectedVenueAccessibilityFilterIds, SelectedVenueAvailabilityFilterIds, SelectedVenueAtmosphereFilterIds, SelectedVenueCultureFilterIds);
                    }
                }
                @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                        filtersButton.hide();
                    else
                        filtersButton.show();
                }
            };

            this.venuesWall.setAdapter(wallVenuesAdapter);
            this.venuesWall.setLayoutManager(new GridLayoutManager(this.baseContext, 1));

            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_primary)));
            this.venuesWall.addItemDecoration(itemDecorator);


            this.wallVenuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
            this.wallVenuesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!venuesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    venuesLoadingStillInAction = true;

                    refreshLastKnownLocation();
                    wallVenuesAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreVenues(itemsInitialLoadCount, 0, "searchall",
                            SelectedVenueAccessibilityFilterIds, SelectedVenueAvailabilityFilterIds, SelectedVenueAtmosphereFilterIds, SelectedVenueCultureFilterIds);
                }
                venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            this.wallVenuesAdapter.clearItems();

            this.loadMoreVenues(itemsInitialLoadCount, 0, query,
                    SelectedVenueAccessibilityFilterIds, SelectedVenueAvailabilityFilterIds, SelectedVenueAtmosphereFilterIds, SelectedVenueCultureFilterIds);

//            this.venuesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
            this.venuesWall.addOnScrollListener(venuesScrollListener);
        }
    }

    void loadMoreVenues(int maxResultsCount, int totalItemsOffset, String query,
                        ArrayList<String> selectedVenueAccessibilityFilterIds,
                        ArrayList<String> selectedVenueAvailabilityFilterIds,
                        ArrayList<String> selectedVenueAtmosphereFilterIds,
                        ArrayList<String> selectedVenueCultureFilterIds){
        if (ConnectivityHelper.isConnected(applicationContext) && this.lastKnownLocation != null) {

            AsyncTaskListener<ArrayList<WallVenue>> apiCallResultListener = new AsyncTaskListener<ArrayList<WallVenue>>() {
                @Override public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(ArrayList<WallVenue> results) {
                    centralProgress.setVisibility(View.GONE);
                    venuesWall.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        for (WallVenue item : results) {
                            String matchingFiltersInfo = constructMatchingFiltersInfo(item.MatchingFiltersIds);
                            String mismatchingFiltersInfo = constructMismatchingFiltersInfo(item.MismatchingFiltersIds);

                            if (StringHelper.isNotNullOrEmpty(matchingFiltersInfo) || StringHelper.isNotNullOrEmpty(mismatchingFiltersInfo)) {

                                boolean lastItemHasSameFiltersInfo = wallVenuesAdapter.lastItemHasSameFiltersInfo(item.MatchingFiltersIds, item.MismatchingFiltersIds);
                                if (!lastItemHasSameFiltersInfo)
                                    wallVenuesAdapter.addFiltersInfoHeader(matchingFiltersInfo, mismatchingFiltersInfo);
                            }
                            wallVenuesAdapter.addVenueWallItem(item);

                            VenueWallItem wallItem = wallVenuesAdapter.getItemByVenueId(item.Id);
                            loadVenueItemInteriorImage(wallItem);
                        }

                        wallVenuesAdapter.APICallStillReturnsElements = results.size() >= itemsToLoadCountWhenScrolled;
                        venuesLoadingStillInAction = false;
                    }
                }
            };

            String localDateTimeOffset = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format(Calendar.getInstance().getTime());

            this.venuesService.getVenues(
                    applicationContext, apiCallResultListener, this::showInvalidHostLayout, maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), query,
                    selectedVenueAccessibilityFilterIds, selectedVenueAvailabilityFilterIds, selectedVenueAtmosphereFilterIds, selectedVenueCultureFilterIds,
                    !ApplyWorkingStatusFilterToVenues, localDateTimeOffset, ApplyDistanceFilterToVenues, this.isDevelopersModeActivated);
        }
    }

    private void loadVenueItemInteriorImage(VenueWallItem venueWallItem) {
        if (venueWallItem.WallVenue != null &&
                venueWallItem.WallVenue.IndoorImage != null &&
                StringHelper.isNotNullOrEmpty(venueWallItem.WallVenue.IndoorImage.Id)) {

            venueWallItem.WallVenue.IndoorImage.Bitmap = getBitmapFromMemCache(venueWallItem.WallVenue.IndoorImage.Id);

            if (venueWallItem.WallVenue.IndoorImage.Bitmap == null){
                AsyncTaskListener<byte[]> listener = new AsyncTaskListener<byte[]>() {
                    @Override public void onPreExecute() {
                        //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                    }
                    @Override public void onPostExecute(byte[] bytes) {
                        if (ArrayHelper.hasValidBitmapContent(bytes)) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 200, 200, false);

                            venueWallItem.WallVenue.IndoorImage.Bitmap = scaledBmp;
                            addBitmapToMemoryCache(venueWallItem.WallVenue.IndoorImage.Id, scaledBmp);

                            wallVenuesAdapter.onItemChanged(venueWallItem);
                        }
                        //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                    }
                };

                new AzureBlobService().downloadVenueThumbnail(this.baseContext, venueWallItem.WallVenue.IndoorImage.Id, ImageResolution.Format100x100, listener);
            }
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    //INFO: Called in "afterViews"
    private void adaptDishItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            RecyclerView.OnScrollListener dishesScrollListener = new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                    TypedValue tv = new TypedValue();
//                    int actionBarHeight = 0;
//                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) dishesSwipeContainer.getLayoutParams();
//                    if (dy <= 0){ // scroll down
//                        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
//                            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
//                    }
//                    marginLayoutParams.setMargins(0, actionBarHeight, 0, 0);
//                    dishesSwipeContainer.setLayoutParams(marginLayoutParams);

                    if (!dishesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext) && wallDishesAdapter.APICallStillReturnsElements) {
                        dishesLoadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        int totalItemsCount = recyclerView.getAdapter().getItemCount();
                        loadMoreDishes(itemsToLoadCountWhenScrolled, totalItemsCount, query,
                                SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
                    }
                }
                @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING)
                        filtersButton.hide();
                    else
                        filtersButton.show();
                }
            };

            final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);

            this.dishesWall.setAdapter(wallDishesAdapter);
            this.dishesWall.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider_primary)));
            this.dishesWall.addItemDecoration(itemDecorator);
            this.dishesWall.addOnScrollListener(dishesScrollListener);
//            this.dishesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll for ListView

            RecyclerViewItemsClickSupport.addTo(this.dishesWall).setOnItemClickListener((recyclerView, position, v) -> {
                WallItemBase itemClicked = wallDishesAdapter.getItemAt(position);

                if (itemClicked.getType() == WallItemBase.ITEM_TYPE_DISH_TILE){
                    WallMenuListItem castedItemClicked = ((DishWallItem)itemClicked).WallMenuListItem;
                    MenuListItemDetailed detailed = castedItemClicked.toDetailed();

                    Intent intent = new Intent(WallActivity.this, DetailsItemActivity_.class);

                    AsyncTaskListener<WallVenue> apiCallResultListener = new AsyncTaskListener<WallVenue>() {
                        @Override public void onPreExecute() {
                            centralProgress.setVisibility(View.VISIBLE);
                        }
                        @Override public void onPostExecute(WallVenue venue) {
                            venue.OutdoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
                            venue.IndoorImage = null; // Don't need these one in the WallVenue page. If needed should implement Serializable or Parcelable
                            venue.Location = null;
                            venue.VenueBusinessHours = null;
                            venue.VenueContacts = null;
                            detailed.MenuListItemCultures = null;
                            detailed.MismatchingFiltersIds = null;
                            detailed.MatchingFiltersIds = null;
                            detailed.Filters = null;
                            detailed.Ingredients= null;
                            detailed.VenueBusinessHours = null;
                            detailed.ImageThumbnails = null;
                            intent.putExtra("item", detailed);
                            intent.putExtra("wallVenue", venue);
                            startActivity(intent);
                        }
                    };
                    this.venuesService.getById(applicationContext, apiCallResultListener, this::showInvalidHostLayout, castedItemClicked.VenueId);
                }
                // Else - Do nothing for now
            });

            this.wallDishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
            this.wallDishesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!dishesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    refreshLastKnownLocation();
                    wallDishesAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreDishes(itemsInitialLoadCount, 0, query,
                            SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
                }
                dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            this.wallDishesAdapter.clearItems();
            this.loadMoreDishes(itemsInitialLoadCount, 0, query,
                    SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
        }
    }

    void loadMoreDishes(int maxResultsCount,
                        int totalItemsOffset,
                        String query,
                        ArrayList<String> selectedDishTypeFilterIds,
                        ArrayList<String> selectedDishRegionFilterIds,
                        ArrayList<String> selectedDishMainIngredientFilterIds,
                        ArrayList<String> selectedDishAllergenFilterIds){

        if (ConnectivityHelper.isConnected(applicationContext) &&
                lastKnownLocation != null) {
            AsyncTaskListener<ArrayList<WallMenuListItem>> apiCallResultListener = new AsyncTaskListener<ArrayList<WallMenuListItem>>() {
                @Override
                public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override
                public void onPostExecute(ArrayList<WallMenuListItem> results) {
                    centralProgress.setVisibility(View.GONE);
                    dishesWall.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        for (WallMenuListItem item : results) {
                            String matchingFiltersInfo = constructMatchingFiltersInfo(item.MatchingFiltersIds);
                            String mismatchingFiltersInfo = constructMismatchingFiltersInfo(item.MismatchingFiltersIds);

                            if (StringHelper.isNotNullOrEmpty(matchingFiltersInfo) || StringHelper.isNotNullOrEmpty(mismatchingFiltersInfo)) {

                                boolean lastItemHasSameFiltersInfo = wallDishesAdapter.lastItemHasSameFiltersInfo(item.MatchingFiltersIds, item.MismatchingFiltersIds);
                                if (!lastItemHasSameFiltersInfo)
                                    wallDishesAdapter.addFiltersInfoHeader(matchingFiltersInfo, mismatchingFiltersInfo);
                            }
                            wallDishesAdapter.addDishWallItem(item);

                            DishWallItem wallItem = wallDishesAdapter.getItemByMenuListItemId(item.Id);
                            loadMenuListItemImageThumbnail(wallItem);
                        }

                        wallDishesAdapter.APICallStillReturnsElements = results.size() >= itemsToLoadCountWhenScrolled;
                        dishesLoadingStillInAction = false;
                    }
                }
            };

            String localDateTimeOffset = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US).format(Calendar.getInstance().getTime());

            this.dishesService.loadDishes(
                    applicationContext, apiCallResultListener,
                    maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    query,
                    selectedDishTypeFilterIds, selectedDishRegionFilterIds, selectedDishMainIngredientFilterIds, selectedDishAllergenFilterIds,
                    !ApplyRecommendedFilterToDishes, !ApplyWorkingStatusFilterToDishes, localDateTimeOffset, ApplyDistanceFilterToDishes, this.isDevelopersModeActivated);
        }
        else {
            Toast.makeText(applicationContext, R.string.activity_wall_locationNotResolvedToast, Toast.LENGTH_LONG).show();
        }
    }

    private void loadMenuListItemImageThumbnail(DishWallItem dishWallItem) {
        if (dishWallItem != null &&
                dishWallItem.WallMenuListItem != null &&
                dishWallItem.WallMenuListItem.ImageThumbnail != null &&
                StringHelper.isNotNullOrEmpty(dishWallItem.WallMenuListItem.ImageThumbnail.Id)) {

            dishWallItem.WallMenuListItem.ImageThumbnail.Bitmap = getBitmapFromMemCache(dishWallItem.WallMenuListItem.ImageThumbnail.Id);

            if (dishWallItem.WallMenuListItem.ImageThumbnail.Bitmap == null){
                AsyncTaskListener<byte[]> mliImageResultListener = new AsyncTaskListener<byte[]>() {
                    @Override public void onPreExecute() {
                        //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
                    }
                    @Override public void onPostExecute(byte[] bytes) {
                        if (ArrayHelper.hasValidBitmapContent(bytes)){
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 200, 200, false);

                            dishWallItem.WallMenuListItem.ImageThumbnail.Bitmap = scaledBmp;
                            addBitmapToMemoryCache(dishWallItem.WallMenuListItem.ImageThumbnail.Id, scaledBmp);

                            wallDishesAdapter.onItemChanged(dishWallItem);
                        }
//                        wallDishesAdapter.onItemChanged(dishWallItem);
                        //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
                    }
                };

                new AzureBlobService().downloadMenuListItemThumbnail(this.baseContext, dishWallItem.WallMenuListItem.ImageThumbnail.Id, ImageResolution.Format200x200, mliImageResultListener);
            }
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    private void showInvalidHostLayout() {
        this.invalidHostLayout.setVisibility(View.VISIBLE);
    }

    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuItem actionVenues = menu.findItem(R.id.action_venues);
        DrawableHelper.changeTint(actionVenues.getIcon(), getResources().getColor(R.color.colorWhite), true);
        actionVenues.setVisible(DishesSearchModeActivated);

        MenuItem actionDishes = menu.findItem(R.id.action_dishes);
        DrawableHelper.changeTint(actionDishes.getIcon(), getResources().getColor(R.color.colorWhite), true);
        actionDishes.setVisible(!DishesSearchModeActivated);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        setSearchIcons(searchView);

        String hint = DishesSearchModeActivated ?
                getString(R.string.activity_wall_searchDishesHint) :
                getString(R.string.activity_wall_searchVenuesHint);
        searchView.setQueryHint(hint);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    private void setSearchIcons(SearchView searchView) {
        try {
            Field closeField = SearchView.class.getDeclaredField("mCloseButton");
            closeField.setAccessible(true);
            ImageView closeBtn = (ImageView) closeField.get(searchView);
            closeBtn.setImageResource(R.drawable.ic_close_32);
            closeBtn.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

            Field searchField = SearchView.class.getDeclaredField("mSearchButton");
            searchField.setAccessible(true);
            ImageView searchButton = (ImageView) searchField.get(searchView);
            searchButton.setImageResource(R.drawable.ic_magnify_32);
            searchButton.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorWhite)));

        } catch (NoSuchFieldException e) {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("SearchView", e.getMessage(), e);
        }
    }

    private boolean checkFiltersSelected(){
        return (DishesSearchModeActivated &&
                ((SelectedDishTypeFilterIds != null && SelectedDishTypeFilterIds.size() > 0) ||
                (SelectedDishRegionFilterIds != null && SelectedDishRegionFilterIds.size() > 0) ||
                (SelectedDishMainIngredientFilterIds != null && SelectedDishMainIngredientFilterIds.size() > 0) ||
                (SelectedDishAllergenFilterIds != null && SelectedDishAllergenFilterIds.size() > 0) ||
                 ApplyDistanceFilterToDishes != DEFAULT_MINIMAL_DISTANCE_FILTER_METERS ||
                 ApplyRecommendedFilterToDishes != DEFAULT_APPLY_RECOMMENDED_FILTER ||
                 ApplyWorkingStatusFilterToDishes != DEFAULT_APPLY_WORKING_STATUS_FILTER))
                ||
               (!DishesSearchModeActivated &&
                ((SelectedVenueAccessibilityFilterIds != null && SelectedVenueAccessibilityFilterIds.size() > 0) ||
                (SelectedVenueAvailabilityFilterIds != null && SelectedVenueAvailabilityFilterIds.size() > 0) ||
                (SelectedVenueAtmosphereFilterIds != null && SelectedVenueAtmosphereFilterIds.size() > 0) ||
                (SelectedVenueCultureFilterIds != null && SelectedVenueCultureFilterIds.size() > 0) ||
                 ApplyDistanceFilterToVenues != DEFAULT_MINIMAL_DISTANCE_FILTER_METERS ||
                 ApplyWorkingStatusFilterToVenues != DEFAULT_APPLY_WORKING_STATUS_FILTER));
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.connectionStateMonitor.onAvailable = null;
        this.connectionStateMonitor.onLost = null;
        if (mLocationResolver != null)
            mLocationResolver.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationResolver != null)
            mLocationResolver.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationResolver.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openFilters() {
        if (DishesSearchModeActivated) {
            Intent intent = new Intent(WallActivity.this, DishesFiltersActivity_.class);
            intent.putExtra("PreselectedDistanceFilterValue", ApplyDistanceFilterToDishes);
            intent.putExtra("PreselectedApplyRecommendedFilter", ApplyRecommendedFilterToDishes);
            intent.putExtra("PreselectedApplyWorkingStatusFilter", ApplyWorkingStatusFilterToDishes);
            intent.putExtra("PreselectedDishTypeFilterIds", SelectedDishTypeFilterIds);
            intent.putExtra("PreselectedDishRegionFilterIds", SelectedDishRegionFilterIds);
            intent.putExtra("PreselectedDishMainIngredientFilterIds", SelectedDishMainIngredientFilterIds);
            intent.putExtra("PreselectedDishAllergenFilterIds", SelectedDishAllergenFilterIds);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WallActivity.this, VenuesFiltersActivity_.class);
            intent.putExtra("PreselectedDistanceFilterValue", ApplyDistanceFilterToVenues);
            intent.putExtra("PreselectedApplyWorkingStatusFilter", ApplyWorkingStatusFilterToVenues);
            intent.putExtra("PreselectedVenueAccessibilityFilterIds", SelectedVenueAccessibilityFilterIds);
            intent.putExtra("PreselectedVenueAvailabilityFilterIds", SelectedVenueAvailabilityFilterIds);
            intent.putExtra("PreselectedVenueAtmosphereFilterIds", SelectedVenueAtmosphereFilterIds);
            intent.putExtra("PreselectedVenueCultureFilterIds", SelectedVenueCultureFilterIds);
            startActivity(intent);
        }
    }

    void refreshLastKnownLocation() {
        mLocationResolver.resolveLocation(this, location -> {
            this.lastKnownLocation = location;

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            Log.v("LOCATION CHANGE", timeStamp + " - IN ON_LOCATION_CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
        });
    }

    private void configureActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void incrementActivityUsagesCount() {
        SharedPreferences mPreferences = this.getSharedPreferences(getString(R.string.pref_walls_visited), Context.MODE_PRIVATE);
        int activityUsagesCount = mPreferences.getInt(getString(R.string.pref_walls_visited), 0);

        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(getString(R.string.pref_walls_visited), activityUsagesCount + 1);
        editor.apply();
    }

    private int getActivityUsagesCount() {
        SharedPreferences mPreferences = this.getSharedPreferences(getString(R.string.pref_walls_visited), Context.MODE_PRIVATE);
        return mPreferences.getInt(getString(R.string.pref_walls_visited), 0);
    }

    private void showFiltersPopupLabel() {
        ViewTreeObserver vto = this.filtersButton.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(WallActivity.this).inflate(R.layout.popup_bubble, null);
                PopupWindow popupWindow = BubblePopupHelper.create(WallActivity.this, bubbleLayout);
                bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);

                filtersButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int[] location = new int[2];
                filtersButton.getLocationOnScreen(location);

                Paint mPaint = new Paint();
                mPaint.setTextSize(20);
                String labelText = getResources().getString(R.string.activity_wall_filtersSettingsTextView);
                float width = mPaint.measureText(labelText, 0, labelText.length());

                int filtersButtonStartingY = location[1];
                int popupMarginTop = (int) MetricsHelper.convertDpToPixel(3);

                int filtersButtonStartingX = location[0];
                int textWidth = (int)width;
                int textViewMarginEnd = (int)MetricsHelper.convertDpToPixel(4);;
                int filtersButtonWidth = filtersButton.getWidth();
                int filtersButtonMarginEnd = (int)MetricsHelper.convertDpToPixel(25);
                int popupSidePadding = (int)MetricsHelper.convertDpToPixel(24);
                int popupMarginRight = (int)MetricsHelper.convertDpToPixel(20);
                int arrowWidth = (int)MetricsHelper.convertDpToPixel(8);

                int popupX = filtersButtonStartingX -
                        filtersButtonWidth -
                        filtersButtonMarginEnd -
                        popupSidePadding -
                        textWidth -
                        textViewMarginEnd -
                        popupMarginRight -
                        arrowWidth;
                int popupY = filtersButtonStartingY + popupMarginTop;

                filtersButton.post(() ->
                        popupWindow.showAtLocation(filtersButton, Gravity.NO_GRAVITY, popupX, popupY)
                );
            }
        });
    }

    private String constructMatchingFiltersInfo(ArrayList<String> matchingFiltersIds) {
        return this.getLocalizedNamesByFiltersIds(matchingFiltersIds);
    }

    private String constructMismatchingFiltersInfo(ArrayList<String> mismatchingFiltersIds) {
        return this.getLocalizedNamesByFiltersIds(mismatchingFiltersIds);
    }

    private String getLocalizedNamesByFiltersIds(ArrayList<String> filtersIds) {
        ArrayList<String> localizedFiltersNames = new ArrayList<>();

        if (this.dishFilters != null){
            for (String filterId : filtersIds) {
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.venueFilters.VenueAccessibilityFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.venueFilters.VenueAvailabilityFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.venueFilters.VenueAtmosphereFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.venueFilters.VenueCultureFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.dishFilters.DishTypeFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.dishFilters.DishRegionFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.dishFilters.DishMainIngredientFilters);
                this.constructLocalizedNamesForFilter(localizedFiltersNames, filterId, this.dishFilters.DishAllergenFilters);
            }
            return StringHelper.join(", ", localizedFiltersNames);
        }
        return  StringHelper.empty();
    }

    private void constructLocalizedNamesForFilter(ArrayList<String> localizedFiltersNames, String filterId, ArrayList<Filter> searchedFiltersSet) {
        for (Filter filter : searchedFiltersSet) {
            if (filterId.equals(filter.Id)){
                String localizedFilterName = StringHelper.getStringAppDefaultLocale(getApplicationContext(), getResources(), filter.I18nResourceName, filter.Name);
                localizedFiltersNames.add(localizedFilterName);
            }
        }
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null)
            mMemoryCache.put(key, bitmap);
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void navigateVenuesSearch() {
        Intent intent = new Intent(WallActivity.this, WallActivity_.class);
        intent.putExtra("DishesSearchModeActivated", false);
        startActivity(intent);
    }

    public void navigateDishesSearch() {
        Intent intent = new Intent(WallActivity.this, WallActivity_.class);
        intent.putExtra("DishesSearchModeActivated", true);
        startActivity(intent);
    }

    public void navigateLanding() {
        Intent intent = new Intent(WallActivity.this, LandingActivity_.class);
        startActivity(intent);
    }

    @OptionsItem(R.id.action_venues)
    void actionVenuesClicked(MenuItem item) {
        this.navigateVenuesSearch();
    }

    @OptionsItem(R.id.action_dishes)
    void actionDishesClicked(MenuItem item) {
        this.navigateDishesSearch();
    }

    @Override
    public void onBackPressed() {
        this.navigateLanding();
    }
}