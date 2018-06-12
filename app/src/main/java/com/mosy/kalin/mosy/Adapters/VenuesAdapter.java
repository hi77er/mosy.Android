package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class VenuesAdapter
        extends RecyclerViewAdapterBase<Venue, VenueItemView> {

    public boolean APICallStillReturnsElements = true;
    public SwipeRefreshLayout swipeContainer;

    @RootContext
    Context context;

    @RootContext
    Activity activity;

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
    void afterInject() {
        if (this.items == null) this.items = new ArrayList<>();
        // final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // final int cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache.

    }

    @Override
    protected VenueItemView onCreateItemView(ViewGroup parent, int viewType) {
        return VenueItemView_.build(context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<VenueItemView> viewHolder, int position) {
        VenueItemView view = viewHolder.getView();
        Venue venue = items.get(position);

        view.bind(venue, position);
    }

    public void addItems(ArrayList<Venue> items){
        if (this.items != null) {
            int lastItemPosition = this.items.size();
            this.items.addAll(lastItemPosition, items);
            activity.runOnUiThread(this::notifyDataSetChanged);
        }
    }

    private int getItemPosition(Venue venue){
        for (Venue item : this.items) {
            if (item.Id.equals(venue.Id))
                return this.items.indexOf(item);
        }
        return -1; // doesn't exist
    }

    public void onItemChanged(Venue venue) {
        int position = this.getItemPosition(venue);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }
}