package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.AsyncTaskListener;
import com.mosy.kalin.mosy.Listeners.EndlessScrollListener;
import com.mosy.kalin.mosy.Models.BindingModels.SearchMenuListItemsBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadMenuListItemsAsyncTask;
import com.mosy.kalin.mosy.Services.AsyncTasks.LoadVenuesAsyncTask;
import com.mosy.kalin.mosy.Services.LocationResolver;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venues)
@OptionsMenu(R.menu.menu)
public class VenuesActivity
        extends AppCompatActivity {

    @Extra
    static boolean DishesSearchModeActivated;
    @Extra
    static ArrayList<String> CuisinePhaseFilterIds;
    @Extra
    static ArrayList<String> CuisineRegionFilterIds;
    @Extra
    static ArrayList<String> CuisineSpectrumFilterIds;

    long timeStarted = 0;
    Boolean searchIsPromoted = true; //INFO: Normally search only promoted dishes, but when using query then search among all dishes
    String query = "searchall";
    int itemsInitialLoadCount = 8;
    int itemsOnScrollLoadCount = 5;

    private LocationResolver mLocationResolver;
    private Location lastKnownLocation;

    @SystemService
    SearchManager searchManager;

    @Bean
    VenuesService venuesService;
    @Bean
    VenuesAdapter venuesAdapter;
    @Bean
    DishesAdapter dishesAdapter;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView venues;
    @ViewById(resName = "venues_lvDishes")
    ListView dishes;
    @ViewById(resName = "venues_llInitialLoadingProgress")
    LinearLayout centralProgress;

    SearchView searchView;
    FloatingActionButton filters;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStarted = System.currentTimeMillis();

        mLocationResolver = new LocationResolver(this);
        mLocationResolver.onStart();
        refreshLastKnownLocation();
    }

    @AfterViews
    void afterViews() {
        configureActionBar();

        this.filters = findViewById(R.id.venues_ibFilters);
        this.filters.setOnClickListener(v -> openFilters());

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searchIsPromoted = null; //INFO: Normally search only promoted dishes, but when using query then search among all dishes
        }

        if (!DishesSearchModeActivated) adaptVenueItems();
        else adaptDishItems();
    }

    //INFO: Called in "afterViews"
    private void adaptVenueItems() {
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!venuesAdapter.LoadingStillInAction && venuesAdapter.APICallStillReturnsElements)
                {
                    venuesAdapter.LoadingStillInAction = true;

                    //INFO: WHILE SCROLLING LOAD
                    loadMoreVenues(itemsOnScrollLoadCount, totalItemsCount, query);
                }
                return true;
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) filters.show();// scrolling stopped
                else filters.hide();
            }
        };

        final SwipeRefreshLayout venuesSwipeContainer = findViewById(R.id.venues_lVenuesSwipeContainer);
        venuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
        venuesAdapter.swipeContainer.setOnRefreshListener(() -> {
            refreshLastKnownLocation();
            venuesAdapter.clearVenues();

            //INFO: REFRESH INITIAL LOAD
            loadMoreVenues(itemsInitialLoadCount, 0, "searchall");

            endlessScrollListener.resetState();
            venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
        });

        //INFO: INITIAL LOAD
        loadMoreVenues(itemsInitialLoadCount, 0, query);

        venues.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
        venues.setAdapter(venuesAdapter);
        venues.setOnScrollListener(endlessScrollListener);
    }

    //INFO: Called in "afterViews"
    private void adaptDishItems() {
        EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!dishesAdapter.loadingStillInAction && dishesAdapter.APICallStillReturnsElements) {
                    dishesAdapter.loadingStillInAction = true;

                    //INFO: WHILE SCROLLING LOAD
                    loadMoreDishes(itemsOnScrollLoadCount, totalItemsCount, searchIsPromoted, query,
                            CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);
                }
                return true;
            }
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) filters.show();// scrolling stopped
                else filters.hide();
            }
        };

        final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);
        dishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
        dishesAdapter.swipeContainer.setOnRefreshListener(() -> {
            refreshLastKnownLocation();
            dishesAdapter.clearDishes();

            //INFO: REFRESH INITIAL LOAD
            loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                    CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);

            endlessScrollListener.resetState();
            dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
        });

        //INFO: INITIAL LOAD
        loadMoreDishes(itemsInitialLoadCount, 0, searchIsPromoted, query,
                CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);

        dishes.setFriction(ViewConfiguration.getScrollFriction() * 20); // slow down the scroll
        dishes.setAdapter(dishesAdapter);
        dishes.setOnScrollListener(endlessScrollListener);
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
            searchView.setQueryHint(getString(R.string.search_venues_hint));
        } else {
            menu.findItem(R.id.action_venues).setVisible(true);
            menu.findItem(R.id.action_dishes).setVisible(false);
            searchView.setQueryHint(getString(R.string.search_dishes_hint));
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        if (BuildConfig.DEBUG) {
            Toast.makeText(this, "Loading..", Toast.LENGTH_SHORT).show();
        }
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
        Venue venue = venuesService.GetById(listItem.VenueId);

        venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.Location = null;
        venue.VenueBusinessHours = null;
        intent.putExtra("Venue", venue);
        intent.putExtra("SelectedMenuListId", listItem.BrochureId);

        startActivity(intent);
    }


    void loadMoreVenues(int maxResultsCount, int totalItemsOffset, String query){
        AsyncTaskListener<ArrayList<Venue>> listener = new AsyncTaskListener<ArrayList<Venue>>() {
            @Override
            public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPostExecute(ArrayList<Venue> result) {
                venuesAdapter.addItems(result);

                venuesAdapter.APICallStillReturnsElements = result.size() >= itemsOnScrollLoadCount;
                venuesAdapter.LoadingStillInAction = false;

                centralProgress.setVisibility(View.GONE);
            }
        };

        SearchVenuesBindingModel model =
            new SearchVenuesBindingModel(maxResultsCount, totalItemsOffset, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), query);

        new LoadVenuesAsyncTask(listener).execute(model);
    }

    void loadMoreDishes(int maxResultsCount, int totalItemsOffset, Boolean isPromoted, String query,
                        ArrayList<String> phaseFilterIds, ArrayList<String> regionFilterIds, ArrayList<String> spectrumFilterIds){
        AsyncTaskListener<ArrayList<MenuListItem>> listener = new AsyncTaskListener<ArrayList<MenuListItem>>() {
            @Override
            public void onPreExecute() {
                centralProgress.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPostExecute(ArrayList<MenuListItem> result) {
                dishesAdapter.addItems(result);

                dishesAdapter.APICallStillReturnsElements = result.size() >= itemsOnScrollLoadCount;;
                dishesAdapter.loadingStillInAction = false;

                centralProgress.setVisibility(View.GONE);
            }
        };

        SearchMenuListItemsBindingModel model = new SearchMenuListItemsBindingModel(
                maxResultsCount,
                totalItemsOffset,
                lastKnownLocation.getLatitude(),
                lastKnownLocation.getLongitude(),
                isPromoted,
                query,
                phaseFilterIds,
                regionFilterIds,
                spectrumFilterIds);

        new LoadMenuListItemsAsyncTask(listener).execute(model);
    }

    //    @ItemClick(resName = "venues_ibFilters")
    void openFilters() {
        if (DishesSearchModeActivated) {
            Intent intent = new Intent(VenuesActivity.this, DishesFiltersActivity_.class);
            intent.putExtra("CuisinePhaseFilterIds", CuisinePhaseFilterIds);
            intent.putExtra("CuisineRegionFilterIds", CuisineRegionFilterIds);
            intent.putExtra("CuisineSpectrumFilterIds", CuisineSpectrumFilterIds);
            startActivity(intent);
        } else
            startActivity(new Intent(VenuesActivity.this, VenuesFiltersActivity_.class));
    }

    public void NavigateVenuesSearch() {
        Intent intent = new Intent(VenuesActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", false);
        startActivity(intent);
    }

    public void NavigateDishesSearch() {
        Intent intent = new Intent(VenuesActivity.this, VenuesActivity_.class);
        intent.putExtra("DishesSearchModeActivated", true);
//        intent.putExtra("CuisinePhaseFilterIds", CuisinePhaseFilterIds);
//        intent.putExtra("CuisineRegionFilterIds", CuisineRegionFilterIds);
//        intent.putExtra("CuisineSpectrumFilterIds", CuisineRegionFilterIds);
        startActivity(intent);
    }

    void refreshLastKnownLocation() {
        mLocationResolver.resolveLocation(this, location -> {
            this.lastKnownLocation = location;
        });
    }

    private void configureActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

}