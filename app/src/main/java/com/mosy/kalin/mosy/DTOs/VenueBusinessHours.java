package com.mosy.kalin.mosy.DTOs;

import com.google.gson.annotations.SerializedName;
import com.mosy.kalin.mosy.DTOs.Results.ResultBase;

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
    public String MondayFrom;
    @SerializedName("TuesdayFrom")
    public String TuesdayFrom;
    @SerializedName("ThursdayFrom")
    public String ThursdayFrom;
    @SerializedName("WednesdayFrom")
    public String WednesdayFrom;
    @SerializedName("FridayFrom")
    public String FridayFrom;
    @SerializedName("SaturdayFrom")
    public String SaturdayFrom ;
    @SerializedName("SundayFrom")
    public String SundayFrom;

    @SerializedName("MondayTo")
    public String MondayTo;
    @SerializedName("TuesdayTo")
    public String TuesdayTo;
    @SerializedName("WednesdayTo")
    public String WednesdayTo;
    @SerializedName("ThursdayTo")
    public String ThursdayTo;
    @SerializedName("FridayTo")
    public String FridayTo;
    @SerializedName("SaturdayTo")
    public String SaturdayTo;
    @SerializedName("SundayTo")
    public String SundayTo;

    public VenueBusinessHours() {

    }

}
