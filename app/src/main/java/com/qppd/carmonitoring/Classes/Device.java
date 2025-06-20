package com.qppd.carmonitoring.Classes;

public class Device {

    private int device_percentage;
    private int device_status;

    public Device(int device_percentage, int device_status) {
        this.device_percentage = device_percentage;
        this.device_status = device_status;
    }

    public Device(){

    }

    public int getDevice_percentage() {
        return device_percentage;
    }

    public void setDevice_percentage(int device_percentage) {
        this.device_percentage = device_percentage;
    }

    public int getDevice_status() {
        return device_status;
    }

    public void setDevice_status(int device_status) {
        this.device_status = device_status;
    }
}
