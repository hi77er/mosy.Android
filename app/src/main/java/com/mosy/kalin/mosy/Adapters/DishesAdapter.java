package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Views.DishItemView;
import com.mosy.kalin.mosy.Views.DishItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class DishesAdapter
        extends BaseAdapter {

    public boolean loadingStillInAction; // used to prevent searching while another async search hasn't been finished
    public boolean APICallStillReturnsElements = true;

    private ArrayList<MenuListItem> menuListItems;
    private LruCache<String, Bitmap> mMemoryCache;

    @RootContext
    Context context;


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
        if (this.menuListItems == null) this.menuListItems = new ArrayList<>();

        // final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // final int cacheSize = maxMemory / 8; // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 10 * 1024 * 1024; // 10 MBs

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
        addBitmapToMemoryCache("default", BitmapFactory.decodeResource(context.getResources(), R.drawable.eat_paprika_100x100));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DishItemView view = null;
        if (convertView == null)
            view = DishItemView_.build(context);
        else
            view = (DishItemView) convertView;

        MenuListItem menuListItem = getItem(position);
        view.bind(menuListItem, mMemoryCache);

        return view;
    }

    @Override
    public int getCount() {
        if (this.menuListItems != null)
            return menuListItems.size();
        else return 0;
    }

    @Override
    public MenuListItem getItem(int position) {
        if (this.menuListItems != null && this.menuListItems.size() > 0)
            return menuListItems.get(position);
        else return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItems(ArrayList<MenuListItem> items){
        if (this.menuListItems != null){
            int lastItemPosition = this.menuListItems.size();
            this.menuListItems.addAll(lastItemPosition, items);
            this.notifyDataSetChanged();
        }
    }

    public void clearDishes(){
        this.menuListItems = new ArrayList<>();
    }

    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

}