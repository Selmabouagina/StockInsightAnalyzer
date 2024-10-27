package com.example.financialanalysis;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Timer;
import java.util.TimerTask;

public class DataFetcher {

    private static boolean isFetchingScheduled = false;

    private static final String API_KEY = "TJEORK0RLDPR808C";  // Replace with your Alpha Vantage API key
    private static final String SYMBOL = "IBM";  // Stock symbol
    private static final String FUNCTION = "TIME_SERIES_INTRADAY";  // API function for real-time data
    private static final String INTERVAL = "5min";  // Data fetch interval (5min, 15min, etc.)
    private static final String URL_STRING = "https://www.alphavantage.co/query?function=" + FUNCTION +
            "&symbol=" + SYMBOL + "&interval=" + INTERVAL + "&apikey=" + API_KEY;

    // Method to fetch real-time stock data using HttpURLConnection
    public static String fetchStockData() throws IOException {
        try {
            // Create a URL object using URI to avoid deprecated constructor
            URL url = URI.create(URL_STRING).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);  // Timeout for connection
            connection.setReadTimeout(5000);  // Timeout for reading data

            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Failed to fetch data. HTTP Status Code: " + status);
            }

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            connection.disconnect();

            return response.toString();
        } catch (IOException e) {
            throw new IOException("Error fetching stock data: " + e.getMessage(), e);
        }
    }

    // Method to save data to CSV
    public static void saveToCSV(String data) {
        try {
            String filePath = "src/main/resources/data/stock_data.csv";
            boolean append = Files.exists(Paths.get(filePath)); // Check if file exists

            try (FileWriter writer = new FileWriter(filePath, append)) {
                if (!append) {
                    // Write headers if the file is created for the first time
                    writer.append("Time,Open,High,Low,Close,Volume\n"); // Modify as needed based on actual data structure
                }
                writer.append(data).append("\n");
                System.out.println("Data saved to CSV.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Schedule the data fetching
    public static boolean scheduleDataFetching() {
        if (isFetchingScheduled) {
            return false; // Already scheduled
        }

        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                try {
                    String stockData = fetchStockData();
                    if (stockData != null) {
                        saveToCSV(stockData);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        
        Timer timer = new Timer("Timer");
        long delay = 0L;
        long period = 300000L; // 5 minutes (300,000 milliseconds)

        try {
            timer.scheduleAtFixedRate(repeatedTask, delay, period);
            isFetchingScheduled = true; // Mark as scheduled
            return true; // Scheduling was successful
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false; // Scheduling failed
        }
    }
}
