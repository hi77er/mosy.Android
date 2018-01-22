package com.mosy.kalin.mosy.Services;

import android.location.Location;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueByIdAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageMetaAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueByIdBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutionException;

@EBean
public class VenuesService {

    public VenuesService() {

    }

    public Venue GetById(String venueId) {
        Venue result = null;
        try {
            GetVenueByIdBindingModel model = new GetVenueByIdBindingModel(venueId);
            result = new GetVenueByIdAsyncTask().execute(model).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Venue> searchVenues(int maxResoultsCount, double deviceLastKnownLatitude, double deviceLastKnownLongitude, String query){
        ArrayList<Venue> result = null;
        try {
            SearchVenuesBindingModel model = new SearchVenuesBindingModel(maxResoultsCount, deviceLastKnownLatitude, deviceLastKnownLongitude, query);
            result = new SearchVenuesAsyncTask().execute(model).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public VenueBusinessHours downloadVenuesBusinessHours(String venueId) {
        VenueBusinessHours businessHours = null;
        try {
            GetVenueBusinessHoursBindingModel businessHoursModel = new GetVenueBusinessHoursBindingModel(venueId);
            businessHours = new GetVenueBusinessHoursAsyncTask().execute(businessHoursModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return businessHours;
    }

    public void downloadVenuesBusinessHours(ArrayList<Venue> venues) {
        try {
            for (Venue venue: venues) {
                GetVenueBusinessHoursBindingModel businessHoursModel = new GetVenueBusinessHoursBindingModel(venue.Id);
                venue.VenueBusinessHours = new GetVenueBusinessHoursAsyncTask().execute(businessHoursModel).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadVenuesOutdoorImageThumbnailsMeta(ArrayList<Venue> venues) {
        try {
            for (Venue venue: venues) {
                GetVenueOutdoorImageThumbnailBindingModel outdoorImageModel = new GetVenueOutdoorImageThumbnailBindingModel(venue.Id);

                venue.OutdoorImage = new GetVenueOutdoorImageThumbnailAsyncTask().execute(outdoorImageModel).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public VenueImage downloadVenueOutdoorImage(String venueId) {
        VenueImage image = null;
        try {
            GetVenueOutdoorImageBindingModel outdoorImageModel = new GetVenueOutdoorImageBindingModel(venueId);
            image = new GetVenueOutdoorImageAsyncTask().execute(outdoorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueImage downloadVenueIndoorImageThumbnails(Venue venue) {
        VenueImage image = null;
        try {
            GetVenueIndoorImageThumbnailBindingModel indorImageModel = new GetVenueIndoorImageThumbnailBindingModel(venue.Id);
            image = new GetVenueIndoorImageThumbnailAsyncTask().execute(indorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueImage downloadVenueIndoorImage(String venueId) {
        VenueImage image = null;
        try {
            GetVenueIndoorImageBindingModel indorImageModel = new GetVenueIndoorImageBindingModel(venueId);
            image = new GetVenueIndoorImageAsyncTask().execute(indorImageModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public VenueLocation downloadVenueLocation(String venueId) {
        VenueLocation location = null;
        try {
            GetVenueLocationBindingModel locationModel = new GetVenueLocationBindingModel(venueId);
            location = new GetVenueLocationAsyncTask().execute(locationModel).get();
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
        for (Venue venue: venues)
            if (venue.DistanceToCurrentDeviceLocation == 0) venue.DistanceToCurrentDeviceLocation = 999999999;

        Collections.sort(venues, new Comparator<Venue>() {
            @Override
            public int compare(Venue v1, Venue v2) {
                return Double.compare(v1.DistanceToCurrentDeviceLocation, v2.DistanceToCurrentDeviceLocation);
            }
        });
    }

    public void LoadVenuesOutdoorImageThumbnails(ArrayList<Venue> venues) {
        for (Venue venue: venues) {
            if (venue.OutdoorImage != null && venue.OutdoorImage.Id != null && venue.OutdoorImage.Id.length() > 0)
                venue.OutdoorImage.Bytes = new AzureBlobService().GetBlob(venue.OutdoorImage.Id, "userimages\\fboalbums\\100x100");
        }
    }

    public VenueImage downloadVenueIndoorImageMeta(String id) {
        GetVenueIndoorImageMetaBindingModel model = new GetVenueIndoorImageMetaBindingModel(id);
        VenueImage image = null;
        try {
            image = new GetVenueIndoorImageMetaAsyncTask().execute(model).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }


}