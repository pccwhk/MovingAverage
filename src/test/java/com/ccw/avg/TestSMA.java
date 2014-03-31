/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ccw.avg;

import com.ccw.avg.model.TickData;
import static com.ccw.avg.util.Constants.DATE_TIME_FORMAT;
import com.ccw.avg.util.SMAUtil;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Queue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 *
 * @author ccwhk
 */
public class TestSMA extends TestCase {

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public TestSMA(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(TestSMA.class);
    }

    public void testSMAByTimeRollDate() throws ParseException {
        int timeInMinute = 2;

        TickData dpt1 = new TickData("B", "2014-03-01 12:21:21.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:21.125"),
                new BigDecimal("2.6"), 12, "");

        TickData dpt2 = new TickData("B", "2014-03-01 12:21:22.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:22.125"),
                new BigDecimal("2.5"), 12, "");
        TickData dpt3 = new TickData("B", "2014-03-01 12:21:59.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:59.125"),
                new BigDecimal("2.4"), 12, "");
        /////////////////////////////////////
        TickData dpt4 = new TickData("B", "2014-03-02 12:22:43.125", DATE_TIME_FORMAT.parse("2014-03-02 12:22:43.125"),
                new BigDecimal("2.7"), 12, "");
        //////////////////////////////////
        TickData dpt5 = new TickData("B", "2014-03-03 12:22:49.125", DATE_TIME_FORMAT.parse("2014-03-03 12:22:49.125"),
                new BigDecimal("2.2"), 12, "");
        TickData dpt6 = new TickData("B", "2014-03-03 12:23:21.125", DATE_TIME_FORMAT.parse("2014-03-03 12:23:21.125"),
                new BigDecimal("3.0"), 12, "");
        //////////////////////////////////
        TickData dpt7 = new TickData("B", "2014-03-04 12:23:27.125", DATE_TIME_FORMAT.parse("2014-03-04 12:23:27.125"),
                new BigDecimal("2.75"), 12, "");

        Queue<TickData> q = new ArrayDeque<TickData>();
        q.add(dpt1);
        q.add(dpt2);
        q.add(dpt3);
        q.add(dpt4);
        q.add(dpt5);
        q.add(dpt6);
        q.add(dpt7);
        Queue<BigDecimal> r = SMAUtil.calculateSMAByTime(q, timeInMinute);

        assert r.size() == 4;
        assert r.poll().compareTo(new BigDecimal("2.5")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.7")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.6")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.75")) == 0;

    }
    
    public void testSMAByTime5Min() throws ParseException {
        int timeInMinute = 5;

        TickData dpt1 = new TickData("B", "2014-03-01 12:21:20.125", DATE_TIME_FORMAT.parse("2014-03-01 12:21:20.125"),
                new BigDecimal("2.6"), 12, "");

        TickData dpt2 = new TickData("B", "2014-03-01 12:23:22.125", DATE_TIME_FORMAT.parse("2014-03-01 12:23:22.125"),
                new BigDecimal("2.5"), 12, "");
        TickData dpt3 = new TickData("B", "2014-03-01 12:24:59.125", DATE_TIME_FORMAT.parse("2014-03-01 12:24:59.125"),
                new BigDecimal("2.4"), 12, "");
        TickData dpt4 = new TickData("B", "2014-03-01 12:26:43.125", DATE_TIME_FORMAT.parse("2014-03-01 12:26:43.125"),
                new BigDecimal("2.9"), 12, "");
        //////////////////////////////////
        TickData dpt5 = new TickData("B", "2014-03-03 12:22:49.125", DATE_TIME_FORMAT.parse("2014-03-03 12:22:49.125"),
                new BigDecimal("2.2"), 12, "");
        TickData dpt6 = new TickData("B", "2014-03-03 12:23:21.125", DATE_TIME_FORMAT.parse("2014-03-03 12:23:21.125"),
                new BigDecimal("3.0"), 12, "");
        TickData dpt7 = new TickData("B", "2014-03-03 12:23:27.125", DATE_TIME_FORMAT.parse("2014-03-03 12:23:27.125"),
                new BigDecimal("2.75"), 12, "");

        Queue<TickData> q = new ArrayDeque<TickData>();
        q.add(dpt1);
        q.add(dpt2);
        q.add(dpt3);
        q.add(dpt4);
        q.add(dpt5);
        q.add(dpt6);
        q.add(dpt7);
        Queue<BigDecimal> r = SMAUtil.calculateSMAByTime(q, timeInMinute);

        assert r.size() == 3;
        assert r.poll().compareTo(new BigDecimal("2.5")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.6")) == 0;
        assert r.poll().compareTo(new BigDecimal("2.65")) == 0;
        

    }
}
