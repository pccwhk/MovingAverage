/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ccwhk
 */
public class MACD extends DatePoint {

    public final int fastEMADayCount ;
    public final int slowEMADayCount ;
           
    public MACD(BigDecimal value, Date date, int fast, int slow) {
        super(value, date);
        this.fastEMADayCount = fast;
        this.slowEMADayCount = slow;
    }

    @Override
    public String toString() {
        return "MACD{ fast = " + fastEMADayCount + " , slow = " + slowEMADayCount +  " " + super.toString() + '}';
    }

}
