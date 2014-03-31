/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.model;

import java.util.Queue;

/**
 *
 * @author ccwhk
 */
public class MACDSeries {

    public final Queue<MACD> macdData;

    public final Queue<EMA> signalLine;
    public final int fast;
    public final int slow;
    public final int singal;

    public MACDSeries(Queue<MACD> macdData, Queue<EMA> signalLine, int fast, int slow, int singal) {
        this.macdData = macdData;
        this.signalLine = signalLine;
        this.fast = fast;
        this.slow = slow;
        this.singal = singal;
        
    }
    
}
