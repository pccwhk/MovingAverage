/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccw.avg.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 *
 * @author potapig
 */
public class Constants {
    public final static String STR_EMA = "EMA";
    public final static String STR_SMA = "SMA";
    public final static String STR_MACD = "MACD";
    
    public final static String STR_TRADE = "TRADE";
    public final static String STR_MIN = "MIN";
    
    public final static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public final static DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");
    
    public final static int DECIMAL_POINT = 6;
}
