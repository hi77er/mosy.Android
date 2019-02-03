package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.OperatorTableAccountsVenueItemView;
import com.mosy.kalin.mosy.ItemViews.OperatorTableAccountsVenueItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.OperatorTableAccountsVenueItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class OperatorTableAccountsVenuesAdapter
        extends RecyclerViewAdapterBase<WallItemBase, WallItemViewBase> {

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
    protected WallItemViewBase onCreateItemView(ViewGroup parent, int viewType) {
        return OperatorTableAccountsVenueItemView_.build(activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<WallItemViewBase> viewHolder, int position) {
        OperatorTableAccountsVenueItemView view = (OperatorTableAccountsVenueItemView)viewHolder.getView();
        OperatorTableAccountsVenueItem wallItemBase = (OperatorTableAccountsVenueItem) this.items.get(position);
        Venue venue = wallItemBase.Venue;
        view.bind(venue);
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getType();
    }

    public WallItemBase getItemAt(int position) {
        return this.items.get(position);
    }

    private int getItemPosition(WallItemBase item) {
        return this.items.indexOf(item);
    }

    public OperatorTableAccountsVenueItem getItemByVenueId(String venueId) {
        for (WallItemBase item : this.items) {
            OperatorTableAccountsVenueItem casted = (OperatorTableAccountsVenueItem) item;
            if (venueId.equals(casted.Venue.Id))
                return casted;
        }
        return null;
    }

    public void addTableAccountsVenueItem(Venue venue) {
        if (this.items != null){
            OperatorTableAccountsVenueItem item = new OperatorTableAccountsVenueItem();
            item.Venue = venue;
            this.items.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void onItemChanged(WallItemBase item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}