package com.mosy.kalin.mosy.Adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;

import com.mosy.kalin.mosy.Adapters.Base.RecyclerViewAdapterBase;
import com.mosy.kalin.mosy.Adapters.Base.ViewWrapper;
import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Views.DishItemView;
import com.mosy.kalin.mosy.Views.DishItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class DishesAdapter
        extends RecyclerViewAdapterBase<MenuListItem, DishItemView> {

    public boolean loadingStillInAction; // used to prevent searching while another async search hasn't been finished
    public boolean APICallStillReturnsElements = true;

    private boolean IsUsingDefaultThumbnail = true;
    private LruCache<String, Bitmap> mMemoryCache;

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
    protected DishItemView onCreateItemView(ViewGroup parent, int viewType) {
        return DishItemView_.build(activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewWrapper<DishItemView> viewHolder, int position) {
        DishItemView view = viewHolder.getView();
        MenuListItem menuListItem = this.items.get(position);

        view.bind(menuListItem, position, this.IsUsingDefaultThumbnail);
    }

    public void addItems(ArrayList<MenuListItem> items){
        if (this.items != null){
            int lastItemPosition = this.items.size();
            this.items.addAll(lastItemPosition, items);
            this.notifyDataSetChanged();
        }
    }

    public MenuListItem getItemAt(int position){
        return this.items.get(position);
    }

    private int getItemPosition(MenuListItem item){
        for (MenuListItem menuListItem : this.items) {
            if (item.Id.equals(menuListItem.Id))
                return this.items.indexOf(item);
        }
        return -1; // doesn't exist
    }

    public void onItemChanged(MenuListItem item) {
        int position = this.getItemPosition(item);
        activity.runOnUiThread(() -> this.notifyItemChanged(position));
    }

}