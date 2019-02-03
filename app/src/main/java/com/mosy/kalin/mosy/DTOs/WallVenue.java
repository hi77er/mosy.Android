package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class WallVenue
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("LocationId")
    public String LocationId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Class")
    public String Class;

    @SerializedName("Description")
    public String Description;

    @SerializedName("SeenCount")
    public String SeenCount;

    @SerializedName("FBOLocation")
    public VenueLocation Location;

    @SerializedName("BusinessHours")
    public VenueBusinessHours VenueBusinessHours;

    @SerializedName("OutdoorImageMeta")
    public VenueImage OutdoorImage;

    @SerializedName("IndoorImageMeta")
    public VenueImage IndoorImage;

    @SerializedName("WorkingStatus")
    public String WorkingStatus;

    @SerializedName("DistanceToDevice")
    public double DistanceToCurrentDeviceLocation;

    @SerializedName("HasOrdersManagementSubscription")
    public boolean HasOrdersManagementSubscription;

    @SerializedName("FBOContacts")
    public VenueContacts VenueContacts;


    @SerializedName("Filters")
    public ArrayList<Filter> Filters;

    @SerializedName("MatchingFiltersIds")
    public ArrayList<String> MatchingFiltersIds;

    @SerializedName("MismatchingFiltersIds")
    public ArrayList<String> MismatchingFiltersIds;

}
