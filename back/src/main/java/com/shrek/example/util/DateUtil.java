package com.shrek.example.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author: wus
 * @description: 时间处理类
 * @date: 2018-11-11
 */

public class DateUtil {


    public static String getYestoday() {
        Calendar cal   =   Calendar.getInstance();
        cal.add(Calendar.DATE,   -1);

        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
        return yesterday;
    }

    public static  String getToday() {
        Calendar   cal   =   Calendar.getInstance();

        String yesterday = new SimpleDateFormat( "yyyy-MM-dd").format(cal.getTime());
        return yesterday;
    }
}
