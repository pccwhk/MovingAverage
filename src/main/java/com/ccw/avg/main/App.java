package com.ccw.avg.main;

import com.ccw.avg.filter.TickFilter;
import com.ccw.avg.filter.TimeFilter;
import com.ccw.avg.model.EMA;
import com.ccw.avg.model.MACD;
import com.ccw.avg.model.MACDSeries;
import com.ccw.avg.model.MinuteData;
import com.ccw.avg.model.SMAType;
import com.ccw.avg.model.TickData;
import com.ccw.avg.util.Constants;
import static com.ccw.avg.util.Constants.DECIMAL_POINT;
import com.ccw.avg.util.DataParser;
import com.ccw.avg.util.EMAUtil;
import com.ccw.avg.util.Helper;
import com.ccw.avg.util.MACDUtil;
import com.ccw.avg.util.SMAUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Moving Average
 *
 */
public class App {

    //private final static Logger logger = LoggerFactory.getLogger(App.class);
    private final static int MIN_ARGS = 2;

    public static void main(String[] args) throws ParseException {

        // parse input 
        if (args.length < MIN_ARGS) {
            System.out.println(String.format("Minimum of arguments is %s", MIN_ARGS));
            System.exit(-1);
        } else {
            // first args has to be file name, which is already sorted
            String fileName = args[0];
            Path path = Paths.get(fileName);
            if (!Files.isReadable(path)) {
                // 
                System.err.println(String.format("{} does not exist or not readable ", fileName));
                System.exit(-1);
            }

            // 2nd arg is the type
            String type = args[1].toUpperCase();
            switch (type) {
                case Constants.STR_SMA:
                    // parse the input 
                    if (args.length < 4) {
                        System.out.println("Correct input for SMA is <fileName> SMA <number> [TRADE/MIN] <start time> <endTime>");
                    } else {
                        // argments:
                        // filename SMA [number] [TRADE/MIN] [start time e.g. 11:30] [end time e.g 22:45]
                        int number = Integer.parseInt(args[2].trim());
                        String unitType = args[3].trim().toUpperCase();
                        SMAType smaType = SMAType.TRADE;
                        if (Constants.STR_MIN.equals(unitType)) {
                            smaType = SMAType.MINUTE;
                        }
                        Map configMap = new HashMap();
                        String startTime = null;
                        String endTime = null;
                        if (args.length == 6) {
                            // optional start time and end time exists
                            startTime = args[4].trim();
                            endTime = args[5].trim();
                        }

                        Queue<BigDecimal> smas = processSMA(path, number, smaType, startTime, endTime);

                        for (BigDecimal e : smas) {
                            System.out.println(e);
                        }
                    }
                    break;
                case Constants.STR_EMA:
                    // filename EMA [number in day]  
                    int number = Integer.parseInt(args[2]);
                    String startTime = null;
                    String endTime = null;
                    if (args.length > 3) {
                        // optional start time and end time exists
                        startTime = args[4].trim();
                        endTime = args[5].trim();
                    }
                    Queue<EMA> emas = processEMA(path, number, startTime, endTime);
                    for (EMA e : emas) {
                        System.out.println(e);
                    }
                    break;
                case Constants.STR_MACD:
                    // filename MACD [fast] [slow] [Signal] [start time e.g. 11:30] [end time e.g 22:45]
                    if (args.length < 5) {
                        System.out.println("Correct input for MACD is <fileName> MACD <fast-day-count>  <slow-day-count> <signal-day-count> <start time> <endTime>");
                    } else {
                        int fastDayCount = Integer.parseInt(args[2].trim());
                        int slowDayCount = Integer.parseInt(args[3].trim());
                        int signalDayCount = Integer.parseInt(args[4].trim());
                        String startTime1 = null;
                        String endTime1 = null;
                        if (args.length == 7) {
                            startTime1 = args[5].trim();
                            endTime1 = args[6].trim();
                        }
                        MACDSeries macdSeries = processMACD(path, fastDayCount, slowDayCount, signalDayCount, startTime1, endTime1);
                        for (MACD macd : macdSeries.macdData){
                            System.out.println(macd);
                        }
                        for (EMA signal : macdSeries.signalLine) {
                            System.out.println(signal);
                        }
                      
                    }
                    break;
                default:
                    System.out.println("Please input valid moving average type : SMA, EMA or MACD ");
                    break;
            }
        }

    }

