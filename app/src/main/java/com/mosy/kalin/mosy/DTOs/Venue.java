package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by User on 7/6/2016.
 */
public class Venue implements Serializable {

    @SerializedName("Id")
    public String Id;

    @SerializedName("Name")
    public String Name;

    @SerializedName("Class")
    public String Class;

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
