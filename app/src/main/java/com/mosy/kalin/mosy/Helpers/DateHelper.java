package com.mosy.kalin.mosy.Helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kkras on 9/28/2017.
 */

public class DateHelper {

    public static String ToString(Date date, SimpleDateFormat formatter){
        SimpleDateFormat df = (formatter == null ? formatter : new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
        return df.format(date != null ? date : StringHelper.empty());
    }

    public static String GetTime(Date date){
        return date.getHours() + ":" + date.getMinutes();
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
