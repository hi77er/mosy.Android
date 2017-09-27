package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Services.VenuesService;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class VenuesAdapter
        extends BaseAdapter {

    ArrayList<Venue> venues;

    @RootContext
    Context context;

    private Location deviceLocation;
    public void setLocation(Location location) {
        this.deviceLocation = location;
    }

    public SwipeRefreshLayout swipeContainer;
    public void setSwipeRefreshLayout(SwipeRefreshLayout layout) {
        if (layout != null) {
            this.swipeContainer = layout;
            // Configure the refreshing colors
            this.swipeContainer.setColorSchemeResources(
                    android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
    }

    @AfterInject
    void initAdapter() {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView = null;
        if (convertView == null)
            venueItemView = VenueItemView_.build(context);
        else
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
        return (Venue) venues.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean loadVenues() {
        try {
            this.venues = new GetVenuesAsyncTask(context).execute(new GetVenuesBindingModel()).get();
            if (this.venues.size() > 0){
                VenuesService vService = new VenuesService();
                vService.downloadVenuesOutdoorImageThumbnails(venues, context);
                vService.downloadVenuesBusinessHours(venues, context);
                vService.calculateVenuesDistances(venues, this.deviceLocation);
                vService.sortVenuesByDistanceToDevice(venues);
                VenuesAdapter.super.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.venues.size() > 0;
    }

    public boolean findVenues(String query){
        try {
            this.venues = new SearchVenuesAsyncTask(context).execute(new SearchVenuesBindingModel(query)).get();
            if (this.venues.size() > 0){
                VenuesService vService = new VenuesService();
                vService.downloadVenuesOutdoorImageThumbnails(venues, context);
                vService.calculateVenuesDistances(venues, this.deviceLocation);
                VenuesAdapter.super.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.venues.size() > 0;
    }


}