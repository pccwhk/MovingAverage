/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.util;

import com.ccw.avg.model.EMA;
import com.ccw.avg.model.DatePoint;
import com.ccw.avg.model.TickData;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author ccwhk
 */
public class EMAUtil {

    // a cache for K
    private static final Map<Integer, BigDecimal> cache = new ConcurrentHashMap<Integer, BigDecimal>();

    // closig price is defined as the last available price of the day
    // from http://www.forextradingzone.org/fx-EMA
    // http://www.iexplain.org/ema-how-to-calculate/
    // EMA is defined as follows
    // EMA = Price(t) * k + EMA(y) * (1 – k)
    // where  k = 2/(N+1), N is the number of days
    public static BigDecimal calculateEMAValue(BigDecimal todayClosingPrice, int numberOfDays, BigDecimal EMAYesterday) {
        BigDecimal k = cache.get(numberOfDays);
        if (k == null) {
            k = (new BigDecimal("2")).divide(new BigDecimal(numberOfDays + 1),
                    Constants.DECIMAL_POINT, RoundingMode.HALF_UP);
            cache.put(numberOfDays, k);
        }

        return ((todayClosingPrice.multiply(k)).add(EMAYesterday.multiply((BigDecimal.ONE.subtract(k)))))
                .setScale(Constants.DECIMAL_POINT, RoundingMode.HALF_UP);

    }

    public static Queue<EMA> calculateEMASeries(Queue<? extends DatePoint> dataQueue, int numberOfDays) {
        // get the first n day dtae for 
        Deque<DatePoint> firstDataGroup = new ArrayDeque<DatePoint>();
        Queue<DatePoint> localDataQueue = new ArrayDeque<DatePoint>(dataQueue);
        for (int i = 0; i < numberOfDays; i++) {
            DatePoint td = localDataQueue.poll();
            if (td != null) {
                firstDataGroup.add(td);
            }
        }

        //calcte the first EMA using SMA
        BigDecimal prevEMAValue = Helper.calculateAverage(Helper.transform(firstDataGroup)).average;
        //System.out.println(prevEMA.toString());
        Queue<EMA> q = new ArrayDeque<EMA>();
        EMA firstEmaData = new EMA(numberOfDays, prevEMAValue, firstDataGroup.getLast().getDate());
        q.add(firstEmaData);

        for (DatePoint td : localDataQueue) {
            BigDecimal ema = EMAUtil.calculateEMAValue(td.getValue(), numberOfDays, prevEMAValue);
            EMA emaData = new EMA(numberOfDays, ema, td.getDate());
            q.add(emaData);
            prevEMAValue = ema;
        }

        // cater for boundary case
        return q;
    }
}
