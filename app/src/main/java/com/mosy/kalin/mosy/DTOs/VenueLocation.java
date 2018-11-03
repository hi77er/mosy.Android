package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

/**
 * Created by kkras on 8/18/2017.
 */

public class VenueLocation
        extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Longitude")
    public double Longitude;

    @SerializedName("Latitude")
    public double Latitude;

    @SerializedName("HorizontalAccuracyMeters")
    public double HorizontalAccuracyMeters;

    public double DistanceToCurrentLocationMeters;
}
