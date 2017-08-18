package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by User on 7/6/2016.
 */
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

    public VenueImage OutdoorImage;

    public BigDecimal Rating;

//    public boolean AllowUploadFBOPictures;
//    public boolean AllowUploadMenuPictures;
//    public boolean AllowAnonymousUpload;
//    public boolean AllowReservations;
//    public FBOStatusMessage LastFBOStatusMessage;
//
//    public List<FBOStatusMessage> StatusMessages;
//    public List<Brochure> Brochures;
//    public List<FBOTable> Tables;
//    public UnassignedBrochures As IEnumerable(Of Brochure)
//    public List<FBOVisit> Visits;
//    public List<FBORate> Rates;
//    public List<FBOContact> Contacts;
//    public List<FBOImageMeta> Images;
//    public List<FBOEndorsement>  Endorsements;
//    public TablesAndCount List<object>;

}
