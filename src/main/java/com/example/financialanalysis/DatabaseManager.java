package com.example.financialanalysis;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/financial_analysis";
    private static final String USER = "root";
    private static final String PASS = "salma";

    public DatabaseManager() {
    }

    public int saveMetaData(MetaData metaData) {
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

    public void saveTimeSeriesData(int metadataId, Map<String, TimeSeriesData> timeSeriesMap) {
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
                pstmt.setInt(7, Integer.parseInt(timeSeriesData.getVolume()));

                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch metadata based on the symbol
    public MetaData fetchMetaData(String symbol) {
        String query = "SELECT information, symbol, last_refreshed, `interval`, output_size, time_zone FROM metadata WHERE symbol = ?";
        MetaData metaData = null;

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, symbol);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    metaData = new MetaData();
                    metaData.setInformation(rs.getString("information"));
                    metaData.setSymbol(rs.getString("symbol"));
                    metaData.setLastRefreshed(rs.getString("last_refreshed"));
                    metaData.setInterval(rs.getString("interval"));
                    metaData.setOutputSize(rs.getString("output_size"));
                    metaData.setTimeZone(rs.getString("time_zone"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return metaData;
    }

    // Fetch time series data based on metadataId
    public Map<String, TimeSeriesData> fetchTimeSeriesData(int metadataId) {
        String query = "SELECT timestamp, open, high, low, close, volume FROM timeseries WHERE metadata_id = ?";
        Map<String, TimeSeriesData> timeSeriesMap = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, metadataId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String timestamp = rs.getString("timestamp");
                    TimeSeriesData timeSeriesData = new TimeSeriesData();
                    timeSeriesData.setOpen(rs.getString("open"));
                    timeSeriesData.setHigh(rs.getString("high"));
                    timeSeriesData.setLow(rs.getString("low"));
                    timeSeriesData.setClose(rs.getString("close"));
                    timeSeriesData.setVolume(rs.getString("volume"));

                    timeSeriesMap.put(timestamp, timeSeriesData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return timeSeriesMap;
    }
}