    public static Queue<TickData> prepareClosingPrice(Path path, String startTime, String endTime) {
        BufferedReader br = null;
        Queue<TickData> tickDataQueue = new ArrayDeque<TickData>();
        TickFilter tickFilter = new TimeFilter(startTime, endTime);
        String currentDate = null;
        TickData closePrice = null;
        TickData td;
        try {
            br = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            // skip the header
            String header = br.readLine();

            String line = null;
            while ((line = br.readLine()) != null) {
                td = tickFilter.filter(DataParser.toTickData(line));
                if (td != null) {
                    if (currentDate != null) {
                        if (td.getDateStr().equals(currentDate)) {
                            closePrice = td;
                        } else {
                            // another date now
                            tickDataQueue.add(closePrice);
                            closePrice = td;
                            currentDate = td.getDateStr();
                        }
                    } else {
                        // current date is null
                        currentDate = td.getDateStr();
                        closePrice = td;
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        tickDataQueue.add(closePrice);
        return tickDataQueue;
    }

    // assume file is sorted 
    // close price is defined as the last traded price of a day 
    public static Queue<EMA> processEMA(Path path, int numberOfDays, String startTime, String endTime) {
        Queue<TickData> tickDataQueue = prepareClosingPrice(path, startTime, endTime);
        return EMAUtil.calculateEMASeries(tickDataQueue, numberOfDays);
    }

    public static MACDSeries processMACD(Path path, int fast, int slow, int signal, String startTime, String endTime) throws ParseException {
        Queue<TickData> tickDataQueue = prepareClosingPrice(path, startTime, endTime);
        Queue<EMA> fastData = EMAUtil.calculateEMASeries(tickDataQueue, fast);
        Queue<EMA> slowData = EMAUtil.calculateEMASeries(tickDataQueue, slow);
        Queue<MACD> macdData = MACDUtil.calculateMACD(fastData, slowData, fast, slow);

        Queue<EMA> signalLine = EMAUtil.calculateEMASeries(macdData, signal);
        return new MACDSeries(macdData, signalLine, fast, slow, signal);
    }

    public static Queue<BigDecimal> processSMA(Path path, int number, SMAType type, String startTime, String endTime) {
        BufferedReader br = null;
        TickFilter tickFilter = new TimeFilter(startTime, endTime);
        Queue<TickData> tickDataQueue = new ArrayDeque<TickData>();
        Queue<BigDecimal> result = new ArrayDeque<BigDecimal>();
        try {
            br = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            // skip the header
            String header = br.readLine();

            String line = null;
            while ((line = br.readLine()) != null) {
                TickData td = tickFilter.filter(DataParser.toTickData(line));
                if (td != null) {
                    tickDataQueue.add(td);

                }
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();

                }
            } catch (IOException ex) {
                Logger.getLogger(App.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (type == SMAType.TRADE) {
            result = SMAUtil.calculateSMAByTrade(tickDataQueue, number);
        } else if (type == SMAType.MINUTE) {
            result = SMAUtil.calculateSMAByTime(tickDataQueue, number);
        }
        return result;
    }

    /**
     * calculate as we read the data for using less memory
     *
     * @param path
     * @param batchSize
     * @param tickFilter
     */
    public static Queue<BigDecimal> calculateSMAByTrade(Path path, int batchSize, TickFilter tickFilter) {
        BufferedReader br = null;
        String line = null;
        Queue<BigDecimal> result = new ArrayDeque<BigDecimal>();
        try {
            br = Files.newBufferedReader(path, Charset.forName("UTF-8"));
            // skip the header
            String header = br.readLine();
            Queue<TickData> tickDataQueue = new ArrayDeque<TickData>();
            BigDecimal sum = BigDecimal.ZERO;
            BigDecimal sma = BigDecimal.ZERO;

            while ((line = br.readLine()) != null) {
                TickData td = tickFilter.filter(DataParser.toTickData(line));
                if (td != null) {
                    if (batchSize == tickDataQueue.size()) {
                        // to do boundary cases 
                        sma = sum.divide(new BigDecimal(String.valueOf(batchSize)), DECIMAL_POINT, RoundingMode.HALF_UP);
                        result.add(sma);

                        // After SMA is done, remove the sum with the first element value 
                        // for faster calcuation
                        sum = sum.subtract(tickDataQueue.poll().getValue());
                    }
                    sum = sum.add(td.getValue());
                    tickDataQueue.add(td);

                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
            return result;
        } catch (ParseException ex) {
            Logger.getLogger(App.class
                    .getName()).log(Level.SEVERE, null, ex);
            return result;
        } finally {
            try {
                if (br != null) {
                    br.close();

                }
            } catch (IOException ex) {
                Logger.getLogger(App.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
