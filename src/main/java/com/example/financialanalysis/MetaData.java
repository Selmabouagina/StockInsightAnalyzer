/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.financialanalysis;

import com.google.gson.annotations.SerializedName;

class MetaData {
    @SerializedName("1. Information")
    private String information;

    @SerializedName("2. Symbol")
    private String symbol;

    @SerializedName("3. Last Refreshed")
    private String lastRefreshed;

    @SerializedName("4. Interval")
    private String interval;

    @SerializedName("5. Output Size")
    private String outputSize;

    @SerializedName("6. Time Zone")
    private String timeZone;

    // Getters and setters
    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLastRefreshed() {
        return lastRefreshed;
    }

    public void setLastRefreshed(String lastRefreshed) {
        this.lastRefreshed = lastRefreshed;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
