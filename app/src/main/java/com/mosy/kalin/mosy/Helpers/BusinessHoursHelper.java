package com.mosy.kalin.mosy.Helpers;

import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class BusinessHoursHelper {

    public static WorkingStatus getWorkingStatus(VenueBusinessHours businessHours){
        if (businessHours == null) return WorkingStatus.Unknown;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY)
            return businessHours.IsSundayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.SundayFrom, businessHours.SundayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.MONDAY)
            return businessHours.IsMondayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.MondayFrom, businessHours.MondayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.TUESDAY)
            return businessHours.IsTuesdayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.TuesdayFrom, businessHours.TuesdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.WEDNESDAY)
            return businessHours.IsWednesdayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.WednesdayFrom, businessHours.WednesdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.THURSDAY)
            return businessHours.IsThursdayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.ThursdayFrom, businessHours.ThursdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.FRIDAY)
            return businessHours.IsFridayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.FridayFrom, businessHours.FridayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.SATURDAY)
            return businessHours.IsSaturdayDayOff ? WorkingStatus.Closed : calculateStatus(businessHours.SaturdayFrom, businessHours.SaturdayTo, businessHours.TimeZoneMinutesOffset);
        return WorkingStatus.Unknown;
    }

    private static WorkingStatus calculateStatus(Date from, Date to, int bhMinutesOffset) {
        if (from == null || to == null) return  WorkingStatus.Unknown;

        Calendar nowCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        Date testBeforeZoning = nowCal.getTime();
        nowCal.add(Calendar.MINUTE, bhMinutesOffset);
        Date now = nowCal.getTime();

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

        if (is247) status = WorkingStatus.Open247   ;
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