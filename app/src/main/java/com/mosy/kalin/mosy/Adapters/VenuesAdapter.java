package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Services.VenuesService;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class VenuesAdapter
        extends BaseAdapter {

    public boolean LoadingStillInAction; // used to prevent searching while another async search hasn't been finished
    public boolean APICallStillReturnsElements = true;

    private ArrayList<Venue> venues;

    public SwipeRefreshLayout swipeContainer;

    @RootContext
    Context context;
    @Bean
    VenuesService venuesService;

    public void setSwipeRefreshLayout(SwipeRefreshLayout layout) {
        if (layout != null) {
            this.swipeContainer = layout;
            // Configure the refreshing colors
            this.swipeContainer.setColorScheme(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            this.swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    @AfterInject
    void initAdapter() {
        if (this.venues == null) this.venues = new ArrayList<>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView = null;
        if (convertView == null) {
            venueItemView = VenueItemView_.build(context);
        } else
            venueItemView = (VenueItemView) convertView;

        Venue venue = getItem(position);
        venueItemView.bind(venue);

        return venueItemView;
    }

    @Override
    public int getCount() {
        if (this.venues != null)
            return venues.size();
        else return 0;
    }

    @Override
    public Venue getItem(int position) {
        return venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public void clearVenues(){
        this.venues = new ArrayList<>();
    }

    public void addItems(ArrayList<Venue> items){
        int lastItemPosition = this.venues.size();
        this.venues.addAll(lastItemPosition, items);
        this.notifyDataSetChanged();
    }
}