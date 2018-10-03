package com.mad.dms.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FmtHelper {
    private static SimpleDateFormat sqlDate;
    private static SimpleDateFormat orderDate;
    private static SimpleDateFormat shortDate;
    public final static String datePlaceHolder = "--/--/----";

    private static SimpleDateFormat getShortDateFmt() {
        if (shortDate == null) {
            shortDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            shortDate.setTimeZone(TimeZone.getDefault());
        }
        return shortDate;
    }

    private static SimpleDateFormat getOrderDateFmt() {
        if (orderDate == null) {
//            orderDate = new SimpleDateFormat("HH:mm aa, MMM dd, yyyy", Locale.getDefault());
            orderDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            orderDate.setTimeZone(TimeZone.getDefault());
        }
        return orderDate;
    }

    private static SimpleDateFormat getSQLDateFmt() {
        if (sqlDate == null) {
            sqlDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sqlDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return sqlDate;
    }

    public static Date parseShortDate(String date) {
        try {
            return getShortDateFmt().parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Date parseSQLDate(String date) {
        try {
            if (date != null) {
                return getSQLDateFmt().parse(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toSQLDate(Date date) {
        if (date != null) {
            return getSQLDateFmt().format(date);
        }
        return "NULL";
    }

    public static String formatOrderDate(Date date) {
        if (date != null) {
            return getOrderDateFmt().format(date);
        }
        return datePlaceHolder;
    }

    public static String formatShortDate(Date date) {
        if (date != null) {
            return getShortDateFmt().format(date);
        }
        return datePlaceHolder;
    }
}
