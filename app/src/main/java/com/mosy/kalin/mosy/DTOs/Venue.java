package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;
import com.mosy.kalin.mosy.DTOs.Base.WallItemBase;

import java.io.Serializable;
import java.util.ArrayList;

public class Venue
        implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("LocationId")
    public String LocationId;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Class")
    public String Class;

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

    @SerializedName("FBOContacts")
    public VenueContacts VenueContacts;


    @SerializedName("Filters")
    public ArrayList<Filter> Filters;

    @SerializedName("MatchingFiltersIds")
    public ArrayList<String> MatchingFiltersIds;

    @SerializedName("MismatchingFiltersIds")
    public ArrayList<String> MismatchingFiltersIds;

}
