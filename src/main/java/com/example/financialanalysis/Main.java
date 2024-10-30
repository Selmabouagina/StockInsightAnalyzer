package com.example.financialanalysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Introduction
        System.out.println("Let's analyze some stocks!");

        
        // Load stock data from CSV
        CSVParser parser = new CSVParser();
        DatabaseManager dbManager = parser.getDbManager();
        CSVParser.setDbManager(dbManager);
        String csvFilePath = "src/main/resources/data/stock_data.csv";
        
        System.out.println("Loading stock data from CSV...");
        parser.loadStockDataFromCSV(csvFilePath);

        // Fetch metadata ID to use for fetching time series data
        // Assuming you know the metadata ID or you can fetch it based on some criteria
        int metadataId = 2; // Change as per your implementation

        // Fetch time series data for analysis
        System.out.println("Fetching time series data...");

        Map<String, TimeSeriesData> timeSeriesData = dbManager.fetchTimeSeriesData(metadataId);
        List<String> dates = new ArrayList<>(timeSeriesData.keySet());
        Collections.sort(dates);
        // Check if data is fetched successfully
        if (timeSeriesData.isEmpty()) {
            System.out.println("No time series data available for analysis.");
            return;
        }

        // Perform analysis
        TimeSeriesAnalysis timeSeriesAnalysis = new TimeSeriesAnalysis();
        
        System.out.println("Calculating daily returns...");
        List<Double> returns = timeSeriesAnalysis.calculateReturns(timeSeriesData, dates);
        System.out.println("Daily Returns: " + returns);
        
        int smaPeriod = 5; // You can change this period as needed
        System.out.println("Calculating Simple Moving Average (SMA) for period " + smaPeriod + "...");
        List<Double> sma = timeSeriesAnalysis.calculateSMA(timeSeriesData, dates, smaPeriod);
        System.out.println("SMA: " + sma);
        
        int emaPeriod = 10; // You can change this period as needed
        System.out.println("Calculating Exponential Moving Average (EMA) for period " + emaPeriod + "...");
        List<Double> ema = timeSeriesAnalysis.calculateEMA(timeSeriesData, dates, emaPeriod);
        System.out.println("EMA: " + ema);

        // Indicate completion
        System.out.println("Analysis completed successfully.");
    }
}

