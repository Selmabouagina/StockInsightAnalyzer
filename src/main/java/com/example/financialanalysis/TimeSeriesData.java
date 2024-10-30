/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.financialanalysis;

import com.google.gson.annotations.SerializedName;

class TimeSeriesData {
    @SerializedName("1. open")
    private String open;

    @SerializedName("2. high")
    private String high;

    @SerializedName("3. low")
    private String low;

    @SerializedName("4. close")
    private String close;

    @SerializedName("5. volume")
    private String volume;

    public TimeSeriesData(String open, String high, String low, String close, String volume) {
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public TimeSeriesData() {
    }

    // Getters and setters
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
