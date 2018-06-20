package com.ppmall.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static DateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    public static Date getDate() {
        return new Date();
    }
    
    public static Date getDate(long milis) {
        return new Date(milis);
    }

    public static String getDateString(long dateTime) {
        return sdf.format(new Date(dateTime));
    }

    public static String getDateString(Date date) {
        return sdf.format(date);
    }

    public static String getDateString(Date date,String format) {
        sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date getDate(String dateString) throws ParseException {
        return sdf.parse(dateString);
    }
}
