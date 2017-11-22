package com.mosy.kalin.mosy.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

    public static Date fromDate(Calendar dateCalendar, int year, int month, int day_of_month, int hour, int minute, int second){
        return new GregorianCalendar(
                year != 0 ? year : dateCalendar.get(Calendar.YEAR),
                month != 0 ? month : dateCalendar.get(Calendar.MONTH),
                day_of_month != 0 ? day_of_month : dateCalendar.get(Calendar.DAY_OF_MONTH),
                hour != 0 ? hour : dateCalendar.get(Calendar.HOUR),
                minute != 0 ? minute : dateCalendar.get(Calendar.MINUTE),
                second != 0 ? second : dateCalendar.get(Calendar.SECOND)
        ).getTime();
    }

    public static Date fromToday(int year, int month, int day_of_month, int hour, int minute, int second){
        Calendar todaysCalendar = Calendar.getInstance();
        return new GregorianCalendar(
                year != 0 ? year : todaysCalendar.get(Calendar.YEAR),
                month != 0 ? month : todaysCalendar.get(Calendar.MONTH),
                day_of_month != 0 ? day_of_month : todaysCalendar.get(Calendar.DAY_OF_MONTH),
                hour != 0 ? hour : todaysCalendar.get(Calendar.HOUR),
                minute != 0 ? minute : todaysCalendar.get(Calendar.MINUTE),
                second != 0 ? second : todaysCalendar.get(Calendar.SECOND)
        ).getTime();
    }

    public static String ToString(Date date, SimpleDateFormat formatter){
        return formatter.format(date != null ? date : StringHelper.empty());
    }

    public static String GetTime(Date date){
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
    }

    public static String printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
//        long daysInMilli = hoursInMilli * 24;

//        long elapsedDays = different / daysInMilli;
//        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        //long elapsedSeconds = different / secondsInMilli;

        // elapsedDays, elapsedSeconds,
        if (Math.abs(elapsedHours) < 1)
            return Math.abs(elapsedMinutes) + " minutes";
        if (Math.abs(elapsedHours) == 1)
            return "1 hour";
        else
            return Math.abs(elapsedHours) + " hours";
    }

    public static Date addHours(Date old, int count)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(old);
        cal.add(Calendar.HOUR_OF_DAY, count);
        return cal.getTime();
    }

    public static Date addMinutes(Date old, int count)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(old);
        cal.add(Calendar.MINUTE, count);
        return cal.getTime();
    }
}
