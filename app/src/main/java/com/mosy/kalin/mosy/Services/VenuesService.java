package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.location.Location;

import com.mosy.kalin.mosy.Adapters.VenuesAdapter;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenuesAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenuesBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kkras on 8/25/2017.
 */

public class VenuesService {


    public void downloadVenuesOutdoorImages(ArrayList<Venue> venues, Context context) {
        try {
            for (Venue venue: venues) {
                GetVenueOutdoorImageBindingModel outdoorImageModel = new GetVenueOutdoorImageBindingModel(venue.Id);
                VenueImage outdoorImage = new GetVenueOutdoorImageAsyncTask(context).execute(outdoorImageModel).get();

                if (outdoorImage.Bytes != null) venue.OutdoorImage = outdoorImage;
                else venue.OutdoorImage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void calculateVenuesDistances(ArrayList<Venue> venues, Location deviceLocation) {
        for (Venue venue: venues) {
            if (deviceLocation != null)
            {
                double deviceLatitude = deviceLocation.getLatitude();
                double deviceLongitude = deviceLocation.getLongitude();
                double distance = LocationHelper.calculateDistanceToLocation(
                        venue.Location.Latitude,
                        venue.Location.Longitude,
                        deviceLatitude,
                        deviceLongitude);
                venue.Location.DistanceToCurrentLocationMeters = distance;
            }
        }
    }

    public void sortVenuesByDistanceToDevice(ArrayList<Venue> venues) {
        Collections.sort(venues, new Comparator<Venue>() {
            @Override
            public int compare(Venue v1, Venue v2) {
                return Double.compare(v1.Location.DistanceToCurrentLocationMeters, v2.Location.DistanceToCurrentLocationMeters);
            }
        });
    }
}
