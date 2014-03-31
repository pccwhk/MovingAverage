/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.model;

import static com.ccw.avg.model.MACD.DATE_TIME_FORMAT;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author ccwhk
 */
public class DatePoint {

    //used for format date string
    public final static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    protected BigDecimal value;
    protected Date date;
    protected String dateStr;

    public DatePoint(BigDecimal value, Date date) {
        this.value = value;
        this.date = date;
        this.dateStr = DATE_TIME_FORMAT.format(date);
    }

    public BigDecimal getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return dateStr;
    }

    @Override
    public String toString() {
        return "DatePoint{" + "value=" + value + ", date=" + getDateStr() + '}';
    }

}
