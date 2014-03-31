/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ccw.avg.util;

import com.ccw.avg.model.EMA;
import com.ccw.avg.model.MACD;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;

/**
 *
 * @author ccwhk
 */
public class MACDUtil {
    
    /*
    public EMA getEMAByDate(Queue<EMA> emas, Date date) {
        
    }*/
    
    //  short period EMA should have more records than  longer period EMA 
    public static Queue<MACD> calculateMACD( Queue<EMA> fastEMAs, Queue<EMA> slowEMAs, int fast, int slow) throws ParseException{
        Queue<MACD> result = new ArrayDeque<MACD>();
        Iterator<EMA> fastIter = fastEMAs.iterator();
        for (EMA slowEMA : slowEMAs){
            String longDate = slowEMA.getDateStr();
            EMA fastEMA = fastIter.next();
            String shortDate = fastEMA.getDateStr();
            while (!shortDate.equals(longDate)) {
                fastEMA = fastIter.next();
                shortDate = fastEMA.getDateStr();
            }
            // match date, 
            // MACD  = shorter EMA - longer EMA
            BigDecimal value = fastEMA.getValue().subtract(slowEMA.getValue());
            MACD macd = new MACD( value, Helper.getDate(shortDate), fast, slow);
            result.add(macd);
        }
        return result;
    }
}
