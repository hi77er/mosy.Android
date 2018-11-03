package com.mosy.kalin.mosy.Helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    public static String ToString(Date date, SimpleDateFormat formatter){
        return formatter.format(date != null ? date : StringHelper.empty());
    }

}

