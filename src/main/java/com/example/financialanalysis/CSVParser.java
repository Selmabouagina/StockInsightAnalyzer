package com.example.financialanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class CSVParser {

    private static DatabaseManager dbManager;

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    public static void setDbManager(DatabaseManager dbManager) {
        CSVParser.dbManager = dbManager;
    }

    public CSVParser() {
        dbManager = new DatabaseManager();
    }

    public static String readJSONFromFile(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        boolean firstLine = true; // Flag to skip the header
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip the header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    public void loadStockDataFromCSV(String filePath) {
        String json = readJSONFromFile(filePath);
        String jsonData = json.trim()
                .replace("\"\"", "\"")  // Replace double quotes inside fields with single quotes
                .replaceAll("^\"|\"$", "");         
    
        // Parse the JSON data
        Gson gson = new GsonBuilder().setLenient().create();
        try {
            Reader reader = new StringReader(jsonData);
    
            TypeToken<StockData> typeToken = new TypeToken<StockData>() {};
            StockData stockData = gson.fromJson(reader, typeToken.getType());
    
            // Accessing Meta Data
            MetaData metaData = stockData.getMetaData();
            int metadataId = dbManager.saveMetaData(metaData); // Method to save metadata and get the ID
    
            // Save time series data to the database
            Map<String, TimeSeriesData> timeSeriesMap = stockData.getTimeSeries();
            dbManager.saveTimeSeriesData(metadataId, timeSeriesMap); // Method to save time series
        } catch (JsonSyntaxException e) {
            System.err.println("JSON Syntax Error: " + e.getMessage());
        }
    }
    



}