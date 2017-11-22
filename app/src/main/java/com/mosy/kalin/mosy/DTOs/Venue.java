package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

// Implements Serializable in order to be passed between activities.
public class Venue
        extends ResultBase
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

//    @SerializedName("BusinessHours")
    public VenueBusinessHours VenueBusinessHours;

    @SerializedName("OutdoorImageMeta")
    public VenueImage OutdoorImage;

    public BigDecimal Rating;

    @SerializedName("FBOEndorsements")
    public ArrayList<VenueBadgeEndorsement> Endorsements;

//    public boolean AllowUploadFBOPictures;
//    public boolean AllowUploadMenuPictures;
//    public boolean AllowAnonymousUpload;
//    public boolean AllowReservations;
//    public FBOStatusMessage LastFBOStatusMessage;
//
//    public List<FBOStatusMessage> StatusMessages;
//    public List<MenuList> Brochures;
//    public List<FBOTable> Tables;
//    public UnassignedBrochures As IEnumerable(Of MenuList)
//    public List<FBOVisit> Visits;
//    public List<FBORate> Rates;
//    public List<FBOContact> Contacts;
//    public List<FBOImageMeta> Images;
//    public TablesAndCount List<object>;

}
