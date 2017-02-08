package com.example.chengyu.mini_linkedin.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Chengyu on 8/24/2016.
 */
public class DateUtils {
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy", Locale.getDefault());

    public static String dateToString(Date date) {return sdf.format(date);}

    public static Date StringTodate(String stringDate) {
        try {
            return sdf.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date(0);
        }
    }

}
