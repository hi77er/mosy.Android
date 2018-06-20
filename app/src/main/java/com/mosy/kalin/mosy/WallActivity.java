package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.CustomControls.Support.RecyclerViewItemsClickSupport;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Filter;
import com.mosy.kalin.mosy.DTOs.Enums.ImageResolution;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ArrayHelper;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.DimensionsHelper;
import com.mosy.kalin.mosy.Helpers.StringHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Models.Responses.DishFiltersResult;
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
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

@SuppressLint("Registered")
@EActivity(R.layout.activity_wall)
@OptionsMenu(R.menu.menu)
public class WallActivity
        extends BaseActivity {

    private long timeStarted = 0;
    private Boolean searchIsPromoted = true; //INFO: Normally search only promoted dishesWall, but when using query then search among all dishesWall
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
    private DishFiltersResult filters;

    @SystemService
    SearchManager searchManager;

    @Bean
    AccountService accountService;
    @Bean
    VenuesService venuesService;
    @Bean
    DishesService dishesService;

    @Bean
    VenuesAdapter venuesAdapter;
    @Bean
    DishesAdapter dishesAdapter;

    @Extra
    static boolean DishesSearchModeActivated;
    @Extra
    static boolean ApplyWorkingStatusFilterToVenues = true;
    @Extra
    static int ApplyDistanceFilterToVenues = 1000;
    @Extra
    static int ApplyDistanceFilterToDishes = 1000;

    @Extra
    static boolean ApplyWorkingStatusFilterToDishes = true;
    @Extra
    static ArrayList<String> SelectedVenuesBadgeFilterIds;
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

    @ViewById(resName = "llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "venues_llInvalidHost")
    LinearLayout invalidHostLayout;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(resName = "venues_lvVenues")
    RecyclerView venuesWall;
    @ViewById(resName = "venues_lvDishes")
    RecyclerView dishesWall;

    @ViewById(resName = "venues_lVenuesSwipeContainer")
    SwipeRefreshLayout venuesSwipeContainer;
    @ViewById(resName = "venues_lDishesSwipeContainer")
    SwipeRefreshLayout dishesSwipeContainer;

    @ViewById(resName = "venues_ibFilters")
    FloatingActionButton filtersButton;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        incrementActivityUsagesCount();
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

        if (ConnectivityHelper.isConnected(applicationContext)) {
            networkLost = false;
            loadData();
        }
        else {
            networkLost = true;
            showInvalidHostLayout(); // For any case.. But should never happen because landing activity doesn't show links to this activity if no internet.
        }


        afterViewsFinished = true;
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
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searchIsPromoted = null; //INFO: Normally get only promoted dishesWall, but when searched - search among all dishesWall
        }

        this.loadDishFilters();

        this.filtersButton.setOnClickListener(v -> openFilters());

        int activityUsagesCount = getActivityUsagesCount();
        if (activityUsagesCount <= 15)
            showFiltersPopupLabel();
    }

    private void loadDishFilters(){
        AsyncTaskListener<DishFiltersResult> listener = new AsyncTaskListener<DishFiltersResult>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }

            @Override public void onPostExecute(DishFiltersResult filtersResult) {
                if (filtersResult != null) {
                    filters = filtersResult;
                }

                if (!DishesSearchModeActivated) adaptVenueItems();
                else adaptDishItems();
            }
        };

        this.dishesService.getFilters(this.applicationContext, listener);
    }

    //INFO: Called in "afterViews"
    private void adaptVenueItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            RecyclerView.OnScrollListener venuesScrollListener = new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!venuesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext) && venuesAdapter.APICallStillReturnsElements) {
                        venuesLoadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        int totalItemsCount = recyclerView.getAdapter().getItemCount();
                        loadMoreVenues(itemsToLoadCountWhenScrolled, totalItemsCount, query, SelectedVenuesBadgeFilterIds, SelectedVenueCultureFilterIds);
                    }
                }
                @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING ||
                        newState == RecyclerView.SCROLL_STATE_SETTLING)
                        filtersButton.hide();
                    else
                        filtersButton.show();
                }
            };

            final SwipeRefreshLayout venuesSwipeContainer = findViewById(R.id.venues_lVenuesSwipeContainer);

            this.venuesWall.setAdapter(venuesAdapter);
            this.venuesWall.setLayoutManager(new GridLayoutManager(this.baseContext, 1));

            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider)));
            this.venuesWall.addItemDecoration(itemDecorator);


            this.venuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
            this.venuesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!venuesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    venuesLoadingStillInAction = true;

                    refreshLastKnownLocation();
                    venuesAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreVenues(itemsInitialLoadCount, 0, "searchall", SelectedVenuesBadgeFilterIds, SelectedVenueCultureFilterIds);
                }
                venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            this.venuesAdapter.clearItems();

            this.loadMoreVenues(itemsInitialLoadCount, 0, query, SelectedVenuesBadgeFilterIds, SelectedVenueCultureFilterIds);

