package com.mosy.kalin.mosy.Helpers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {

    public static String ToString(Date date, SimpleDateFormat formatter){
        return formatter.format(date != null ? date : StringHelper.empty());
    }

}
