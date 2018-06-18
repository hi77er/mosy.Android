package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DAL.Http.Results.ResultBase;

import java.util.Date;

/**
 * Created by kkras on 9/4/2017.
 */

public class VenueBusinessHours
        extends ResultBase {

    @SerializedName("Id")
    public String Id;

    @SerializedName("IsMondayDayOff")
    public boolean IsMondayDayOff;
    @SerializedName("IsTuesdayDayOff")
    public boolean IsTuesdayDayOff;
    @SerializedName("IsWednesdayDayOff")
    public boolean IsWednesdayDayOff;
    @SerializedName("IsThursdayDayOff")
    public boolean IsThursdayDayOff;
    @SerializedName("IsFridayDayOff")
    public boolean IsFridayDayOff;
    @SerializedName("IsSaturdayDayOff")
    public boolean IsSaturdayDayOff;
    @SerializedName("IsSundayDayOff")
    public boolean IsSundayDayOff;

    @SerializedName("MondayFrom")
    public Date MondayFrom;
    @SerializedName("TuesdayFrom")
    public Date TuesdayFrom;
    @SerializedName("ThursdayFrom")
    public Date ThursdayFrom;
    @SerializedName("WednesdayFrom")
    public Date WednesdayFrom;
    @SerializedName("FridayFrom")
    public Date FridayFrom;
    @SerializedName("SaturdayFrom")
    public Date SaturdayFrom ;
    @SerializedName("SundayFrom")
    public Date SundayFrom;

    @SerializedName("MondayTo")
    public Date MondayTo;
    @SerializedName("TuesdayTo")
    public Date TuesdayTo;
    @SerializedName("WednesdayTo")
    public Date WednesdayTo;
    @SerializedName("ThursdayTo")
    public Date ThursdayTo;
    @SerializedName("FridayTo")
    public Date FridayTo;
    @SerializedName("SaturdayTo")
    public Date SaturdayTo;
    @SerializedName("SundayTo")
    public Date SundayTo;

    public VenueBusinessHours() {

    }

}
