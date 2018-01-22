package com.mosy.kalin.mosy.Helpers;

import android.location.Location;

import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
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
        Date now = Calendar.getInstance().getTime();
        Calendar nowCal = Calendar.getInstance();

        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Date newFrom = new GregorianCalendar(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), fromCal.get(Calendar.HOUR_OF_DAY), fromCal.get(Calendar.MINUTE), fromCal.get(Calendar.SECOND)).getTime();
        from.setTime(newFrom.getTime());

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        Date newTo = new GregorianCalendar(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), toCal.get(Calendar.HOUR_OF_DAY), toCal.get(Calendar.MINUTE), toCal.get(Calendar.SECOND)).getTime();
        to.setTime(newTo.getTime());

        String outputText = StringHelper.empty();

        boolean closesAfterMidnight = from.after(to);
        boolean is247 = !closesAfterMidnight && !to.after(from);

        if (is247)
            outputText = "Opened 24/7";
        else if (!closesAfterMidnight){
            if (now.after(from) && (now.before(to)))
                outputText = "Opened";
            else if (now.before(from))
                outputText = "Opens after " + DateHelper.printDifference(now, from);
            else if (now.after(to))
                outputText = "Closed";
        } else {
            if ((now.after(from) && (now.after(to)))
             || (now.before(from) && (now.before(to)))){
                outputText = "Opened";
            } else {
                outputText = "Closed";
            }
        }

        return outputText;
    }

    public static WorkingStatus getWorkingStatus(VenueBusinessHours businessHours){
        if (businessHours == null) return WorkingStatus.Unknown;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY)
            return businessHours.IsSundayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.SundayFrom, businessHours.SundayTo);
        else if (day == Calendar.MONDAY)
            return businessHours.IsMondayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.MondayFrom, businessHours.MondayTo);
        else if (day == Calendar.TUESDAY)
            return businessHours.IsTuesdayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.TuesdayFrom, businessHours.TuesdayTo);
        else if (day == Calendar.WEDNESDAY)
            return businessHours.IsWednesdayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.WednesdayFrom, businessHours.WednesdayTo);
        else if (day == Calendar.THURSDAY)
            return businessHours.IsThursdayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.ThursdayFrom, businessHours.ThursdayTo);
        else if (day == Calendar.FRIDAY)
            return businessHours.IsFridayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.FridayFrom, businessHours.FridayTo);
        else if (day == Calendar.SATURDAY)
            return businessHours.IsSaturdayDayOff ? WorkingStatus.Closed : workingStatusWhenNoDayOff(businessHours.SaturdayFrom, businessHours.SaturdayTo);
        return WorkingStatus.Unknown;
    }

    private static WorkingStatus workingStatusWhenNoDayOff(Date from, Date to) {
        if (from == null || to == null) return  WorkingStatus.Unknown;
        Date now = Calendar.getInstance().getTime();
        Calendar nowCal = Calendar.getInstance();

        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Date newFrom = new GregorianCalendar(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), fromCal.get(Calendar.HOUR_OF_DAY), fromCal.get(Calendar.MINUTE), fromCal.get(Calendar.SECOND)).getTime();
        from.setTime(newFrom.getTime());

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        Date newTo = new GregorianCalendar(nowCal.get(Calendar.YEAR), nowCal.get(Calendar.MONTH), nowCal.get(Calendar.DAY_OF_MONTH), toCal.get(Calendar.HOUR_OF_DAY), toCal.get(Calendar.MINUTE), toCal.get(Calendar.SECOND)).getTime();
        to.setTime(newTo.getTime());

        WorkingStatus status = WorkingStatus.Unknown;

        boolean closesAfterMidnight = from.after(to);
        boolean is247 = !closesAfterMidnight && !to.after(from);

        if (is247) status = WorkingStatus.Open247;
        else if (!closesAfterMidnight){
            if (now.after(from) && (now.before(to)))
                status = WorkingStatus.Open;
            else if (now.after(to))
                status = WorkingStatus.Closed;
        } else {
            if ((now.after(from) && (now.after(to)))
                    || (now.before(from) && (now.before(to)))){
                status = WorkingStatus.Open;
            } else {
                status = WorkingStatus.Closed;
            }
        }
        return status;
    }
}
