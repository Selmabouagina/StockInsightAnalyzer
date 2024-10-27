package com.example.financialanalysis;

import java.util.Map;
import com.google.gson.annotations.SerializedName;

public class StockData {
    @SerializedName("Meta Data")
    private MetaData metaData;

    @SerializedName("Time Series (5min)")
    private Map<String, TimeSeriesData> timeSeries;

    // Getters and setters
    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Map<String, TimeSeriesData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(Map<String, TimeSeriesData> timeSeries) {
        this.timeSeries = timeSeries;
    }
}