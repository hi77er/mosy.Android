package com.mosy.kalin.mosy;

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
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Services.LocationResolver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_venues)
@OptionsMenu(R.menu.search_menu)
public class VenuesActivity
        extends AppCompatActivity
{
    private LocationResolver mLocationResolver;
    SearchView searchView;

    @Bean
    VenuesAdapter adapter;
    @SystemService
    SearchManager searchManager;
    @ViewById(R.id.search_toolbar)
    Toolbar toolbar;
    @ViewById(resName = "venues_lvVenues")
    ListView Venues;

    @AfterViews
    void bindAdapter() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout)findViewById(R.id.venues_lSwipeContainer);
        adapter.setSwipeRefreshLayout(swipeContainer);

        adapter.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadVenues();
                retrieveLocation();
                swipeContainer.setRefreshing(false); // Make sure you call swipeContainer.setRefreshing(false) once the network request has completed successfully.
            }
        });
        Venues.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            performSearch(query);
        }
        mLocationResolver = new LocationResolver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
//      searchView.setFocusable(true); searchView.setIconified(false); searchView.requestFocusFromTouch();
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationResolver.onStart();
        retrieveLocation();
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
        venue.Location = null;
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
        Toast.makeText(getApplicationContext(), "Searching for " + query + " .." , Toast.LENGTH_LONG).show();
    }

}