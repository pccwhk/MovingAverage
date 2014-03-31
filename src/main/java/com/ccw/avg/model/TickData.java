package com.ccw.avg.model;

import static com.ccw.avg.model.MACD.DATE_TIME_FORMAT;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author ccwhk
 */
public class TickData extends DatePoint {

    public final String symbol;
    public final long time;
    public final long size;
    public final String qualifier;
    
    public TickData(String symbol, String dateStr, Date date, BigDecimal price, long size, String qualifier) {
        super(price, date);
        this.symbol = symbol;
        this.size = size;
        this.dateStr = dateStr;
        this.qualifier = qualifier;
        this.time = date.getTime();
    }

    @Override
    public String toString() {
        return "TickData{" + "symbol=" + symbol + ", time=" + time + ", price=" + value + ", dateStr=" + dateStr + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.date);
        hash = 29 * hash + Objects.hashCode(this.symbol);
        hash = 29 * hash + Objects.hashCode(this.value);
        hash = 29 * hash + (int) (this.size ^ (this.size >>> 32));
        hash = 29 * hash + Objects.hashCode(this.qualifier);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TickData other = (TickData) obj;
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.qualifier, other.qualifier)) {
            return false;
        }
        return true;
    }


}
