package com.mosy.kalin.mosy.Services;

import android.content.Context;
import android.location.Location;

import com.mosy.kalin.mosy.Async.Tasks.GetVenueBadgeEndorsementsAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBadgeEndorsement;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBadgeEndorsementsBindingModel;
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
public class BadgeEndorsementsService {

    public ArrayList<VenueBadgeEndorsement> downloadVenueBadgeEndorsements(String venueId) {
        ArrayList<VenueBadgeEndorsement> endorsements = null;
        try {
            GetVenueBadgeEndorsementsBindingModel businessHoursModel = new GetVenueBadgeEndorsementsBindingModel(venueId);
            endorsements = new GetVenueBadgeEndorsementsAsyncTask().execute(businessHoursModel).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return endorsements;
    }

    public ArrayList<Venue> downloadVenuesBadgeEndorsements(ArrayList<Venue> venues) {
        for (Venue venue : venues) {
            try {
                GetVenueBadgeEndorsementsBindingModel businessHoursModel = new GetVenueBadgeEndorsementsBindingModel(venue.Id);
                venue.Endorsements = new GetVenueBadgeEndorsementsAsyncTask().execute(businessHoursModel).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return venues;
    }

}
