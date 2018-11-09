package com.visirx.patient.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aa on 20.1.16.
 */
public class DateFormat {


    public static String getDateStr(Date date) {
        String dateStr = null;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dateStr = sf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String GetFormattedTimeStr(String inputDateString) {
        String strTime = null;
        try {
            SimpleDateFormat sfutc = new SimpleDateFormat("HH:mm");
            Date dateConverted = null;
            if (inputDateString != null) {
                dateConverted = sfutc.parse(inputDateString);
            }
            SimpleDateFormat sfDefault = new SimpleDateFormat("hh:mm a");
            if (dateConverted != null) {
                strTime = sfDefault.format(dateConverted);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDateTimeStr(String inputDateString) {
        String strTime = null;
        try {
            SimpleDateFormat sfutc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date dateConverted = null;
            if (inputDateString != null) {
                dateConverted = sfutc.parse(inputDateString);
            }
            SimpleDateFormat sfDefault = new SimpleDateFormat("d MMM");
            if (dateConverted != null) {
                strTime = sfDefault.format(dateConverted);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDateTime(Date inputDate) {
        String strTime = null;
        try {
            SimpleDateFormat sfDefault = new SimpleDateFormat("d  MMM");
            if (inputDate != null) {
                strTime = sfDefault.format(inputDate);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDateStr(String inputDateString) {
        String strTime = null;
        try {
            SimpleDateFormat sfutc = new SimpleDateFormat("yyyy-MM-dd");
            Date dateConverted = null;
            if (inputDateString != null) {
                dateConverted = sfutc.parse(inputDateString);
            }
            SimpleDateFormat sfDefault = new SimpleDateFormat("d MMM");
            if (dateConverted != null) {
                strTime = sfDefault.format(dateConverted);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDate_YYYYMMDD(Date inputDate) {
        String strTime = null;
        try {
            SimpleDateFormat sfDefault = new SimpleDateFormat("yyyy-MM-dd");
            if (inputDate != null) {
                strTime = sfDefault.format(inputDate);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDate_YYYYMMDD(String strinputDate) {
        String strTime = null;
        try {
            SimpleDateFormat sfutc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date inputDate = sfutc.parse(strinputDate);
            SimpleDateFormat sfDefault = new SimpleDateFormat("yyyy-MM-dd");
            if (inputDate != null) {
                strTime = sfDefault.format(inputDate);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDateEMR(String inputDateString) {
        String strTime = null;
        try {
            SimpleDateFormat sfutc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date dateConverted = null;
            if (inputDateString != null) {
                dateConverted = sfutc.parse(inputDateString);
            }
            SimpleDateFormat sfDefault = new SimpleDateFormat("MMM d");
            if (dateConverted != null) {
                strTime = sfDefault.format(dateConverted);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }

    public static String GetFormattedDate_DDMMMDAY(Date inputDate) {
        String strTime = null;
        try {
            SimpleDateFormat sfDefault = new SimpleDateFormat("dd MMM, E");
            if (inputDate != null) {
                strTime = sfDefault.format(inputDate);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strTime;
    }
}
