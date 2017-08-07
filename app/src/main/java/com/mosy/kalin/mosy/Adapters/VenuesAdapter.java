package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class VenuesAdapter extends BaseAdapter {
    List<Venue> venues;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        venues = new ArrayList<Venue>();

        this.venues.add( new Venue("The Godfather", "Bar"));
        this.venues.add(new Venue("Happy", "Bar"));
        this.venues.add(new Venue("Mc Donalds", "Fast Food"));
        this.venues.add(new Venue("Raffy", "Restaurant & Bar"));
        this.venues.add(new Venue("Donut", "Restaurant"));
        this.venues.add(new Venue("Pizza Hut", "Pizzeria"));
        this.venues.add(new Venue("Lovin Hut", "Snack Bar"));
        this.venues.add(new Venue("Spaghetti Kitchen", "Restaurant"));
        this.venues.add(new Venue("Venue9", "Bar"));
        this.venues.add(new Venue("Venue10", "Bar"));
        this.venues.add(new Venue("Venue11", "Bar"));
        this.venues.add(new Venue("Happy", "Bar"));
        this.venues.add(new Venue("Mc Donalds", "Fast Food"));
        this.venues.add(new Venue("Raffy", "Restaurant & Bar"));
        this.venues.add(new Venue("Donut", "Restaurant"));
        this.venues.add(new Venue("Pizza Hut", "Pizzeria"));
        this.venues.add(new Venue("Lovin Hut", "Snack Bar"));
        this.venues.add(new Venue("Spaghetti Kitchen", "Restaurant"));
        this.venues.add(new Venue("Venue9", "Bar"));
        this.venues.add(new Venue("Venue10", "Bar"));
        this.venues.add(new Venue("Venue11", "Bar"));
        this.venues.add(new Venue("Happy", "Bar"));
        this.venues.add(new Venue("Mc Donalds", "Fast Food"));
        this.venues.add(new Venue("Raffy", "Restaurant & Bar"));
        this.venues.add(new Venue("Donut", "Restaurant"));
        this.venues.add(new Venue("Pizza Hut", "Pizzeria"));
        this.venues.add(new Venue("Lovin Hut", "Snack Bar"));
        this.venues.add(new Venue("Spaghetti Kitchen", "Restaurant"));
        this.venues.add(new Venue("Venue9", "Bar"));
        this.venues.add(new Venue("Venue10", "Bar"));
        this.venues.add(new Venue("Venue11", "Bar"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView;
        if (convertView == null) {
            venueItemView = VenueItemView_.build(context);
        } else {
            venueItemView = (VenueItemView) convertView;
        }
        venueItemView.bind(getItem(position));

        return venueItemView;
//        return null;
    }

    @Override
    public int getCount() {
        return venues.size();//ListView items count.
    }

    @Override
    public Venue getItem(int position) {
        return venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}