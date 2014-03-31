/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.model;

import static com.ccw.avg.model.MACD.DATE_TIME_FORMAT;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ccwhk
 */
public class EMA extends DatePoint {

    public final int dayCount;

    public EMA(int dayCount, BigDecimal value, Date date) {
        super(value, date);
        this.dayCount = dayCount;
    }

    @Override
    public String toString() {
        return "EMA{" + "dayCount=" + dayCount + " " + super.toString() + '}';
    }

}
