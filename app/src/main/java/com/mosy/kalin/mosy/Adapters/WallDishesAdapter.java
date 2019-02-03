package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.DishWallItemView;
import com.mosy.kalin.mosy.ItemViews.DishWallItemView_;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.DishWallItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FiltersInfoItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class WallDishesAdapter
        extends RecyclerViewAdapterBase<WallItemBase, WallItemViewBase> {

    public boolean APICallStillReturnsElements = true;

    @RootContext
    Activity activity;

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
        if (this.items == null) this.items = new ArrayList<>();

        // final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // final int cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache.
    }

    @Override
    protected WallItemViewBase onCreateItemView(ViewGroup parent, int viewType) {
        if (viewType == WallItemBase.ITEM_TYPE_DISH_TILE) {
            return DishWallItemView_.build(activity);
        } else if (viewType == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
            return FiltersInfoWallItemView_.build(activity);
        } else {
            throw new IllegalArgumentException("Adapter supports only Dish and FilterInfo based items.");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<WallItemViewBase> viewHolder, int position) {
        int viewType = this.getItemViewType(position);
        if (viewType == WallItemBase.ITEM_TYPE_DISH_TILE) {
            DishWallItemView view = (DishWallItemView)viewHolder.getView();
            DishWallItem wallItemBase = (DishWallItem) this.items.get(position);
            WallMenuListItem wallMenuListItem = wallItemBase.WallMenuListItem;
            view.bind(wallMenuListItem);
        } else if (viewType == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
            FiltersInfoWallItemView view = (FiltersInfoWallItemView)viewHolder.getView();
            FiltersInfoItem wallItemBase = (FiltersInfoItem) this.items.get(position);
            view.bind(wallItemBase.MatchingFiltersInfo, wallItemBase.MismatchingFiltersInfo);
        } else {
            throw new IllegalArgumentException("Adapter supports only Dish and FilterInfo based items.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

//    public void addItems(ArrayList<WallItemBase> items){
//        if (this.items != null){
//            int lastItemPosition = this.items.size();
//            this.items.addAll(lastItemPosition, items);
//            this.notifyDataSetChanged();
//        }
//    }

    public WallItemBase getItemAt(int position) {
        return this.items.get(position);
    }

    private int getItemPosition(WallItemBase item) {
        return this.items.indexOf(item);
    }

    public DishWallItem getItemByMenuListItemId(String menuListItemId) {
        for (WallItemBase item : this.items) {
            if (item.getType() == WallItemBase.ITEM_TYPE_DISH_TILE) {
                DishWallItem casted = (DishWallItem)item;
                if (menuListItemId.equals(casted.WallMenuListItem.Id))
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
            if (item.getType() == WallItemBase.ITEM_TYPE_DISH_TILE) {
                DishWallItem casted = (DishWallItem) item;
                return (matchingFiltersIds.equals(casted.WallMenuListItem.MatchingFiltersIds) && mismatchingFiltersIds.equals(casted.WallMenuListItem.MismatchingFiltersIds));
            } else {
                throw new TypeNotPresentException("Currently working in Dishes context. This type was not expected", new Exception());
            }
        }
        return false;
    }

    public void addDishWallItem(WallMenuListItem wallMenuListItem) {
        if (this.items != null){
            DishWallItem dishWallItem = new DishWallItem();
            dishWallItem.WallMenuListItem = wallMenuListItem;
            this.items.add(dishWallItem);
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