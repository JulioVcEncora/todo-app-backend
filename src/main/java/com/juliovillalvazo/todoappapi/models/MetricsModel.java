package com.juliovillalvazo.todoappapi.models;

public class MetricsModel {
    private long generalAverageTime;
    private long lowAverageTime;
    private long mediumAverageTime;
    private long highAverageTime;

    public MetricsModel(long generalAverageTime, long lowAverageTime, long mediumAverageTime, long highAverageTime) {
        this.generalAverageTime = generalAverageTime;
        this.lowAverageTime = lowAverageTime;
        this.mediumAverageTime = mediumAverageTime;
        this.highAverageTime = highAverageTime;
    }

    public long getGeneralAverageTime() {
        return generalAverageTime;
    }

    public void setGeneralAverageTime(long generalAverageTime) {
        this.generalAverageTime = generalAverageTime;
    }

    public long getLowAverageTime() {
        return lowAverageTime;
    }

    public void setLowAverageTime(long lowAverageTime) {
        this.lowAverageTime = lowAverageTime;
    }

    public long getMediumAverageTime() {
        return mediumAverageTime;
    }

    public void setMediumAverageTime(long mediumAverageTime) {
        this.mediumAverageTime = mediumAverageTime;
    }

    public long getHighAverageTime() {
        return highAverageTime;
    }

    public void setHighAverageTime(long highAverageTime) {
        this.highAverageTime = highAverageTime;
    }
}
