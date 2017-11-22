package com.mosy.kalin.mosy.Helpers;

import android.location.Location;

import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        Date currentTime = Calendar.getInstance().getTime();

        // Equalize year month and day
        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Date newFrom = DateHelper.fromToday(0, 0, 0, fromCal.get(Calendar.HOUR), fromCal.get(Calendar.MINUTE), fromCal.get(Calendar.SECOND));
        from.setTime(newFrom.getTime());

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        Date newTo = DateHelper.fromToday(0, 0, 0, toCal.get(Calendar.HOUR), toCal.get(Calendar.MINUTE), toCal.get(Calendar.SECOND));
        to.setTime(newTo.getTime());

        if (currentTime.after(from) && (currentTime.before(to))) {
            Date threeHoursFromOpening = DateHelper.addHours(from, 3);
            Date threeHoursBeforeClosing = DateHelper.addHours(from, 3);
            if (currentTime.before(threeHoursFromOpening))
                outputText = "Opened since " + DateHelper.printDifference(currentTime, from);
            else if(currentTime.after(threeHoursBeforeClosing))
                outputText = "Closes after " + DateHelper.printDifference(currentTime, to);
            else
                outputText = "Opened";
        }
        else if (currentTime.before(from))
            outputText = "Opens after " + DateHelper.printDifference(currentTime, from);
        else if (currentTime.after(to))
            outputText = "Closed since " + DateHelper.printDifference(currentTime, from);

        return outputText;
    }

}
