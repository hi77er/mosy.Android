package com.mosy.kalin.mosy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Services.LocationResolver;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_venues)
public class VenuesActivity
        extends AppCompatActivity
{
    private LocationResolver mLocationResolver;

    @ViewById(resName = "venues_lvVenues")
    ListView Venues;

    @Bean
    VenuesAdapter adapter;

    @AfterViews
    void bindAdapter() {
        Venues.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationResolver = new LocationResolver(this);
        retrieveLocation();

        setContentView(R.layout.activity_venues);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationResolver.onStart();
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

    @ItemClick
    void venues_lvVenuesItemClicked(Venue venue) {
        Intent intent = new Intent(VenuesActivity.this, VenueActivity_.class);
        venue.OutdoorImage = null; // Looks like it cannot serialize the image object. We either don't need this one in the Venue page.
        intent.putExtra("Venue", venue);
        startActivity(intent);
    }

    void retrieveLocation() {
        mLocationResolver.resolveLocation(this, new LocationResolver.OnLocationResolved() {
            @Override
            public void onLocationResolved(Location location) {
                if (adapter != null)
                    adapter.setLocation(location);

                String message = "Lat: " + location.getLatitude() + ", Lon: " + location.getLongitude();
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

}