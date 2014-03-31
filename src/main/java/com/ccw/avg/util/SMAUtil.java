/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.util;

import com.ccw.avg.model.DatePoint;
import com.ccw.avg.model.MinuteData;
import com.ccw.avg.model.TickData;
import static com.ccw.avg.util.Constants.DECIMAL_POINT;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.Queue;

/**
 *
 * @author ccwhk
 */
public class SMAUtil {

    /**
     * This version of calculaion of SMA currently one works for one minute
     * Interval
     *
     * @param priceDataQueue
     * @param timeInMinute
     * @return
     */
    public static Queue<BigDecimal> calculateSMAByTime(Queue<? extends TickData> priceDataQueue, int timeInMinute) {
        long periodBorder = -1;
        long minuteBorder = -1;
        long periodTickCount = 0;
        long minuteTickCount = 0;
        BigDecimal periodSum = null;
        BigDecimal minuteSum = BigDecimal.ZERO;
        BigDecimal sma = BigDecimal.ZERO;
        Deque<MinuteData> minuteSumQueue = new ArrayDeque<MinuteData>();
        Queue<BigDecimal> result = new ArrayDeque<BigDecimal>();
        TickData td = null;
        String currentDateStr = "";
        for (TickData tickData : priceDataQueue) {
            if (tickData != null) {
                td = tickData;
                if (periodBorder == -1) {
                    // set up the period border for calcaltion of SMA 
                    periodBorder = Helper.getNextPeriodByMinute(td.getDate(), timeInMinute);
                    currentDateStr = td.getDateStr().split(" ")[0];
                }
                if (minuteBorder == -1) {
                    minuteBorder = Helper.getNextMinute(td.getDate());
                }

                // calculate the data per minute interval
                if (td.time < minuteBorder) {
                    minuteSum = minuteSum.add(td.getValue());
                    minuteTickCount++;
                } else {
                    //new tick data over minute-border, save the previous minute data
                    minuteSumQueue.add(new MinuteData(minuteTickCount, minuteSum));

                    String newDateStr = td.getDateStr().split(" ")[0];
                    if (!newDateStr.equals(currentDateStr)) {
                        // a new trading  now, 
                        // calc the sma now

                        if (periodSum == null) {
                            // only add all minute sum for when period sum is uninitialized
                            periodSum = Helper.sum(Helper.transformToBigDecimal(minuteSumQueue));
                        } else {
                            MinuteData lastMinute = minuteSumQueue.getLast();
                            periodSum = periodSum.add(lastMinute.sum);
                        }

                        sma = periodSum.divide(new BigDecimal(String.valueOf(periodTickCount)), DECIMAL_POINT, RoundingMode.HALF_UP);
                        result.add(sma);
                        // clear all minute data, period data
                        minuteSumQueue.clear();
                        periodTickCount = 0;
                        periodSum = null;

                        // 
                        periodBorder = Helper.getNextPeriodByMinute(td.getDate(), timeInMinute);
                        currentDateStr = newDateStr;
                    }
                    minuteBorder = Helper.getNextMinute(td.getDate());
                    //System.out.println("minute = " + Constants.DATE_TIME_FORMAT.format(new Date(minuteBorder)));

                    minuteSum = BigDecimal.ZERO.add(td.getValue());
                    minuteTickCount = 1;
                }

                if (td.time >= periodBorder) {

                    periodBorder = Helper.getNextMinute(td.getDate());
                    //periodTickCount = periodTickCount + minuteTickCount;
                    //System.out.println("period = " + Constants.DATE_TIME_FORMAT.format(new Date(periodBorder)));
                    if (periodSum == null) {
                        // only add all minute sum for when period sum is uninitialized
                        periodSum = Helper.sum(Helper.transformToBigDecimal(minuteSumQueue));
                    } else {
                        MinuteData lastMinute = minuteSumQueue.getLast();
                        periodSum = periodSum.add(lastMinute.sum);
                    }
                    sma = periodSum.divide(new BigDecimal(String.valueOf(periodTickCount)), DECIMAL_POINT, RoundingMode.HALF_UP);
                    result.add(sma);
                    //System.out.println(sma);

                    // subtract first minute sum from period sum
                    if (minuteSumQueue.size() > 0) {
                        MinuteData d = minuteSumQueue.poll();
                        periodSum = periodSum.subtract(d.sum);
                        periodTickCount = periodTickCount - d.count;
                    }

                }
                periodTickCount++;
            }
        }
        // cater for last tick
        if (periodSum == null) {
            // or roll next date and data point less than 1 minue
            if (minuteSumQueue.size() > 0) {
                minuteSumQueue.add(new MinuteData(minuteTickCount, minuteSum));
                periodSum = Helper.sum(Helper.transformToBigDecimal(minuteSumQueue));
                sma = periodSum.divide(new BigDecimal(String.valueOf(periodTickCount)), DECIMAL_POINT, RoundingMode.HALF_UP);
                result.add(sma);
            } else {
                // for the case that all tick data less than 1 minute
                sma = minuteSum.divide(new BigDecimal(String.valueOf(minuteTickCount)), DECIMAL_POINT, RoundingMode.HALF_UP);
                result.add(sma);
            }
        } else {
            periodSum = periodSum.add(minuteSum);
            sma = periodSum.divide(new BigDecimal(String.valueOf(periodTickCount)), DECIMAL_POINT, RoundingMode.HALF_UP);
            result.add(sma);
        }
        //System.out.println(sma);

        return result;
    }

    /**
     *
     * SMA(T) = (Previous Period Sum - oldest Period Sum + Sum period (T) ) /
     * total period count
     *
     * read all data from the file before calcuation
     *
     * @param priceDataQueue
     * @param batchSize
     * @param tickFilter
     * @return
     */
    public static Queue<BigDecimal> calculateSMAByTrade(Queue<? extends DatePoint> priceDataQueue, int batchSize) {
        if (batchSize == 1) {
            // batch size of 1 trade means no need to do calculation
            return Helper.transform(priceDataQueue);
        } else {

            BigDecimal sum = BigDecimal.ZERO;
            BigDecimal sma = BigDecimal.ZERO;
            int count = 0;
            Queue<DatePoint> localTickDataQueue = new ArrayDeque<DatePoint>();
            Queue<BigDecimal> result = new ArrayDeque<BigDecimal>();
            for (DatePoint td : priceDataQueue) {
                DatePoint localTick = td;
                if (localTick != null) {
                    if (localTickDataQueue.size() == batchSize) {
                        sma = sum.divide(new BigDecimal(String.valueOf(batchSize)),
                                Constants.DECIMAL_POINT, RoundingMode.HALF_UP);
                        sum = sum.subtract(localTickDataQueue.poll().getValue());
                        result.add(sma);
                    }
                    localTickDataQueue.add(localTick);
                    sum = sum.add(localTick.getValue());
                }
            }
            if (localTickDataQueue.size() != 0) {
                sma = sum.divide(new BigDecimal(String.valueOf(localTickDataQueue.size())),
                        Constants.DECIMAL_POINT, RoundingMode.HALF_UP);
                result.add(sma);
            }
            return result;
        }
    }
}
