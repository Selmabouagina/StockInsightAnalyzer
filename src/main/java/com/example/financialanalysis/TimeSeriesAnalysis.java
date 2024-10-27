package com.example.financialanalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimeSeriesAnalysis {

    // Calculate daily percentage returns from close prices
    public List<Double> calculateReturns(Map<String, TimeSeriesData> timeSeriesData, List<String> dates) {
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < dates.size(); i++) {
            double currentClose = Double.parseDouble(timeSeriesData.get(dates.get(i)).getClose());
            double previousClose = Double.parseDouble(timeSeriesData.get(dates.get(i - 1)).getClose());
            double dailyReturn = (currentClose - previousClose) / previousClose * 100; // percentage return
            returns.add(dailyReturn);
        }
        return returns;
    }

    // Calculate simple moving average (SMA) over a period
    public List<Double> calculateSMA(Map<String, TimeSeriesData> timeSeriesData, List<String> dates, int period) {
        List<Double> smaList = new ArrayList<>();
        for (int i = period - 1; i < dates.size(); i++) {
            double sum = 0.0;
            for (int j = 0; j < period; j++) {
                sum += Double.parseDouble(timeSeriesData.get(dates.get(i - j)).getClose());
            }
            double sma = sum / period;
            smaList.add(sma);
        }
        return smaList;
    }

    // Calculate exponential moving average (EMA) over a period
    public List<Double> calculateEMA(Map<String, TimeSeriesData> timeSeriesData, List<String> dates, int period) {
        List<Double> emaList = new ArrayList<>();
        
        double multiplier = 2.0 / (period + 1);
        double previousEMA = 0.0;
        
        for (int i = 0; i < dates.size(); i++) {
            double currentClose = Double.parseDouble(timeSeriesData.get(dates.get(i)).getClose());
            if (i == 0) {
                previousEMA = currentClose;  // Start with the first close price as the first EMA
            } else {
                previousEMA = (currentClose - previousEMA) * multiplier + previousEMA;
            }
            emaList.add(previousEMA);
        }
        return emaList;
    }
}
