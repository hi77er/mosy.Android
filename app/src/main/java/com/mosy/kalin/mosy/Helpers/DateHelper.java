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

}
