/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg.util;

import com.ccw.avg.model.TickData;
import static com.ccw.avg.util.Constants.DATE_TIME_FORMAT;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 *
 * @author potapig
 */
public class DataParser {

    public static TickData toTickData(String line) throws ParseException {
        if (line == null || "".equals(line)) {
            return null;
        } else {
            String data[] = line.split(",");
            String dateStr = data[0].trim();
            String symbol = data[1].trim();
            BigDecimal price = new BigDecimal(data[2].trim());
            Long size = Long.parseLong(data[3].trim());
            String qualifier = data[4].trim();
            String timeStr = data[5].trim();
            Date date = DATE_TIME_FORMAT.parse(String.format("%s %s", dateStr, timeStr));
            return new TickData(symbol, dateStr, date, price, size, qualifier);
        }
    }
}
