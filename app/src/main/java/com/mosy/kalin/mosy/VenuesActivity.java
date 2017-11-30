package com.mosy.kalin.mosy;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.DishesAdapter;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Services.LocationResolver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venues)
@OptionsMenu(R.menu.search_menu)
public class VenuesActivity
        extends AppCompatActivity
{
    long timeStarted = 0;

    @Extra
    static boolean DishesSearchModeActivated;

    SearchView searchView;
    LocationResolver mLocationResolver;

    @SystemService
    SearchManager searchManager;

    @Bean
    VenuesAdapter venuesAdapter;
    @Bean
    DishesAdapter dishesAdapter;

    @ViewById(R.id.search_toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView Venues;
    @ViewById(resName = "venues_lvDishes")
    ListView Dishes;

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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String query = "searchall";
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
            query = intent.getStringExtra(SearchManager.QUERY);

        if (!DishesSearchModeActivated) {
            final SwipeRefreshLayout venuesSwipeContainer = findViewById(R.id.venues_lVenuesSwipeContainer);
            venuesAdapter.setSwipeRefreshLayout(venuesSwipeContainer);
            venuesAdapter.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    retrieveLocation();
                    performFBOSearch("searchall");
                    venuesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
                }
            });

            performFBOSearch(query);
            Venues.setAdapter(venuesAdapter);
        }
        else {
            final SwipeRefreshLayout dishesSwipeContainer = findViewById(R.id.venues_lDishesSwipeContainer);
            dishesAdapter.setSwipeRefreshLayout(dishesSwipeContainer);
            dishesAdapter.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    retrieveLocation();
                    performDishesSearch("searchall");
                    dishesSwipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
                }
            });

            performDishesSearch(query);
            Dishes.setAdapter(dishesAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        if (BuildConfig.DEBUG) {
            System.out.println("MOSYLOGS : APP LOADED!" + " TOOK: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec;");
            Toast.makeText(this, "Loaded for: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec", Toast.LENGTH_LONG).show();
        }
        return true;
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
    void openMenu(Venue venue) {
        Intent intent = new Intent(VenuesActivity.this, VenueActivity_.class);
        venue.OutdoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.IndoorImage = null; // Don't need these one in the Venue page. If needed should implement Serializable or Parcelable
        venue.Location = null;
        venue.VenueBusinessHours = null;
        intent.putExtra("Venue", venue);
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

    private void performFBOSearch(String query) {
        Boolean found = venuesAdapter.findVenues(query);
//        String toastMessage = found ? "Results for '"+query+"'" : "No matches found";
//        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

    private void performDishesSearch(String query) {
        Boolean found = dishesAdapter.findDishes(query);
    }
}