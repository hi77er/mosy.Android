package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WallVenue
        implements Serializable {

    @SerializedName("id")
    public String Id;

    @SerializedName("locationId")
    public String LocationId;

    @SerializedName("name")
    public String Name;

    @SerializedName("class")
    public String Class;

    @SerializedName("description")
    public String Description;

    @SerializedName("seenCount")
    public String SeenCount;

    @SerializedName("fboLocation")
    public VenueLocation Location;

    @SerializedName("businessHours")
    public VenueBusinessHours VenueBusinessHours;

    @SerializedName("outdoorImageMeta")
    public VenueImage OutdoorImage;

    @SerializedName("indoorImageMeta")
    public VenueImage IndoorImage;

    @SerializedName("workingStatus")
    public String WorkingStatus;

    @SerializedName("distanceToDevice")
    public double DistanceToCurrentDeviceLocation;

    @SerializedName("hasOrdersManagementSubscription")
    public boolean HasOrdersManagementSubscription;

    @SerializedName("fboContacts")
    public VenueContacts VenueContacts;


    @SerializedName("filters")
    public ArrayList<Filter> Filters;

    @SerializedName("matchingFiltersIds")
    public ArrayList<String> MatchingFiltersIds;

    @SerializedName("mismatchingFiltersIds")
    public ArrayList<String> MismatchingFiltersIds;

}
