package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.location.Location;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageThumbnailBindingModel;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kkras on 8/25/2017.
 */

@EBean
public class VenuesService {

    public VenueBusinessHours downloadVenuesBusinessHours(String venueId, Context context) {
        VenueBusinessHours businessHours = null;
        try {
            GetVenueBusinessHoursBindingModel businessHoursModel = new GetVenueBusinessHoursBindingModel(venueId);
            businessHours = new GetVenueBusinessHoursAsyncTask(context).execute(businessHoursModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public void downloadVenuesBusinessHours(ArrayList<Venue> venues, Context context) {
        try {
            for (Venue venue: venues) {
                GetVenueBusinessHoursBindingModel businessHoursModel = new GetVenueBusinessHoursBindingModel(venue.Id);
                venue.BusinessHours = new GetVenueBusinessHoursAsyncTask(context).execute(businessHoursModel).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadVenuesOutdoorImageThumbnails(ArrayList<Venue> venues, Context context) {
        try {
            for (Venue venue: venues) {
                GetVenueOutdoorImageThumbnailBindingModel outdoorImageModel = new GetVenueOutdoorImageThumbnailBindingModel(venue.Id);
                VenueImage outdoorImage = new GetVenueOutdoorImageThumbnailAsyncTask(context).execute(outdoorImageModel).get();

                if (outdoorImage.Bytes != null) venue.OutdoorImage = outdoorImage;
                else venue.OutdoorImage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VenueImage downloadVenueOutdoorImage(String venueId, Context context) {
        VenueImage image = null;
        try {
            GetVenueOutdoorImageBindingModel outdoorImageModel = new GetVenueOutdoorImageBindingModel(venueId);
            image = new GetVenueOutdoorImageAsyncTask(context).execute(outdoorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueImage downloadVenueIndoorImageThumbnails(Venue venue, Context context) {
        VenueImage image = null;
        try {
            GetVenueIndoorImageThumbnailBindingModel indorImageModel = new GetVenueIndoorImageThumbnailBindingModel(venue.Id);
            image = new GetVenueIndoorImageThumbnailAsyncTask(context).execute(indorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueImage downloadVenueIndoorImage(String venueId, Context context) {
        VenueImage image = null;
        try {
            GetVenueIndoorImageBindingModel indorImageModel = new GetVenueIndoorImageBindingModel(venueId);
            image = new GetVenueIndoorImageAsyncTask(context).execute(indorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueLocation downloadVenueLocation(String venueId, Context context) {
        VenueLocation location = null;
        try {
            GetVenueLocationBindingModel locationModel = new GetVenueLocationBindingModel(venueId);
            location = new GetVenueLocationAsyncTask(context).execute(locationModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
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