//            this.venuesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
            this.venuesWall.addOnScrollListener(venuesScrollListener);
        }
    }

    void loadMoreVenues(int maxResultsCount, int totalItemsOffset,
                        String query, ArrayList<String> selectedVenueBadgeFilterIds, ArrayList<String> selectedVenueCultureFilterIds){
        if (ConnectivityHelper.isConnected(applicationContext) &&
                this.lastKnownLocation != null) {
            AsyncTaskListener<ArrayList<Venue>> apiCallResultListener = new AsyncTaskListener<ArrayList<Venue>>() {
                @Override public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(ArrayList<Venue> result) {
                    centralProgress.setVisibility(View.GONE);
                    venuesWall.setVisibility(View.VISIBLE);

                    if (result != null) {
                        venuesAdapter.addItems(result);

                        for (Venue venue : result)
                            loadVenueItemOutdoorImage(venue);

                        venuesAdapter.APICallStillReturnsElements = result.size() >= itemsToLoadCountWhenScrolled;
                        venuesLoadingStillInAction = false;
                    }
                }
            };

            Integer localDayOfWeek = null;
            String localTime = null;
            if (ApplyWorkingStatusFilterToVenues) {
                Calendar localCalendar = Calendar.getInstance();
                localTime = localCalendar.get(Calendar.HOUR_OF_DAY) + ":" + localCalendar.get(Calendar.MINUTE);
                localDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
            }

            this.venuesService.getVenues(
                    applicationContext, apiCallResultListener, this::showInvalidHostLayout, maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    query, selectedVenueBadgeFilterIds, selectedVenueCultureFilterIds,
                    localDayOfWeek, localTime, ApplyDistanceFilterToVenues);
        }
    }

    private void loadVenueItemOutdoorImage(Venue venue) {
        AsyncTaskListener<byte[]> outdoorImageResultListener = new AsyncTaskListener<byte[]>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(byte[] bytes) {
                if (ArrayHelper.hasValidBitmapContent(bytes)) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 200, 200, false);
                    venue.OutdoorImage.Bitmap = scaledBmp;
                    addBitmapToMemoryCache(venue.OutdoorImage.Id, scaledBmp);

                    venuesAdapter.onItemChanged(venue);
                }
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        if (venue != null && venue.OutdoorImage != null) {
            if (StringHelper.isNotNullOrEmpty(venue.OutdoorImage.Id)) {
                venue.OutdoorImage.Bitmap = getBitmapFromMemCache(venue.OutdoorImage.Id);
                if (venue.OutdoorImage.Bitmap == null)
                    new AzureBlobService().downloadVenueThumbnail(venue.OutdoorImage.Id, ImageResolution.Format100x100, outdoorImageResultListener);
            }
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    //INFO: Called in "afterViews"
    private void adaptDishItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            RecyclerView.OnScrollListener dishesScrollListener = new RecyclerView.OnScrollListener() {
                @Override public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (!dishesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext) && dishesAdapter.APICallStillReturnsElements) {
                        dishesLoadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        int totalItemsCount = recyclerView.getAdapter().getItemCount();
                        loadMoreDishes(itemsToLoadCountWhenScrolled, totalItemsCount, searchIsPromoted, query,
                                SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
                    }
                }
                @Override public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING ||
                        newState == RecyclerView.SCROLL_STATE_SETTLING)
                        filtersButton.hide();
                    else
                        filtersButton.show();
                }
            };

            final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);

            this.dishesWall.setAdapter(dishesAdapter);
            this.dishesWall.setLayoutManager(new GridLayoutManager(this.baseContext, 1));
            DividerItemDecoration itemDecorator = new DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL);
            itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this.applicationContext, R.drawable.wall_divider)));
            this.dishesWall.addItemDecoration(itemDecorator);
            this.dishesWall.addOnScrollListener(dishesScrollListener);
