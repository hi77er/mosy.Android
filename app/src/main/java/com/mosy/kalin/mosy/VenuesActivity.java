package com.mosy.kalin.mosy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_venues)
public class VenuesActivity extends AppCompatActivity {

    @ViewById(resName = "venues_lvVenues")
    ListView Venues;

    @Bean
    VenuesAdapter adapter;

    @AfterViews
    void bindAdapter() {
        Venues.setAdapter(adapter);
    }

    @ItemClick
    void venues_lvVenuesItemClicked(Venue venue) {
        Intent intent = new Intent(VenuesActivity.this, VenueActivity_.class);
        intent.putExtra("Venue", venue);
        startActivity(intent);
    }
}
