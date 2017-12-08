package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;
import com.mosy.kalin.mosy.Services.VenuesService;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class VenuesAdapter
        extends BaseAdapter {

    ArrayList<Venue> venues;

    @RootContext
    Context context;

    private Location deviceLocation;
    public void setLocation(Location location) {
        this.deviceLocation = location;
        this.DeviceLastKnownLatitude = this.deviceLocation != null ? this.deviceLocation.getLatitude() : 0;
        this.DeviceLastKnownLongitude = this.deviceLocation != null ? this.deviceLocation.getLongitude() : 0;
    }
    public Location getLocation() { return this.deviceLocation; }

    private double DeviceLastKnownLatitude;
    public double getDeviceLastKnownLatitude() { return this.DeviceLastKnownLatitude; }
    private double DeviceLastKnownLongitude;
    public double getDeviceLastKnownLongitude() { return this.DeviceLastKnownLongitude; }

    public SwipeRefreshLayout swipeContainer;
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

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView = null;
        if (convertView == null) {
            venueItemView = VenueItemView_.build(context);
        }
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
        return venues.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean findVenues(String query){
        try {
            SearchVenuesBindingModel model = new SearchVenuesBindingModel(8, this.DeviceLastKnownLatitude, this.DeviceLastKnownLongitude, query);
            this.venues = new SearchVenuesAsyncTask().execute(model).get();

            if (this.venues != null && this.venues.get(0) != null) {
                VenuesService vService = new VenuesService();
                vService.LoadVenuesOutdoorImageThumbnails(venues);
                vService.sortVenuesByDistanceToDevice(venues);
                VenuesAdapter.super.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.venues != null && this.venues.size() > 0;
    }

}