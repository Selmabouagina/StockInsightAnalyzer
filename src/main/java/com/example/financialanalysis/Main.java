package com.example.financialanalysis;

public class Main {
    public static void main(String[] args) {
        // Your code goes here!
        System.out.println("Let's analyze some stocks!");
        
        boolean result = DataFetcher.scheduleDataFetching();
        if (result){
            System.out.println("Data fetching should be scheduled successfully");
        }

        CSVParser.processStockDataFromCSV("src/main/resources/data/stock_data.csv");
    }
    
}
