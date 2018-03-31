package com.box8.discountingEngine.util;

import java.util.Date;


public class HelperUtil {
    public static Boolean ifExistsBetweenDates(Date d , Date min , Date max){
        return d.after(min) && d.before(max);
    }
}
