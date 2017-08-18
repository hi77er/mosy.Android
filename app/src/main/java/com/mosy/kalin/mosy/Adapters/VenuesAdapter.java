package com.mosy.kalin.mosy.Adapters;

import android.content.Context;
import android.location.Location;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Services.LocationResolver;
import com.mosy.kalin.mosy.Views.VenueItemView;
import com.mosy.kalin.mosy.Views.VenueItemView_;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

/**
 * Created by kkras on 7/31/2017.
 */

@EBean
public class VenuesAdapter extends BaseAdapter {
    ArrayList<Venue> venues;

    @RootContext
    Context context;

    private Location deviceLocation;
    public void setLocation(Location location) {
        this.deviceLocation = location;
        loadVenues();
    }

    @AfterInject
    void initAdapter() {
        loadVenues();
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

    private void loadVenues() {
        try {
            this.venues = new GetVenuesAsyncTask(context).execute(new GetVenuesBindingModel()).get();
            for (Venue venue: this.venues) {
                GetVenueOutdoorImageBindingModel outdoorImageModel = new GetVenueOutdoorImageBindingModel(venue.Id);
                VenueImage outdoorImage = new GetVenueOutdoorImageAsyncTask(context).execute(outdoorImageModel).get();
                venue.OutdoorImage = outdoorImage;

                if (this.deviceLocation != null)
                    venue.Location.DistanceToCurrentLocationMeters = calculateDistanceToLocation(
                            venue.Location.Latitude,
                            venue.Location.Longitude,
                            this.deviceLocation.getLatitude(),
                            this.deviceLocation.getLongitude());

            }

            Collections.sort(this.venues, new Comparator<Venue>() {
                @Override
                public int compare(Venue v1, Venue v2) {
                    return Double.compare(v1.Location.DistanceToCurrentLocationMeters, v2.Location.DistanceToCurrentLocationMeters);
                }
            });

//            this.venues.sort(Comparator.comparing(a -> a.Location.DistanceToCurrentLocationMeters));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateDistanceToLocation(double fromLongitude, double fromLatitude, double toLongitude, double toLatitude) {
        float[] distance = new float[1];
        Location.distanceBetween(fromLatitude, fromLongitude, toLatitude, toLongitude, distance); // in Meters
        return distance[0];
    }
}