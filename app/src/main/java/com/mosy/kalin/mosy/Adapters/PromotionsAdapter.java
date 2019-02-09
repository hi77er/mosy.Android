package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.Base.PromotionItemBase;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;
import com.mosy.kalin.mosy.DTOs.ItemPercentagePromotion;
import com.mosy.kalin.mosy.DTOs.PercentagePromotion;
import com.mosy.kalin.mosy.DTOs.WallVenue;
import com.mosy.kalin.mosy.ItemViews.Base.PromotionItemViewBase;
import com.mosy.kalin.mosy.ItemViews.Base.WallItemViewBase;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView;
import com.mosy.kalin.mosy.ItemViews.FiltersInfoWallItemView_;
import com.mosy.kalin.mosy.ItemViews.ItemPercentagePromotionItemView;
import com.mosy.kalin.mosy.ItemViews.PercentagePromotionItemView;
import com.mosy.kalin.mosy.ItemViews.VenueWallItemView;
import com.mosy.kalin.mosy.ItemViews.VenueWallItemView_;
import com.mosy.kalin.mosy.Models.Views.ItemModels.FiltersInfoItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.ItemPercentagePromotionItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.PercentagePromotionItem;
import com.mosy.kalin.mosy.Models.Views.ItemModels.VenueWallItem;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class PromotionsAdapter
        extends RecyclerViewAdapterBase<PromotionItemBase, PromotionItemViewBase> {

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
    protected PromotionItemViewBase onCreateItemView(ViewGroup parent, int viewType) {
        if (viewType == WallItemBase.ITEM_TYPE_VENUE_TILE) {
           // return VenueWallItemView_.build(activity);
        } else if (viewType == WallItemBase.ITEM_TYPE_FILTERS_INFO_HEADER) {
           // return FiltersInfoWallItemView_.build(activity);
        } else {
            throw new IllegalArgumentException("Adapter supports only WallVenue and FilterInfo based items.");
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<PromotionItemViewBase> viewHolder, int position) {
        int viewType = this.getItemViewType(position);
        if (viewType == PromotionItemBase.ITEM_TYPE_PERCENTAGE_PROMOTION) {
            PercentagePromotionItemView view = (PercentagePromotionItemView)viewHolder.getView();
            PercentagePromotionItem itemBase = (PercentagePromotionItem) this.items.get(position);
            PercentagePromotion percentagePromotion = itemBase.PercentagePromotion;
            view.bind(percentagePromotion);
        } else if (viewType == PromotionItemBase.ITEM_TYPE_ITEM_PERCENTAGE_PROMOTION) {
            ItemPercentagePromotionItemView view = (ItemPercentagePromotionItemView)viewHolder.getView();
            ItemPercentagePromotionItem itemBase = (ItemPercentagePromotionItem) this.items.get(position);
            ItemPercentagePromotion itemPercentagePromotion = itemBase.ItemPercentagePromotion;
            view.bind(itemPercentagePromotion);
        } else {
            throw new IllegalArgumentException("Adapter doesn't support this item type.");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return this.items.get(position).getType();
    }

    public PromotionItemBase getItemAt(int position) {
        return this.items.get(position);
    }

    private int getItemPosition(PromotionItemBase item) {
        return this.items.indexOf(item);
    }

    public PercentagePromotionItem getPercentagePromotionItemById(String id) {
        for (PromotionItemBase item : this.items) {
            if (item.getType() == PromotionItemBase.ITEM_TYPE_PERCENTAGE_PROMOTION) {
                PercentagePromotionItem casted = (PercentagePromotionItem) item;
                if (id.equals(casted.PercentagePromotion.Id))
                    return casted;
            }
        }
        return null;
    }

    public ItemPercentagePromotionItem getItemPercentagePromotionItemById(String id) {
        for (PromotionItemBase item : this.items) {
            if (item.getType() == PromotionItemBase.ITEM_TYPE_ITEM_PERCENTAGE_PROMOTION) {
                ItemPercentagePromotionItem casted = (ItemPercentagePromotionItem) item;
                if (id.equals(casted.ItemPercentagePromotion.Id))
                    return casted;
            }
        }
        return null;
    }

    public void addPercentagePromotionItem(PercentagePromotion percentagePromotion) {
        if (this.items != null){
            PercentagePromotionItem item = new PercentagePromotionItem();
            item.PercentagePromotion = percentagePromotion;
            this.items.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void addItemPercentagePromotionItem(ItemPercentagePromotion itemPercentagePromotion) {
        if (this.items != null){
            ItemPercentagePromotionItem item = new ItemPercentagePromotionItem();
            item.ItemPercentagePromotion = itemPercentagePromotion;
            this.items.add(item);
            this.notifyDataSetChanged();
        }
    }

    public void onItemChanged(PromotionItemBase item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}