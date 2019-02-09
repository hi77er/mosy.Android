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
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView_;
import com.mosy.kalin.mosy.ItemViews.VenueWallItemView;
import com.mosy.kalin.mosy.ItemViews.VenueWallItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FiltersInfoItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.VenueWallItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class WallVenuesAdapter
        extends RecyclerViewAdapterBase<WallItemBase, WallItemViewBase> {

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
    protected WallItemViewBase onCreateItemView(ViewGroup parent, int viewType) {
        if (viewType == WallItemBase.ITEM_TYPE_VENUE_TILE) {
            return VenueWallItemView_.build(activity);
        } else if (viewType == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
            return FiltersInfoWallItemView_.build(activity);
        } else {
            throw new IllegalArgumentException("Adapter supports only WallVenue and FilterInfo based items.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<WallItemViewBase> viewHolder, int position) {
        int viewType = this.getItemViewType(position);
        if (viewType == WallItemBase.ITEM_TYPE_VENUE_TILE) {
            VenueWallItemView view = (VenueWallItemView)viewHolder.getView();
            VenueWallItem wallItemBase = (VenueWallItem) this.items.get(position);
            WallVenue wallVenue = wallItemBase.WallVenue;
            view.bind(wallVenue);
        } else if (viewType == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
            FiltersInfoWallItemView view = (FiltersInfoWallItemView)viewHolder.getView();
            FiltersInfoItem wallItemBase = (FiltersInfoItem) this.items.get(position);
            view.bind(wallItemBase.MatchingFiltersInfo, wallItemBase.MismatchingFiltersInfo);
        } else {
            throw new IllegalArgumentException("Adapter supports only WallVenue and FilterInfo based items.");
        }
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

    public VenueWallItem getItemByVenueId(String venueId) {
        for (WallItemBase item : this.items) {
            if (item.getType() == WallItemBase.ITEM_TYPE_VENUE_TILE) {
                VenueWallItem casted = (VenueWallItem) item;
                if (venueId.equals(casted.WallVenue.Id))
                    return casted;
            }
        }
        return null;
    }

    public boolean filtersInfoExists(String matchingFiltersInfo, String mismatchingFiltersInfo) {
        for (WallItemBase item : this.items) {
            if (item.getType() == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
                FiltersInfoItem casted = (FiltersInfoItem)item;
                if (matchingFiltersInfo.equals(casted.MatchingFiltersInfo) && mismatchingFiltersInfo.equals(casted.MismatchingFiltersInfo))
                    return true;
            }
        }
        return false;
    }

    public boolean lastItemHasSameFiltersInfo(ArrayList<String> matchingFiltersIds, ArrayList<String> mismatchingFiltersIds) {
        if (this.items != null && this.items.size() > 0) {
            WallItemBase item = this.items.get(this.items.size() - 1);
            if (item.getType() == WallItemBase.ITEM_TYPE_VENUE_TILE) {
                VenueWallItem casted = (VenueWallItem) item;
                return (matchingFiltersIds.equals(casted.WallVenue.MatchingFiltersIds) && mismatchingFiltersIds.equals(casted.WallVenue.MismatchingFiltersIds));
            } else {
                throw new TypeNotPresentException("Currently working in WallVenue context. This type was not expected", new Exception());
            }
        }
        return false;
    }

    public void addVenueWallItem(WallVenue wallVenue) {
        if (this.items != null){
            VenueWallItem venueWallItem = new VenueWallItem();
            venueWallItem.WallVenue = wallVenue;
            this.items.add(venueWallItem);
            this.notifyDataSetChanged();
        }
    }

    public void addFiltersInfoHeader(String matchingFiltersInfo, String mismatchingFiltersInfo) {
        if (this.items != null){
            FiltersInfoItem filtersInfoItem = new FiltersInfoItem();
            filtersInfoItem.MatchingFiltersInfo = matchingFiltersInfo;
            filtersInfoItem.MismatchingFiltersInfo = mismatchingFiltersInfo;
            this.items.add(filtersInfoItem);
            this.notifyDataSetChanged();
        }
    }

    public void onItemChanged(WallItemBase item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}