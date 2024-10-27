package com.example.financialanalysis;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CSVParser {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/financial_analysis"; // Adjust as needed
    private static final String USER = "root"; // Adjust as needed
    private static final String PASS = "salma"; // Adjust as needed
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
                // Clean the line: remove leading/trailing quotes from the entire line
                String cleanedLine = line.trim()
                .replace("\"\"", "\"")  // Replace double quotes inside fields with single quotes
                .replaceAll("^\"|\"$", "");  // Remove leading and trailing quotes from the entire line
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonData.toString();
    }

    public static void processStockDataFromCSV(String filePath) {
        String json = readJSONFromFile(filePath);
        String jsonData = json.trim()
        .replace("\"\"", "\"")  // Replace double quotes inside fields with single quotes
        .replaceAll("^\"|\"$", ""); 
        String cleanedJsonData = jsonData.replaceAll("^\"|\"$", ""); // Remove quotes at the beginning and end of the string
        if (cleanedJsonData.startsWith("\"") || cleanedJsonData.endsWith("\"")) {
            System.out.println("There are still quotes!");
        } else {
            System.out.println("No quotes at the start or end of the string.");
        }
        

        // Parse the JSON data
        Gson gson = new GsonBuilder().create();
        StockData stockData = gson.fromJson(jsonData, StockData.class);

        // Accessing Meta Data
        MetaData metaData = stockData.getMetaData();
        int metadataId = saveMetaData(metaData); // Method to save metadata and get the ID

        // Save time series data to the database
        Map<String, TimeSeriesData> timeSeriesMap = stockData.getTimeSeries();
        saveTimeSeriesData(metadataId, timeSeriesMap); // Method to save time series

    }

    private static int saveMetaData(MetaData metaData) {
    int generatedId = 0;
    String query = "INSERT INTO metadata (information, symbol, last_refreshed, `interval`, output_size, time_zone) "
                 + "VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

        pstmt.setString(1, metaData.getInformation());
        pstmt.setString(2, metaData.getSymbol());
        pstmt.setString(3, metaData.getLastRefreshed());
        pstmt.setString(4, metaData.getInterval());
        pstmt.setString(5, metaData.getOutputSize());
        pstmt.setString(6, metaData.getTimeZone());

        int affectedRows = pstmt.executeUpdate();

        // Retrieve the generated ID (primary key) for metadata
        if (affectedRows > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return generatedId;
}

private static void saveTimeSeriesData(int metadataId, Map<String, TimeSeriesData> timeSeriesMap) {
    String query = "INSERT INTO timeseries (metadata_id, timestamp, open, high, low, close, volume) "
                 + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        for (Map.Entry<String, TimeSeriesData> entry : timeSeriesMap.entrySet()) {
            String timestamp = entry.getKey();
            TimeSeriesData timeSeriesData = entry.getValue();

            pstmt.setInt(1, metadataId);
            pstmt.setString(2, timestamp);
            pstmt.setBigDecimal(3, new BigDecimal(timeSeriesData.getOpen()));
            pstmt.setBigDecimal(4, new BigDecimal(timeSeriesData.getHigh()));
            pstmt.setBigDecimal(5, new BigDecimal(timeSeriesData.getLow()));
            pstmt.setBigDecimal(6, new BigDecimal(timeSeriesData.getClose()));
            pstmt.setInt(7, Integer.parseInt(timeSeriesData.getVolume())); // Convert String to int

            pstmt.addBatch(); // Add to batch for efficient insert
        }

        pstmt.executeBatch(); // Execute all the inserts in a batch
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}