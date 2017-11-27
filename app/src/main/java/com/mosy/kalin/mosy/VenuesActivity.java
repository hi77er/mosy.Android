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

import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Services.LocationResolver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;

import javax.xml.datatype.Duration;

@SuppressLint("Registered")
@EActivity(R.layout.activity_venues)
@OptionsMenu(R.menu.search_menu)
public class VenuesActivity
        extends AppCompatActivity
{
//    @Extra
//    SearchMode Mode;
    long timeStarted = 0;
    Location lastKnowLocation;

    @Extra
    boolean DishesSearchModeActivated;

    SearchView searchView;
    LocationResolver mLocationResolver;

    @SystemService
    SearchManager searchManager;

    @Bean
    VenuesAdapter adapter;

    @ViewById(R.id.search_toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView Venues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timeStarted = System.currentTimeMillis();
        boolean value = this.DishesSearchModeActivated;

        mLocationResolver = new LocationResolver(this);
        mLocationResolver.onStart();
        retrieveLocation();
    }

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String query = "searchall";
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction()))
            query = intent.getStringExtra(SearchManager.QUERY);
        performSearch(query);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.venues_lSwipeContainer);
        adapter.setSwipeRefreshLayout(swipeContainer);
        adapter.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveLocation();
                performSearch("searchall");
                swipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            }
        });
        Venues.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (BuildConfig.DEBUG) {
            System.out.println("MOSYLOGS : APP LOADED!" + " TOOK: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec;");
            Toast.makeText(this, "Loaded for: " + ((System.currentTimeMillis() - timeStarted) / 1000) + " sec", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
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
                adapter.setLocation(location);
            }
        });
    }

    private void performSearch(String query) {
        Boolean found = adapter.findVenues(query); //TODO: SET LATITUDE AND LONGITUDE TO DUCRRENT DEVICE PARAMS
        String toastMessage = found ? "Results for '"+query+"'" : "No matches found";
        Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
    }

}