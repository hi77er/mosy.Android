package com.mosy.kalin.mosy.Services;

import android.location.Location;

import com.mosy.kalin.mosy.Async.Tasks.GetRequestableFiltersAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueBusinessHoursAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageMetaAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueIndoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueLocationAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageAsyncTask;
import com.mosy.kalin.mosy.Async.Tasks.GetVenueOutdoorImageThumbnailAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.DTOs.VenueLocation;
import com.mosy.kalin.mosy.Helpers.LocationHelper;
import com.mosy.kalin.mosy.Models.BindingModels.GetRequestableFiltersBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueBusinessHoursBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageMetaBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueIndoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueLocationBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageBindingModel;
import com.mosy.kalin.mosy.Models.BindingModels.GetVenueOutdoorImageThumbnailBindingModel;
import com.mosy.kalin.mosy.Models.Responses.RequestableFiltersResponse;

import org.androidannotations.annotations.EBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

@EBean
public class FiltersService {

    public FiltersService() {

    }

    public RequestableFiltersResponse getRequestableFilters() {
        RequestableFiltersResponse result = null;
        try {
            GetRequestableFiltersBindingModel model = new GetRequestableFiltersBindingModel();
            result = new GetRequestableFiltersAsyncTask().execute(model).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}