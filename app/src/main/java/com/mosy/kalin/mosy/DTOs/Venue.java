package com.mosy.kalin.mosy.DTOs;

import java.math.BigDecimal;

/**
 * Created by User on 7/6/2016.
 */
public class Venue {

    public String ID;
    public String getID(){ return ID; }
    public void setID(String id){ this.ID = id; }

    public String Name;
    public String getName(){ return Name; }
    public void setName(String name){ this.Name = name; }

    public BigDecimal Rating;
    public BigDecimal getRating(){ return Rating; }
    public void setID(BigDecimal rating){ this.Rating = rating; }

    public String VenueClass;
    public String getVenueClass(){ return VenueClass; }
    public void setClass(String venueClass){ this.VenueClass = venueClass; }

    public String TeamEmail;
    public String getTeamEmail(){ return TeamEmail; }
    public void setTeamEmail(String teamEmail){ this.ID = TeamEmail; }

    public boolean AllowUploadFBOPictures;
    public boolean getAllowUploadFBOPictures(){ return AllowUploadFBOPictures; }
    public void setAllowUploadFBOPictures(boolean allowUploadFBOPictures){ this.AllowUploadFBOPictures = allowUploadFBOPictures; }

    public boolean AllowUploadMenuPictures;
    public boolean getAllowUploadMenuPictures(){ return AllowUploadMenuPictures; }
    public void setAllowUploadMenuPictures(boolean allowUploadMenuPictures){ this.AllowUploadMenuPictures = allowUploadMenuPictures; }

    public boolean AllowAnonymousUpload;
    public boolean getAllowAnonymousUpload(){ return AllowAnonymousUpload; }
    public void setAllowAnonymousUpload(boolean allowAnonymousUpload){ this.AllowAnonymousUpload = allowAnonymousUpload; }

    public boolean AllowReservations;
    public boolean getAllowReservations(){ return AllowReservations; }
    public void setAllowReservations(boolean allowReservations){ this.AllowReservations = allowReservations; }

//    public FBOStatusMessage LastFBOStatusMessage;
//
//    public List<FBOStatusMessage> StatusMessages;
//
//    public List<Brochure> Brochures;
//
//    public List<FBOTable> Tables;
//
//    public UnassignedBrochures As IEnumerable(Of Brochure)
//
//    public List<FBOVisit> Visits;
//
//    public List<FBORate> Rates;
//
//    public List<FBOContact> Contacts;
//
//    public List<FBOImageMeta> Images;
//
//    public List<FBOEndorsement>  Endorsements;
//
//    public TablesAndCount List<object>;
}
