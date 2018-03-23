package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;

@EBean
public class VenuesAdapter
        extends BaseAdapter {

    public boolean LoadingStillInAction; // used to prevent searching while another async search hasn't been finished
    public boolean APICallStillReturnsElements = true;

    private ArrayList<Venue> venues;
    private LruCache<String, Bitmap> mMemoryCache;

    public SwipeRefreshLayout swipeContainer;

    @RootContext
    Context context;

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
        addBitmapToMemoryCache("default", BitmapFactory.decodeResource(context.getResources(), R.drawable.venue_default_thumbnail));
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView = null;
        if (convertView == null) {
            venueItemView = VenueItemView_.build(context);
        } else
            venueItemView = (VenueItemView) convertView;

        Venue venue = getItem(position);
        venueItemView.bind(venue, mMemoryCache);

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


    public void addItems(ArrayList<Venue> items){
        int lastItemPosition = this.venues.size();
        this.venues.addAll(lastItemPosition, items);
        this.notifyDataSetChanged();
    }

    public void clearVenues(){
        this.venues = new ArrayList<>();
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