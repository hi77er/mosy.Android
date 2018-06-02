package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.daasuu.bl.BubblePopupHelper;
import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Helpers.ConnectivityHelper;
import com.mosy.kalin.mosy.Helpers.DimensionsHelper;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Listeners.EndlessScrollListener;
import com.mosy.kalin.mosy.Services.AccountService;
import com.mosy.kalin.mosy.Services.DishesService;
import com.mosy.kalin.mosy.Services.Location.LocationResolver;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venues)
@OptionsMenu(R.menu.menu)
public class VenuesActivity
        extends BaseActivity {

    long timeStarted = 0;
    Boolean searchIsPromoted = true; //INFO: Normally search only promoted dishesWall, but when using query then search among all dishesWall
    String query = "searchall";
    int itemsInitialLoadCount = 8;
    int itemsOnScrollLoadCount = 5;
    boolean afterViewsFinished = false;

    private LocationResolver mLocationResolver;
    private Location lastKnownLocation;

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
    static boolean ApplyWorkingStatusFilterToDishes = true;
    @Extra
    static ArrayList<String> SelectedPhaseFilterIds;
    @Extra
    static ArrayList<String> SelectedRegionFilterIds;
    @Extra
    static ArrayList<String> SelectedSpectrumFilterIds;
    @Extra
    static ArrayList<String> SelectedAllergensFilterIds;

    @ViewById(resName = "venues_llInitialLoadingProgress")
    LinearLayout centralProgress;
    @ViewById(resName = "venues_llInvalidHost")
    LinearLayout invalidHostLayout;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView venuesWall;
    @ViewById(resName = "venues_lvDishes")
    ListView dishesWall;

    @ViewById(resName = "venues_ibFilters")
    FloatingActionButton filtersButton;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        incrementActivityUsagesCount();
        timeStarted = System.currentTimeMillis();
        this.applicationContext = getApplicationContext();
    }

    @AfterViews
    void afterViews() {
        configureActionBar();

        if (ConnectivityHelper.isConnected(applicationContext))
            loadData();
        else
            showInvalidHostLayout(); // For any case.. But should never happen because landing activity doesn't show links to this activity if no internet.

        afterViewsFinished = true;
    }

    private void loadData() {
        this.invalidHostLayout.setVisibility(View.GONE);

        dishesWall.setEmptyView(findViewById(R.id.venues_llSwipeToRefresh));
        venuesWall.setEmptyView(findViewById(R.id.venues_llSwipeToRefresh));

        mLocationResolver = new LocationResolver(this);
        mLocationResolver.onStart();
        refreshLastKnownLocation();

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searchIsPromoted = null; //INFO: Normally get only promoted dishesWall, but when searched - search among all dishesWall
        }

        if (!DishesSearchModeActivated) adaptVenueItems();
        else adaptDishItems();

        this.filtersButton.setOnClickListener(v -> openFilters());

        int activityUsagesCount = getActivityUsagesCount();
        if (activityUsagesCount <= 15)
            showFiltersPopupLabel();
    }

    @Override
    protected void onNetworkAvailable() {
        super.onNetworkAvailable();

        if (afterViewsFinished)
            runOnUiThread(this::loadData);
    }

    @Override
    protected void onNetworkLost() {
        super.onNetworkLost();
        if (afterViewsFinished)
            runOnUiThread(this::showInvalidHostLayout);
    }

    private void showInvalidHostLayout() {
        this.invalidHostLayout.setVisibility(View.VISIBLE);
    }

    //INFO: Called in "afterViews"
    private void adaptVenueItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    if (ConnectivityHelper.isConnected(applicationContext) &&
                            !venuesAdapter.LoadingStillInAction &&
                            venuesAdapter.APICallStillReturnsElements) {
                        venuesAdapter.LoadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        loadMoreVenues(itemsOnScrollLoadCount, totalItemsCount, query);
                        return true;
                    }
                    return false;
                }

                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0) filtersButton.show();// scrolling stopped
                    else filtersButton.hide();
                }
            };

            final SwipeRefreshLayout venuesSwipeContainer = findViewById(R.id.venues_lVenuesSwipeContainer);
            venuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
            venuesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (ConnectivityHelper.isConnected(applicationContext)) {
                    refreshLastKnownLocation();
                    venuesAdapter.clearVenues();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreVenues(itemsInitialLoadCount, 0, "searchall");
                }
                endlessScrollListener.resetState();
                venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            venuesAdapter.clearVenues();
            loadMoreVenues(itemsInitialLoadCount, 0, query);

            venuesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
            venuesWall.setAdapter(venuesAdapter);
            venuesWall.setOnScrollListener(endlessScrollListener);
        }
    }

    //INFO: Called in "afterViews"
    private void adaptDishItems() {
        if (ConnectivityHelper.isConnected(applicationContext)) {
            EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    if (ConnectivityHelper.isConnected(applicationContext) &&
                        !dishesAdapter.loadingStillInAction && dishesAdapter.APICallStillReturnsElements) {
                        dishesAdapter.loadingStillInAction = true;

                        //INFO: WHILE SCROLLING LOAD
                        loadMoreDishes(itemsOnScrollLoadCount, totalItemsCount, searchIsPromoted, query,
                                SelectedPhaseFilterIds, SelectedRegionFilterIds, SelectedSpectrumFilterIds, SelectedAllergensFilterIds);
                        return true;
                    }
                    return false;
                }
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0) filtersButton.show();// scrolling stopped
                    else filtersButton.hide();
                }
            };

            final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);
            dishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
            dishesAdapter.swipeContainer.setOnRefreshListener(() -> {
                if (ConnectivityHelper.isConnected(applicationContext)) {
                    refreshLastKnownLocation();
                    dishesAdapter.clearDishes();

                    //INFO: REFRESH INITIAL LOAD
                    loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                            SelectedPhaseFilterIds, SelectedRegionFilterIds, SelectedSpectrumFilterIds, SelectedAllergensFilterIds);
                }

                endlessScrollListener.resetState();
                dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            //INFO: INITIAL LOAD
            dishesAdapter.clearDishes();
            loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                    SelectedPhaseFilterIds, SelectedRegionFilterIds, SelectedSpectrumFilterIds, SelectedAllergensFilterIds);

            dishesWall.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
            dishesWall.setAdapter(dishesAdapter);
            dishesWall.setOnScrollListener(endlessScrollListener);
        }
    }

    void loadMoreVenues(int maxResultsCount, int totalItemsOffset, String query){
        if (ConnectivityHelper.isConnected(applicationContext) &&
                lastKnownLocation != null) {
            AsyncTaskListener<ArrayList<Venue>> listener = new AsyncTaskListener<ArrayList<Venue>>() {
                @Override public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override public void onPostExecute(ArrayList<Venue> result) {
                    centralProgress.setVisibility(View.GONE);
                    venuesWall.setVisibility(View.VISIBLE);

                    if (result != null) {
                        venuesAdapter.addItems(result);
                        venuesAdapter.APICallStillReturnsElements = result.size() >= itemsOnScrollLoadCount;
                        venuesAdapter.LoadingStillInAction = false;
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
                    applicationContext, listener, maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    query, localDayOfWeek, localTime);
        }
    }

    void loadMoreDishes(int maxResultsCount,
                        int totalItemsOffset,
                        Boolean isPromoted,
                        String query,
                        ArrayList<String> phaseFilterIds,
                        ArrayList<String> regionFilterIds,
                        ArrayList<String> spectrumFilterIds,
                        ArrayList<String> allergensFilterIds){

        if (ConnectivityHelper.isConnected(applicationContext) &&
                lastKnownLocation != null) {
            AsyncTaskListener<ArrayList<MenuListItem>> listener = new AsyncTaskListener<ArrayList<MenuListItem>>() {
                @Override
                public void onPreExecute() {
                    centralProgress.setVisibility(View.VISIBLE);
                }
                @Override
                public void onPostExecute(ArrayList<MenuListItem> result) {
                    centralProgress.setVisibility(View.GONE);
                    dishesWall.setVisibility(View.VISIBLE);

                    if (result != null) {
                        dishesAdapter.addItems(result);
                        dishesAdapter.APICallStillReturnsElements = result.size() >= itemsOnScrollLoadCount;
                        dishesAdapter.loadingStillInAction = false;
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

            this.dishesService.getDishes(
                    applicationContext, listener,
                    maxResultsCount, totalItemsOffset,
                    lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(),
                    isPromoted, query,
                    phaseFilterIds, regionFilterIds, spectrumFilterIds, allergensFilterIds,
                    localDayOfWeek, localTime);
        }
        else {
            Toast.makeText(applicationContext, R.string.activity_venues_locationNotResolvedToast, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        if (!DishesSearchModeActivated) {
            menu.findItem(R.id.action_dishes).setVisible(true);
            menu.findItem(R.id.action_venues).setVisible(false);
            searchView.setQueryHint(getString(R.string.activity_venues_searchVenuesHint));
        } else {
            menu.findItem(R.id.action_venues).setVisible(true);
            menu.findItem(R.id.action_dishes).setVisible(false);
            searchView.setQueryHint(getString(R.string.activity_venues_searchDishesHint));
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
                this.NavigateVenuesSearch();
                return true;
            case R.id.action_dishes:
                this.NavigateDishesSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationResolver.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationResolver.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationResolver.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @ItemClick(resName = "venues_lvDishes")
    void dishClicked(MenuListItem listItem) {
        Intent intent = new Intent(VenuesActivity.this, VenueActivity_.class);

        AsyncTaskListener<Venue> listener = new AsyncTaskListener<Venue>() {
            @Override public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override public void onPostExecute(Venue result) {
                Venue venue = result;
                venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
                venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
                venue.Location = null;
                venue.VenueBusinessHours = null;
                venue.VenueContacts = null;
                intent.putExtra("Venue", venue);
                intent.putExtra("SelectedMenuListId", listItem.BrochureId);

                startActivity(intent);
            }
        };

        this.venuesService.getById(applicationContext, listener, listItem.VenueId);
    }

    void openFilters() {
        if (DishesSearchModeActivated) {
            Intent intent = new Intent(VenuesActivity.this, DishesFiltersActivity_.class);
            intent.putExtra("PreselectedPhaseFilterIds", SelectedPhaseFilterIds);
            intent.putExtra("PreselectedRegionFilterIds", SelectedRegionFilterIds);
            intent.putExtra("PreselectedSpectrumFilterIds", SelectedSpectrumFilterIds);
            intent.putExtra("PreselectedAllergensFilterIds", SelectedAllergensFilterIds);
            intent.putExtra("PreselectedApplyWorkingStatusFilter", ApplyWorkingStatusFilterToDishes);
            startActivity(intent);
        } else {
            Intent intent = new Intent(VenuesActivity.this, VenuesFiltersActivity_.class);
            intent.putExtra("PreselectedApplyWorkingStatusFilter", ApplyWorkingStatusFilterToVenues);
            startActivity(intent);
        }
    }

    public void NavigateVenuesSearch() {
        Intent intent = new Intent(VenuesActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", false);
        startActivity(intent);
    }

    public void NavigateDishesSearch() {
        Intent intent = new Intent(VenuesActivity.this, VenuesActivity_.class);
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
                BubbleLayout bubbleLayout = (BubbleLayout) LayoutInflater.from(VenuesActivity.this).inflate(R.layout.popup_bubble, null);
                PopupWindow popupWindow = BubblePopupHelper.create(VenuesActivity.this, bubbleLayout);
                bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);

                filtersButton.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int[] location = new int[2];
                filtersButton.getLocationOnScreen(location);

                Paint mPaint = new Paint();
                mPaint.setTextSize(20);
                String labelText = getResources().getString(R.string.activity_venues_filtersSettingsTextView);
                float width = mPaint.measureText(labelText, 0, labelText.length());

                int filtersButtonStartingY = location[1];
                int popupMarginTop = DimensionsHelper.dpToPx(3, VenuesActivity.this);

                int filtersButtonStartingX = location[0];
                int textWidth = (int)width;
                int textViewMarginEnd = DimensionsHelper.dpToPx(4, VenuesActivity.this);;
                int filtersButtonWidth = filtersButton.getWidth();
                int filtersButtonMarginEnd = DimensionsHelper.dpToPx(25, VenuesActivity.this);
                int popupSidePadding = DimensionsHelper.dpToPx(24, VenuesActivity.this);
                int popupMarginRight = DimensionsHelper.dpToPx(20, VenuesActivity.this);
                int arrowWidth = DimensionsHelper.dpToPx(8, VenuesActivity.this);

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


}