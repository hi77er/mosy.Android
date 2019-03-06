package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Http.HttpResults.HttpResult;

import java.util.Date;

/**
 * Created by kkras on 9/4/2017.
 */

public class VenueBusinessHours
        extends HttpResult {

    @SerializedName("id")
    public String Id;

    @SerializedName("isMondayDayOff")
    public boolean IsMondayDayOff;
    @SerializedName("isTuesdayDayOff")
    public boolean IsTuesdayDayOff;
    @SerializedName("isWednesdayDayOff")
    public boolean IsWednesdayDayOff;
    @SerializedName("isThursdayDayOff")
    public boolean IsThursdayDayOff;
    @SerializedName("isFridayDayOff")
    public boolean IsFridayDayOff;
    @SerializedName("isSaturdayDayOff")
    public boolean IsSaturdayDayOff;
    @SerializedName("iSundayDayOff")
    public boolean IsSundayDayOff;

    @SerializedName("timeZoneMinutesOffset")
    public int TimeZoneMinutesOffset;

    @SerializedName("mondayFrom")
    public Date MondayFrom;
    @SerializedName("tuesdayFrom")
    public Date TuesdayFrom;
    @SerializedName("thursdayFrom")
    public Date ThursdayFrom;
    @SerializedName("wednesdayFrom")
    public Date WednesdayFrom;
    @SerializedName("fridayFrom")
    public Date FridayFrom;
    @SerializedName("saturdayFrom")
    public Date SaturdayFrom ;
    @SerializedName("sundayFrom")
    public Date SundayFrom;

    @SerializedName("mondayTo")
    public Date MondayTo;
    @SerializedName("tuesdayTo")
    public Date TuesdayTo;
    @SerializedName("wednesdayTo")
    public Date WednesdayTo;
    @SerializedName("thursdayTo")
    public Date ThursdayTo;
    @SerializedName("fridayTo")
    public Date FridayTo;
    @SerializedName("saturdayTo")
    public Date SaturdayTo;
    @SerializedName("sundayTo")
    public Date SundayTo;



    public VenueBusinessHours() {

    }

}
