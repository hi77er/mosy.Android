package com.mosy.kalin.mosy.Helpers;

import com.mosy.kalin.mosy.DTOs.Enums.WorkingStatus;
import com.mosy.kalin.mosy.DTOs.VenueBusinessHours;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class BusinessHoursHelper {

    public static WorkingStatus getWorkingStatus(VenueBusinessHours businessHours){
        if (businessHours == null)
            return WorkingStatus.Unknown;

        Calendar calendar = Calendar.getInstance();
        TimeZone deviceTimeZone = calendar.getTimeZone();
        int deviceTimeGMTOffset = deviceTimeZone.getRawOffset();
        long deviceTimeGMTMinutesOffset = TimeUnit.MINUTES.convert(deviceTimeGMTOffset, TimeUnit.MILLISECONDS);

        calendar.add(Calendar.MINUTE, -(int)deviceTimeGMTMinutesOffset); // convert calendar to GMT TimeZone
        calendar.add(Calendar.MINUTE, -businessHours.TimeZoneMinutesOffset);  // convert calendar to venue TimeZone

        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == Calendar.SUNDAY)
            return businessHours.IsSundayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.SundayFrom, businessHours.SundayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.MONDAY)
            return businessHours.IsMondayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.MondayFrom, businessHours.MondayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.TUESDAY)
            return businessHours.IsTuesdayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.TuesdayFrom, businessHours.TuesdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.WEDNESDAY)
            return businessHours.IsWednesdayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.WednesdayFrom, businessHours.WednesdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.THURSDAY)
            return businessHours.IsThursdayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.ThursdayFrom, businessHours.ThursdayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.FRIDAY)
            return businessHours.IsFridayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.FridayFrom, businessHours.FridayTo, businessHours.TimeZoneMinutesOffset);
        else if (day == Calendar.SATURDAY)
            return businessHours.IsSaturdayDayOff ?
                    WorkingStatus.Closed :
                    calculateStatus(businessHours.SaturdayFrom, businessHours.SaturdayTo, businessHours.TimeZoneMinutesOffset);
        return WorkingStatus.Unknown;
    }

    private static WorkingStatus calculateStatus(Date from, Date to, int bhMinutesOffset) {
        WorkingStatus status = WorkingStatus.Unknown;
        if (from == null || to == null)
            return  status;

        Calendar calendar = Calendar.getInstance();
        TimeZone deviceTimeZone = calendar.getTimeZone();
        int deviceTimeGMTOffset = deviceTimeZone.getRawOffset();
        long deviceTimeGMTMinutesOffset = TimeUnit.MINUTES.convert(deviceTimeGMTOffset, TimeUnit.MILLISECONDS);
        /*DEBUGGING PURPOSE:*/ String deviceTimeText = calendar.getTime().toString();

        calendar.add(Calendar.MINUTE, -(int)deviceTimeGMTMinutesOffset); // convert calendar to GMT TimeZone
        /*DEBUGGING PURPOSE:*/ String gmtTimeText = calendar.getTime().toString();

        calendar.add(Calendar.MINUTE, -bhMinutesOffset); // convert calendar to Venue TimeZone
        Date venueTime = calendar.getTime();
        /*DEBUGGING PURPOSE:*/ String venueTimeText = calendar.getTime().toString();

        Calendar fromCal = Calendar.getInstance();
        fromCal.setTime(from);
        Date newFrom = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), fromCal.get(Calendar.HOUR_OF_DAY), fromCal.get(Calendar.MINUTE), fromCal.get(Calendar.SECOND)).getTime();
        from.setTime(newFrom.getTime());
        /*DEBUGGING PURPOSE:*/ String fromTimeText = from.toString();

        Calendar toCal = Calendar.getInstance();
        toCal.setTime(to);
        Date newTo = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), toCal.get(Calendar.HOUR_OF_DAY), toCal.get(Calendar.MINUTE), toCal.get(Calendar.SECOND)).getTime();
        to.setTime(newTo.getTime());
        /*DEBUGGING PURPOSE:*/ String toTimeText = to.toString();

        boolean closesAfterMidnight = from.after(to);
        boolean is247 = !closesAfterMidnight && !to.after(from);

        if (is247) status = WorkingStatus.Open247 ;
        else if (!closesAfterMidnight){
            if (venueTime.after(from) && (venueTime.before(to)))
                status = WorkingStatus.Open;
            else
                status = WorkingStatus.Closed;
        } else {
            if ((venueTime.after(from) && (venueTime.after(to)))
             || (venueTime.before(from) && (venueTime.before(to)))){
                status = WorkingStatus.Open;
            } else {
                status = WorkingStatus.Closed;
            }
        }
        return status;
    }
}