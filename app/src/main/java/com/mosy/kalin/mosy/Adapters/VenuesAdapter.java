package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;
//import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class VenuesAdapter extends BaseAdapter {
    ArrayList<Venue> venues;

    @RootContext
    Context context;

    @AfterInject
    void initAdapter() {
        try {
            this.venues = new GetVenuesAsyncTask(context).execute(new GetVenuesBindingModel()).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VenueItemView venueItemView = null;
        if (convertView == null)
            venueItemView = VenueItemView_.build(context);
        else
            venueItemView = (VenueItemView) convertView;

        Venue venue = getItem(position);
        venueItemView.bind(venue);

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
        return (Venue) venues.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}