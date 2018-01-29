package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.location.Location;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.MenuListItem;
import com.mosy.kalin.mosy.Services.MenuListItemsService;
import com.mosy.kalin.mosy.Views.DishItemView;
import com.mosy.kalin.mosy.Views.DishItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@EBean
public class DishesAdapter
        extends BaseAdapter {

    ArrayList<MenuListItem> menuListItems;

    @Bean
    MenuListItemsService menuListItemsService;


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
        DishItemView view = null;
        if (convertView == null) {
            view = DishItemView_.build(context);
        }
        else
            view = (DishItemView) convertView;

        MenuListItem menuListItem = getItem(position);
        view.bind(menuListItem);

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
        return menuListItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean findDishes(Boolean isPromoted, String query, ArrayList<String> phaseFilterIds, ArrayList<String> regionFilterIds, ArrayList<String> spectrumFilterIds){
        try {
            this.menuListItems = this.menuListItemsService.searchMenuListItems(
                    8,
                    this.DeviceLastKnownLatitude,
                    this.DeviceLastKnownLongitude,
                    isPromoted,
                    query,
                    phaseFilterIds,
                    regionFilterIds,
                    spectrumFilterIds);

            if (this.menuListItems != null && this.menuListItems.get(0) != null) {
                MenuListItemsService mService = new MenuListItemsService();
                mService.LoadMenuListItemImageThumbnails(menuListItems);
//                mService.sortMenuListItemsByDistanceToDevice(menuListItems);
                DishesAdapter.super.notifyDataSetChanged();
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                Function<MenuListItem, String> transform = MenuListItem::getName;
                List<String> result = this.menuListItems.stream().map(transform).collect(Collectors.toList());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String joined = String.join(", ", result);
                    //DebugHelper.breakHere();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this.menuListItems != null && this.menuListItems.size() > 0;
    }
}