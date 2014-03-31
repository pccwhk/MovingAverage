/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.util;

import com.ccw.avg.model.Average;
import com.ccw.avg.model.MinuteData;
import com.ccw.avg.model.DatePoint;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Queue;

/**
 *
 * @author potapig
 */
public class Helper {

    private static final Calendar c = Calendar.getInstance();
    public final static DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public static long getNextMinute(Date date) {
        return getNextPeriodByMinute(date, 1);
    }

    public static long getNextPeriodBySecond(Date date, int timeInSecond) {
        c.clear();
        c.setTimeInMillis(date.getTime());
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.add(Calendar.SECOND, timeInSecond);
        return c.getTime().getTime();
    }

    public static Date getDate(String dateStr) throws ParseException{
        return DATE_TIME_FORMAT.parse(dateStr);
    }
    
    public static long getNextPeriodByMinute(Date date, int timeInMinute) {
        return getNextPeriodBySecond(date, timeInMinute * 60);
    }

    public static BigDecimal sum(Collection<BigDecimal> numbers) {
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal elm : numbers) {
            sum = sum.add(elm);
        }
        return sum;
    }

    public static Queue<BigDecimal> transform(Collection<? extends DatePoint> data) {
        Queue<BigDecimal> q = new ArrayDeque<BigDecimal>();
        for (DatePoint td : data) {
            q.add(td.getValue());
        }
        return q;
    }

    public static Queue<BigDecimal> transformToBigDecimal(Collection<MinuteData> data) {
        Queue<BigDecimal> q = new ArrayDeque<BigDecimal>();
        for (MinuteData d : data) {
            q.add(d.sum);
        }
        return q;
    }

    public static Average calculateAverage(Collection<BigDecimal> numbers) {
        BigDecimal sum = sum(numbers);
        BigDecimal count = new BigDecimal(String.valueOf(numbers.size()));
        return new Average(sum, sum.divide(new BigDecimal(String.valueOf(count)),
                Constants.DECIMAL_POINT, RoundingMode.HALF_UP), numbers.size());
    }

    /*
     public static BigDecimal calculateSMA(SMAUtil prevSMA,
     BigDecimal headSubPeriodSum, int headSubPeriodCount,
     Collection<BigDecimal> nextSubPeriodPrices) {
     BigDecimal nextSubPeriodSum = sum(nextSubPeriodPrices);
     BigDecimal SMA = prevSMA.sum.subtract(headSubPeriodSum).add(nextSubPeriodSum).divide(new BigDecimal(String.valueOf(headSubPeriodCount)));
     return SMA;
     }*/
}
