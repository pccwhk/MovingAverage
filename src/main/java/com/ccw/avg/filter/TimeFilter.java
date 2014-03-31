/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.filter;

import com.ccw.avg.model.TickData;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author potapig
 */
public class TimeFilter implements TickFilter {

    private int startHour;
    private int startMinute;

    private int endHour;
    private int endMinute;

    private final Calendar calendar;

    private boolean isEnableTimeFilter = false;
    private String dateStr = null;
    private long startTime = -1;
    private long endTime = Long.MAX_VALUE;

    public TimeFilter(String startTime, String endTime) {
        if (startTime != null) {
            String start[] = startTime.split(":");
            startHour = Integer.parseInt(start[0]);
            startMinute = Integer.parseInt(start[1]);
            isEnableTimeFilter = true;
        }
        if (endTime != null) {
            String end[] = endTime.split(":");
            endHour = Integer.parseInt(end[0]);
            endMinute = Integer.parseInt(end[1]);
            isEnableTimeFilter = true;
        }
        if (isEnableTimeFilter) {
            // only init calendar getInstance when needed 
            calendar = Calendar.getInstance();
        } else {
            calendar = null;
        }
    }

    @Override
    public TickData filter(TickData tickData) {
        if (tickData == null){
            return null;
        }
        
        if (!isEnableTimeFilter && startTime == -1 && endTime == Long.MAX_VALUE) {
            return tickData;
        }
        if (dateStr == null || !dateStr.equals(tickData.getDateStr())) {
            // build the correct time only if
            // a) first time 
            // OR 
            // b)  the date is change
            dateStr = tickData.getDateStr();

            calendar.clear();
            calendar.setTime(tickData.getDate());
            calendar.set(Calendar.HOUR_OF_DAY, startHour);
            calendar.set(Calendar.MINUTE, startMinute);
            startTime = calendar.getTime().getTime();

            calendar.set(Calendar.HOUR_OF_DAY, endHour);
            calendar.set(Calendar.MINUTE, endMinute);
            endTime = calendar.getTime().getTime();

        }

        if (tickData.time >= startTime && tickData.time <= endTime) {
            return tickData;
        } else {
            return null;
        }
    }
}
