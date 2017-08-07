package com.mosy.kalin.mosy;

import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.DTOs.Venue;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;


import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

@EActivity(R.layout.activity_venues)
public class VenuesActivity extends AppCompatActivity {

    @ViewById
    ListView venues_lvVenues;

    @Bean
    VenuesAdapter adapter;

    @AfterViews
    void bindAdapter() {
        venues_lvVenues.setAdapter(adapter);
    }

    @ItemClick
    void venues_lvVenuesItemClicked(Venue venue) {
        makeText(this, venue.getName() + " " + venue.getVenueClass(), LENGTH_SHORT).show();
    }
}
