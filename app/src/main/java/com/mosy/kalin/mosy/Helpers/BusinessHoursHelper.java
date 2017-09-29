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
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY)
            return businessHours.IsSundayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.SundayFrom, businessHours.SundayTo);
        else if (day == Calendar.MONDAY)
            return businessHours.IsMondayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.MondayFrom, businessHours.MondayTo);
        else if (day == Calendar.TUESDAY)
            return businessHours.IsTuesdayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.TuesdayFrom, businessHours.TuesdayTo);
        else if (day == Calendar.WEDNESDAY)
            return businessHours.IsWednesdayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.WednesdayFrom, businessHours.WednesdayTo);
        else if (day == Calendar.THURSDAY)
            return businessHours.IsThursdayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.ThursdayFrom, businessHours.ThursdayTo);
        else if (day == Calendar.FRIDAY)
            return businessHours.IsFridayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.FridayFrom, businessHours.FridayTo);
        else if (day == Calendar.SATURDAY)
            return businessHours.IsSaturdayDayOff ? "Closed" : constructTextWhenNoDayOff(businessHours.SaturdayFrom, businessHours.SaturdayTo);
        return StringHelper.empty();
    }

    private static String constructTextWhenNoDayOff(Date from, Date to) {
        if (from == null || to == null) return  StringHelper.empty();
        String outputText = StringHelper.empty();
        Calendar calendar = Calendar.getInstance();
        Date currentTime = calendar.getTime();

        // Equalize year month and day
        from.setYear(currentTime.getYear());
        from.setMonth(currentTime.getMonth());
        from.setDate(currentTime.getDate());
        to.setYear(currentTime.getYear());
        to.setMonth(currentTime.getMonth());
        to.setDate(currentTime.getDate());

        if (currentTime.after(from) && (currentTime.before(to))) {
            Date threeHoursFromOpening = DateHelper.addHours(from, 3);
            Date threeHoursBeforeClosing = DateHelper.addHours(from, 3);
            if (currentTime.before(threeHoursFromOpening))
                outputText = " - Opened since " + DateHelper.printDifference(currentTime, from);
            else if(currentTime.after(threeHoursBeforeClosing))
                outputText = " - Closes after " + DateHelper.printDifference(currentTime, to);
            else
                outputText = " - Opened";
        }
        else if (currentTime.before(from))
            outputText = " - Opens after " + DateHelper.printDifference(currentTime, from);
        else if (currentTime.after(to))
            outputText = " - Closed since " + DateHelper.printDifference(currentTime, from);

        return outputText;
    }

}
