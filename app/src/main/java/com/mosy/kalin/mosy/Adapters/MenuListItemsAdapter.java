package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;

import com.mosy.kalin.mosy.DTOs.WallMenuListItem;
import com.mosy.kalin.mosy.R;
import com.mosy.kalin.mosy.ItemViews.MenuListItemDetailsView;
import com.mosy.kalin.mosy.ItemViews.MenuListItemDetailsView_;
import com.mosy.kalin.mosy.ItemViews.MenuListItemView;
import com.mosy.kalin.mosy.ItemViews.MenuListItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.HashMap;

@EBean
public class MenuListItemsAdapter
        implements ExpandableListAdapter {

    ArrayList<WallMenuListItem> wallMenuListItems;
    public void setWallMenuListItems(ArrayList<WallMenuListItem> wallMenuListItems){
        this.wallMenuListItems = wallMenuListItems;
        this.setDetails(this.wallMenuListItems);
    }
    private LruCache<String, Bitmap> mMemoryCache;


    HashMap<String, WallMenuListItem> Details;
    public void setDetails(ArrayList<WallMenuListItem> value){
        this.Details = new HashMap<>();
        for (WallMenuListItem req: value)
            this.Details.put(req.Name, req);
    }

    private boolean venueHasOrdersManagementSubscription;
    public void setVenueHasOrdersManagementSubscription(boolean value){
        this.venueHasOrdersManagementSubscription = value;
    }

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        if (this.wallMenuListItems == null) this.wallMenuListItems = new ArrayList<>();

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
    public Object getChild(int listPosition, int expandedListPosition) {
        Object child = this.wallMenuListItems.get(listPosition);
        return child;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return 1;
    }

    @Override
    public View getChildView(int listPosition, int expandedListPosition, boolean b, View convertView, ViewGroup viewGroup) {
        MenuListItemDetailsView menuListItemDetailsView = null;
        if (convertView == null)
            menuListItemDetailsView = MenuListItemDetailsView_.build(context);
        else
            menuListItemDetailsView = (MenuListItemDetailsView) convertView;

        WallMenuListItem wallMenuListItem = this.wallMenuListItems.get(listPosition);
        menuListItemDetailsView.bind(wallMenuListItem, mMemoryCache, venueHasOrdersManagementSubscription);

        return menuListItemDetailsView;
    }

    @Override
    public int getChildrenCount(int i) {
        if (this.wallMenuListItems.size() < 1)
            return 0;
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.wallMenuListItems.get(listPosition).Name;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getGroupCount() {
        return this.wallMenuListItems.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        MenuListItemView menuListItemView = null;
        if (convertView == null)
            menuListItemView = MenuListItemView_.build(context);
        else
            menuListItemView = (MenuListItemView) convertView;

        WallMenuListItem wallMenuListItem = this.wallMenuListItems.get(listPosition);

        menuListItemView.bind(wallMenuListItem);

        return menuListItemView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int i) {
        String assdasda = "";
    }
    @Override
    public void onGroupCollapsed(int i) { }
    @Override
    public long getCombinedChildId(long l, long l1) {
        return 0;
    }
    @Override
    public long getCombinedGroupId(long l) {
        return 0;
    }
    @Override
    public boolean hasStableIds() {
        return false;
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