package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

/**
 * Created by kkras on 8/18/2017.
 */

public class VenueLocation
        extends HttpResult {

    @SerializedName("id")
    public String Id;

    @SerializedName("longitude")
    public double Longitude;

    @SerializedName("latitude")
    public double Latitude;

    @SerializedName("horizontalAccuracyMeters")
    public double HorizontalAccuracyMeters;

    public double DistanceToCurrentLocationMeters;
}
