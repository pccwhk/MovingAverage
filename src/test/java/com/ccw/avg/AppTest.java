package com.ccw.avg;

import com.ccw.avg.model.Average;
import com.ccw.avg.model.DatePoint;
import com.ccw.avg.model.EMA;
import com.ccw.avg.model.MACD;
import com.ccw.avg.model.TickData;
import static com.ccw.avg.util.Constants.DATE_TIME_FORMAT;
import com.ccw.avg.util.EMAUtil;
import com.ccw.avg.util.Helper;
import com.ccw.avg.util.MACDUtil;
import com.ccw.avg.util.SMAUtil;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    public void testSMAByTimeSingle1() throws ParseException {
        TickData dpt1 = new TickData("A", "2014-03-01 12:21:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:21.125"),
                new BigDecimal("999.9"), 12, "");

        Queue<TickData> q = new ArrayDeque<TickData>();
        q.add(dpt1);

        Queue<BigDecimal> r = SMAUtil.calculateSMAByTime(q, 2);
        assert r.size() == 1;

        for (BigDecimal c : r) {
            assert c.compareTo(new BigDecimal("999.9")) == 0;
        }
    }

    public void testSMAByTimeSingle2() throws ParseException {
        TickData dpt1 = new TickData("A", "2014-03-01 12:21:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:21.125"),
                new BigDecimal("10"), 12, "");
        TickData dpt2 = new TickData("A", "2014-03-01 12:21:22.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:22.125"),
                new BigDecimal("20"), 12, "");

        Queue<TickData> q = new ArrayDeque<TickData>();
        q.add(dpt1);
        q.add(dpt2);
        Queue<BigDecimal> r = SMAUtil.calculateSMAByTime(q, 2);
        assert r.size() == 1;

        for (BigDecimal c : r) {
            assert c.compareTo(new BigDecimal("15.0")) == 0;
        }
    }


    
    public void testSMAByTime() throws ParseException {
        int timeInMinute = 2;

        TickData dpt1 = new TickData("A", "2014-03-01 12:21:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:21.125"),
                new BigDecimal("2.6"), 12, "");

        TickData dpt2 = new TickData("A", "2014-03-01 12:21:22.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:22.125"),
                new BigDecimal("2.5"), 12, "");
        TickData dpt3 = new TickData("A", "2014-03-01 12:21:59.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:59.125"),
                new BigDecimal("2.4"), 12, "");
        TickData dpt4 = new TickData("A", "2014-03-01 12:22:43.125", DATE_TIME_FORMAT.parse("2014-03-01 12:22:43.125"),
                new BigDecimal("2.7"), 12, "");
        TickData dpt5 = new TickData("A", "2014-03-01 12:22:49.125", DATE_TIME_FORMAT.parse("2014-03-01 12:22:49.125"),
                new BigDecimal("2.6"), 12, "");
        TickData dpt6 = new TickData("A", "2014-03-01 12:23:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:23:21.125"),
                new BigDecimal("2.65"), 12, "");
        TickData dpt7 = new TickData("A", "2014-03-01 12:24:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:24:21.125"),
                new BigDecimal("2.48"), 12, "");

        Queue<TickData> q = new ArrayDeque<TickData>();
        q.add(dpt1);
        q.add(dpt2);
        q.add(dpt3);
        q.add(dpt4);
        q.add(dpt5);
        q.add(dpt6);
        q.add(dpt7);
        Queue<BigDecimal> r = SMAUtil.calculateSMAByTime(q, timeInMinute);

        assert r.size () == 3;
        assert r.poll().compareTo( new BigDecimal("2.56")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.65") ) == 0;
        assert r.poll().compareTo(new BigDecimal("2.565") ) == 0;
        

    }

    public void testSMAByTrade() throws ParseException {
        int tradeBatchSize = 2;
        DatePoint dpt1 = new DatePoint(new BigDecimal("1.5"), Helper.getDate("2014-03-01"));
        DatePoint dpt2 = new DatePoint(new BigDecimal("2.5"), Helper.getDate("2014-03-02"));
        DatePoint dpt3 = new DatePoint(new BigDecimal("3.5"), Helper.getDate("2014-03-03"));
        DatePoint dpt4 = new DatePoint(new BigDecimal("4.5"), Helper.getDate("2014-03-04"));
        DatePoint dpt5 = new DatePoint(new BigDecimal("4.7"), Helper.getDate("2014-03-05"));
        DatePoint dpt6 = new DatePoint(new BigDecimal("4.8"), Helper.getDate("2014-03-06"));
        Queue<DatePoint> q = new ArrayDeque<DatePoint>();
        q.add(dpt1);
        q.add(dpt2);
        q.add(dpt3);
        q.add(dpt4);
        q.add(dpt5);
        q.add(dpt6);
        Queue<BigDecimal> r = SMAUtil.calculateSMAByTrade(q, tradeBatchSize);
        assert r.size() == 5;
        assert r.poll().compareTo(new BigDecimal("2")) == 0;
        assert r.poll().compareTo(new BigDecimal("3")) == 0;
        assert r.poll().compareTo(new BigDecimal("4")) == 0;
        assert r.poll().compareTo(new BigDecimal("4.6")) == 0;
        assert r.poll().compareTo(new BigDecimal("4.75")) == 0;
    }

    public void testEMA() throws ParseException {
        int dayCount = 3;
        DatePoint dpt1 = new DatePoint(new BigDecimal("1.5"), Helper.getDate("2014-03-01"));
        DatePoint dpt2 = new DatePoint(new BigDecimal("2.5"), Helper.getDate("2014-03-02"));
        DatePoint dpt3 = new DatePoint(new BigDecimal("3.5"), Helper.getDate("2014-03-03"));
        DatePoint dpt4 = new DatePoint(new BigDecimal("4.5"), Helper.getDate("2014-03-04"));
        DatePoint dpt5 = new DatePoint(new BigDecimal("4.7"), Helper.getDate("2014-03-05"));
        DatePoint dpt6 = new DatePoint(new BigDecimal("4.8"), Helper.getDate("2014-03-06"));
        Queue<DatePoint> q = new ArrayDeque<DatePoint>();
        q.add(dpt1);
        q.add(dpt2);
        q.add(dpt3);
        q.add(dpt4);
        q.add(dpt5);
        q.add(dpt6);
        Queue<EMA> emas = EMAUtil.calculateEMASeries(q, dayCount);

        assert emas.size() == 4;
        assert emas.poll().getValue().compareTo(new BigDecimal("2.50")) == 0;
        assert emas.poll().getValue().compareTo(new BigDecimal("3.50")) == 0;
        assert emas.poll().getValue().compareTo(new BigDecimal("4.10")) == 0;
        assert emas.poll().getValue().compareTo(new BigDecimal("4.450")) == 0;
    }

    public void testMACD() throws ParseException {
        EMA fastdp1 = new EMA(6, new BigDecimal("0.12"), Helper.getDate("2014-01-01"));
        EMA fastdp2 = new EMA(6, new BigDecimal("0.13"), Helper.getDate("2014-01-02"));
        EMA slowdp1 = new EMA(12, new BigDecimal("0.1"), Helper.getDate("2014-01-02"));
        Queue<EMA> fast = new ArrayDeque<EMA>();
        Queue<EMA> slow = new ArrayDeque<EMA>();
        fast.add(fastdp1);
        fast.add(fastdp2);
        slow.add(slowdp1);

        Queue<MACD> q = MACDUtil.calculateMACD(fast, slow, 6, 12);
        assert q.size() == 1;
        assert q.peek().getValue().compareTo(new BigDecimal("0.03")) == 0;
        assert q.peek().getDateStr().equals("2014-01-02");
    }

    public void testGetNextPeriod() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date d = df.parse("2013-01-05 23:45:12.125");
        Date nextPeriod = new Date(Helper.getNextPeriodBySecond(d, 3600));
        String s = df.format(nextPeriod);
        //System.out.println(s);
        assert s.equals("2013-01-06 00:45:00.000");

        Date d2 = df.parse("2013-01-05 23:40:12.125");
        Date nextPeriod2 = new Date(Helper.getNextMinute(d2));
        String s2 = df.format(nextPeriod2);
        //System.out.println(s2);
        assert s2.equals("2013-01-05 23:41:00.000");
    }

    /**
     * Rigourous Test :-)
     */
    public void testAverage() {
        ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
        for (int i = 0; i < 10; i++) {
            BigDecimal n = new BigDecimal(String.valueOf(i));
            list.add(n);
        }
        Average avg = Helper.calculateAverage(list);
        assert avg.average.compareTo(new BigDecimal("4.5")) == 0;
        assert avg.count == 10;
        assert avg.sum.compareTo(new BigDecimal("45.0")) == 0;
        assert list.size() == 10;
    }

}
