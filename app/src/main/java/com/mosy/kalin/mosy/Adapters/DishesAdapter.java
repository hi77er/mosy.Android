package com.mosy.kalin.mosy.Adapters;

import android.content.AsyncTaskLoader;
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

    public boolean loadingStillInAction; // used to prevent searching while another async search hasn't been finished
    public boolean APICallStillReturnsElements = true;

    private ArrayList<MenuListItem> menuListItems;

    @RootContext
    Context context;

    @Bean
    MenuListItemsService menuListItemsService;


//    private Location deviceLocation;
//    public void setLocation(Location location) {
//        this.deviceLocation = location;
//        this.DeviceLastKnownLatitude = this.deviceLocation != null ? this.deviceLocation.getLatitude() : 0;
//        this.DeviceLastKnownLongitude = this.deviceLocation != null ? this.deviceLocation.getLongitude() : 0;
//    }
//    public Location getLocation() { return this.deviceLocation; }
//    private double DeviceLastKnownLatitude;
//    public double getDeviceLastKnownLatitude() { return this.DeviceLastKnownLatitude; }
//    private double DeviceLastKnownLongitude;
//    public double getDeviceLastKnownLongitude() { return this.DeviceLastKnownLongitude; }

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




    public void addItems(ArrayList<MenuListItem> items){
        int lastItemPosition = this.menuListItems.size();
        this.menuListItems.addAll(lastItemPosition, items);
        this.notifyDataSetChanged();
    }

    public void clearDishes(){
        this.menuListItems = new ArrayList<>();
    }

}