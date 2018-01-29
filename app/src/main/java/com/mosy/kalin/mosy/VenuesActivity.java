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
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Listeners.EndlessScrollListener;
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
    long timeStarted = 0;

    @Extra
    static boolean DishesSearchModeActivated;
    @Extra
    static ArrayList<String> CuisinePhaseFilterIds;
    @Extra
    static ArrayList<String> CuisineRegionFilterIds;
    @Extra
    static ArrayList<String> CuisineSpectrumFilterIds;

    Boolean searchIsPromoted = true; // Normally search only promoted dishes, but when using query then search among all dishes
    String query = "searchall";

    LocationResolver mLocationResolver;

    @SystemService
    SearchManager searchManager;

    @Bean
    VenuesService venuesService;
    @Bean
    VenuesAdapter venuesAdapter;
    @Bean
    DishesAdapter dishesAdapter;

    SearchView searchView;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView venues;
    @ViewById(resName = "venues_lvDishes")
    ListView dishes;

    FloatingActionButton filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStarted = System.currentTimeMillis();

        mLocationResolver = new LocationResolver(this);
        mLocationResolver.onStart();
        retrieveLocation();
    }

    @AfterViews
    void afterViews() {
        configureActionBar();

//        venuesService = new VenuesService();

        this.filters = findViewById(R.id.venues_ibFilters);
        this.filters.setOnClickListener(v -> openFilters());

        Intent intent = getIntent();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
            searchIsPromoted = null; // Normally search only promoted dishes, but when using query then search among all dishes
        }

        if (!DishesSearchModeActivated) {
            final SwipeRefreshLayout venuesSwipeContainer = findViewById(R.id.venues_lVenuesSwipeContainer);
            venuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
            venuesAdapter.swipeContainer.setOnRefreshListener(() -> {
                retrieveLocation();
                performVenueSearch("searchall");
                venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            performVenueSearch(query);
            venues.setAdapter(venuesAdapter);
            venues.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView

                    //loadNextDataFromApi(page);

                    return true; // ONLY if more data is actually being loaded; false otherwise.
                }
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0) filters.show();// scrolling stopped
                    else filters.hide();
                }
            });
        } else {
            EndlessScrollListener endlessScrollListener = new EndlessScrollListener() {
                @Override
                public boolean onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    boolean found = false;
                    if (dishesAdapter.hasMoreElements())
                        found = performDishesSearch(totalItemsCount, searchIsPromoted, query, CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);

                    return found; // ONLY if more data is actually being loaded; false otherwise.
                }
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == 0) filters.show();// scrolling stopped
                    else filters.hide();
                }
            };
            final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);
            dishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
            dishesAdapter.swipeContainer.setOnRefreshListener(() -> {
                retrieveLocation();
                dishesAdapter.clearDishes();
                performDishesSearch(0, true,"searchall", CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);
                endlessScrollListener.resetState();
                dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            });

            performDishesSearch(0, searchIsPromoted, query, CuisinePhaseFilterIds, CuisineRegionFilterIds, CuisineSpectrumFilterIds);
            dishes.setAdapter(dishesAdapter);
            dishes.setOnScrollListener(endlessScrollListener);
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
            searchView.setQueryHint(getString(R.string.search_venues_hint));
        } else {
            menu.findItem(R.id.action_venues).setVisible(true);
            menu.findItem(R.id.action_dishes).setVisible(false);
            searchView.setQueryHint(getString(R.string.search_dishes_hint));
        }
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        if (BuildConfig.DEBUG) {
            System.out.println("MOSYLOGS : APP LOADED!" + " TOOK: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec;");
            Toast.makeText(this, "Loaded for: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec", Toast.LENGTH_LONG).show();
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

    @ItemClick(resName = "venues_lvVenues")
    void openVenueMenu(Venue venue) {
        Intent intent = new Intent(VenuesActivity.this, VenueActivity_.class);
        venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.Location = null;
        venue.VenueBusinessHours = null;
        intent.putExtra("Venue", venue);
        startActivity(intent);
    }


    @ItemClick(resName = "venues_lvDishes")
    void openDishMenu(MenuListItem listItem) {
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

    void retrieveLocation() {
        mLocationResolver.resolveLocation(this, new LocationResolver.OnLocationResolved() {
            @Override
            public void onLocationResolved(Location location) {
                venuesAdapter.setLocation(location);
                dishesAdapter.setLocation(location);
            }
        });
    }

    private boolean performVenueSearch(String query) {
        boolean found = venuesAdapter.findVenues(query);
        return false;
    }

    private boolean performDishesSearch(int totalItemsOffset, Boolean isPromoted, String query, ArrayList<String> phaseFilterIds, ArrayList<String> regionFilterIds, ArrayList<String> spectrumFilterIds) {
        boolean found = dishesAdapter.findDishes(totalItemsOffset, isPromoted, query, phaseFilterIds, regionFilterIds, spectrumFilterIds);
        return found;
    }

    private void configureActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

}