package com.mosy.kalin.mosy.Helpers;

import android.location.Location;

import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kkras on 8/23/2017.
 */

public class BusinessHoursHelper {

    public static String buildBusinessHoursText(VenueBusinessHours businessHours){
        String outputText = StringHelper.empty();
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Date currentDayVenueFrom = null;
        Date currentDayVenueTo = null;

        if (day == Calendar.SUNDAY) {
            currentDayVenueFrom = businessHours.SundayFrom;
            currentDayVenueTo = businessHours.SundayTo;
        } else if (day == Calendar.MONDAY) {
            currentDayVenueFrom = businessHours.MondayFrom;
            currentDayVenueTo = businessHours.MondayTo;
        } else if (day == Calendar.TUESDAY) {
            currentDayVenueFrom = businessHours.TuesdayFrom;
            currentDayVenueTo = businessHours.TuesdayTo;
        }else if (day == Calendar.WEDNESDAY) {
            currentDayVenueFrom = businessHours.WednesdayFrom;
            currentDayVenueTo = businessHours.WednesdayTo;
        } else if (day == Calendar.THURSDAY) {
            currentDayVenueFrom = businessHours.ThursdayFrom;
            currentDayVenueTo = businessHours.ThursdayTo;
        } else if (day == Calendar.FRIDAY) {
            currentDayVenueFrom = businessHours.FridayFrom;
            currentDayVenueTo = businessHours.FridayTo;
        } else if (day == Calendar.SATURDAY) {
            currentDayVenueFrom = businessHours.SaturdayFrom;
            currentDayVenueTo = businessHours.SaturdayTo;
        }

        return outputText;
    }

}
