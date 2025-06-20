package com.qppd.carmonitoring.Classes;

public class Timer {

    private int time;
    private int status;

    public Timer(int time, int status) {
        this.time = time;
        this.status = status;
    }

    public Timer(){

    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