//            this.dishesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll for ListView

            RecyclerViewItemsClickSupport.addTo(this.dishesWall).setOnItemClickListener((recyclerView, position, v) -> {
                WallItemBase itemClicked = dishesAdapter.getItemAt(position);

                if (itemClicked.getType() == WallItemBase.ITEM_TYPE_DISH_TILE){
                    MenuListItem castedItemClicked = ((DishWallItem)itemClicked).MenuListItem;

                    Intent intent = new Intent(WallActivity.this, VenueMenuActivity_.class);

                    AsyncTaskListener<Venue> apiCallResultListener = new AsyncTaskListener<Venue>() {
                        @Override public void onPreExecute() {
                            centralProgress.setVisibility(View.VISIBLE);
                        }
                        @Override public void onPostExecute(Venue venue) {
                            venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
                            venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
                            venue.Location = null;
                            venue.VenueBusinessHours = null;
                            venue.VenueContacts = null;
                            intent.putExtra("Venue", venue);
                            intent.putExtra("SelectedMenuListId", castedItemClicked.BrochureId);
                            startActivity(intent);
                        }
                    };
                    this.venuesService.getById(applicationContext, apiCallResultListener, this::showInvalidHostLayout, castedItemClicked.VenueId);
                }
                // Else - Do nothing for now
            });

            this.dishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
            this.dishesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (!dishesLoadingStillInAction && ConnectivityHelper.isConnected(applicationContext)) {
                    refreshLastKnownLocation();
                    dishesAdapter.clearItems();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                            SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
                }
                dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            this.dishesAdapter.clearItems();
            this.loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                    SelectedDishTypeFilterIds, SelectedDishRegionFilterIds, SelectedDishMainIngredientFilterIds, SelectedDishAllergenFilterIds);
        }
    }

    void loadMoreDishes(int maxResultsCount,
                        int totalItemsOffset,
                        Boolean isPromoted,
                        String query,
                        ArrayList<String> selectedDishTypeFilterIds,
                        ArrayList<String> selectedDishRegionFilterIds,
                        ArrayList<String> selectedDishMainIngredientFilterIds,
                        ArrayList<String> selectedDishAllergenFilterIds){

        if (ConnectivityHelper.isConnected(applicationContext) &&
                lastKnownLocation != null) {
            AsyncTaskListener<ArrayList<MenuListItem>> apiCallResultListener = new AsyncTaskListener<ArrayList<MenuListItem>>() {
                @Override
                public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override
                public void onPostExecute(ArrayList<MenuListItem> results) {
                    centralProgress.setVisibility(View.GONE);
                    dishesWall.setVisibility(View.VISIBLE);

                    if (results != null && results.size() > 0) {
                        for (MenuListItem item : results) {
                            String matchingFiltersInfo = constructMatchingFiltersInfo(item.MatchingFiltersIds);
                            String mismatchingFiltersInfo = constructMismatchingFiltersInfo(item.MismatchingFiltersIds);

                            if (StringHelper.isNotNullOrEmpty(matchingFiltersInfo) || StringHelper.isNotNullOrEmpty(mismatchingFiltersInfo)) {

                                boolean prevItemHasSameFiltersInfo = dishesAdapter.previousItemHasSameFiltersInfo(item.MatchingFiltersIds, item.MismatchingFiltersIds);
                                if (!prevItemHasSameFiltersInfo)
                                    dishesAdapter.addFiltersInfoHeader(matchingFiltersInfo, mismatchingFiltersInfo);
                            }
                            dishesAdapter.addDishWallItem(item);
                        }

                        for (MenuListItem menuListItem : results) {
                            DishWallItem wallItem = dishesAdapter.getItemByMenuListItemId(menuListItem.Id);
                            loadMenuListItemImageThumbnail(wallItem);
                        }

                        dishesAdapter.APICallStillReturnsElements = results.size() >= itemsToLoadCountWhenScrolled;
                        dishesLoadingStillInAction = false;
                    }
                }
            };

            Integer localDayOfWeek = null;
            String localTime = null;
            if (ApplyWorkingStatusFilterToDishes)
            {
                Calendar localCalendar = Calendar.getInstance();
                localTime = localCalendar.get(Calendar.HOUR_OF_DAY) + ":" + localCalendar.get(Calendar.MINUTE);
                localDayOfWeek = localCalendar.get(Calendar.DAY_OF_WEEK);
            }

            this.dishesService.loadDishes(
                    applicationContext, apiCallResultListener,
                    maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    isPromoted, query,
                    selectedDishTypeFilterIds, selectedDishRegionFilterIds, selectedDishMainIngredientFilterIds, selectedDishAllergenFilterIds,
                    localDayOfWeek, localTime, ApplyDistanceFilterToDishes);
        }
        else {
            Toast.makeText(applicationContext, R.string.activity_wall_locationNotResolvedToast, Toast.LENGTH_LONG).show();
        }

    }

    private void loadMenuListItemImageThumbnail(DishWallItem dishWallItem) {
        AsyncTaskListener<byte[]> mliImageResultListener = new AsyncTaskListener<byte[]>() {
            @Override public void onPreExecute() {
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(byte[] bytes) {
                if (ArrayHelper.hasValidBitmapContent(bytes)){
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 200, 200, false);
                    dishWallItem.MenuListItem.ImageThumbnail.Bitmap = scaledBmp;
                    addBitmapToMemoryCache(dishWallItem.MenuListItem.ImageThumbnail.Id, scaledBmp);
                }
                dishesAdapter.onItemChanged(dishWallItem);
                //INFO: HERE IF NECESSARY: progress.setVisibility(View.GONE);
            }
        };

        if (dishWallItem != null && dishWallItem.MenuListItem != null && dishWallItem.MenuListItem.ImageThumbnail != null) {
            if (StringHelper.isNotNullOrEmpty(dishWallItem.MenuListItem.ImageThumbnail.Id)) {
                dishWallItem.MenuListItem.ImageThumbnail.Bitmap = getBitmapFromMemCache(dishWallItem.MenuListItem.ImageThumbnail.Id);
                if (dishWallItem.MenuListItem.ImageThumbnail.Bitmap == null)
                    new AzureBlobService().downloadMenuListItemThumbnail(dishWallItem.MenuListItem.ImageThumbnail.Id, ImageResolution.Format200x200, mliImageResultListener);
            }
        } //IN ALL 3 'ELSE'S DO NOTHING. The Item has already been set the default image
    }

    private void showInvalidHostLayout() {
        this.invalidHostLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (!DishesSearchModeActivated) {
            menu.findItem(R.id.action_dishes).setVisible(true);
            menu.findItem(R.id.action_venues).setVisible(false);
            searchView.setQueryHint(getString(R.string.activity_wall_searchVenuesHint));
        } else {
            menu.findItem(R.id.action_venues).setVisible(true);
            menu.findItem(R.id.action_dishes).setVisible(false);
            searchView.setQueryHint(getString(R.string.activity_wall_searchDishesHint));
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_venues:
                this.navigateVenuesSearch();
                return true;
            case R.id.action_dishes:
                this.navigateDishesSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            intent.putExtra("PreselectedVenueBadgeFilterIds", SelectedVenuesBadgeFilterIds);
            intent.putExtra("PreselectedVenueCultureFilterIds", SelectedVenueCultureFilterIds);
            startActivity(intent);
        }
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
                int popupMarginTop = DimensionsHelper.dpToPx(3, WallActivity.this);

                int filtersButtonStartingX = location[0];
                int textWidth = (int)width;
                int textViewMarginEnd = DimensionsHelper.dpToPx(4, WallActivity.this);;
                int filtersButtonWidth = filtersButton.getWidth();
                int filtersButtonMarginEnd = DimensionsHelper.dpToPx(25, WallActivity.this);
                int popupSidePadding = DimensionsHelper.dpToPx(24, WallActivity.this);
                int popupMarginRight = DimensionsHelper.dpToPx(20, WallActivity.this);
                int arrowWidth = DimensionsHelper.dpToPx(8, WallActivity.this);

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

        if (this.filters != null){
            for (String filterId : filtersIds) {
                for (Filter filter : this.filters.DishTypeFilters) {
                    if (filterId.equals(filter.Id)){
                        String localizedFilterName = StringHelper.getStringAppDefaultLocale(getApplicationContext(), getResources(), filter.I18nResourceName, filter.Name);
                        localizedFiltersNames.add(localizedFilterName);
                    }
                }
                for (Filter filter : this.filters.DishRegionFilters) {
                    if (filterId.equals(filter.Id)){
                        String localizedFilterName = StringHelper.getStringAppDefaultLocale(getApplicationContext(), getResources(), filter.I18nResourceName, filter.Name);
                        localizedFiltersNames.add(localizedFilterName);
                    }
                }
                for (Filter filter : this.filters.DishMainIngredientFilters) {
                    if (filterId.equals(filter.Id)){
                        String localizedFilterName = StringHelper.getStringAppDefaultLocale(getApplicationContext(), getResources(), filter.I18nResourceName, filter.Name);
                        localizedFiltersNames.add(localizedFilterName);
                    }
                }
                for (Filter filter : this.filters.DishAllergenFilters) {
                    if (filterId.equals(filter.Id)){
                        String localizedFilterName = StringHelper.getStringAppDefaultLocale(getApplicationContext(), getResources(), filter.I18nResourceName, filter.Name);
                        localizedFiltersNames.add(localizedFilterName);
                    }
                }
            }
            return StringHelper.join(", ", localizedFiltersNames);
        }
        return  StringHelper.empty();
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null)
            mMemoryCache.put(key, bitmap);
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}