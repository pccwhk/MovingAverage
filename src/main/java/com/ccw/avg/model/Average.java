/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccw.avg.model;

import java.math.BigDecimal;

/**
 *
 * @author ccwhk
 */
public class Average {

    public final BigDecimal sum;
    public final BigDecimal average;
    public final long count;

    public Average(BigDecimal sum, BigDecimal average, long count) {
        this.sum = sum;
        this.average = average;
        this.count = count;
    }
}
