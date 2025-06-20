package com.qppd.carmonitoring.Classes;

public class Setting {

    private int device_status;
    private int alarm_status;
    private int sleep_mode;

    public Setting(int device_status, int alarm_status, int sleep_mode) {
        this.device_status = device_status;
        this.alarm_status = alarm_status;
        this.sleep_mode = sleep_mode;
    }

    public Setting(){

    }

    public int getDevice_status() {
        return device_status;
    }

    public void setDevice_status(int device_status) {
        this.device_status = device_status;
    }

    public int getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(int alarm_status) {
        this.alarm_status = alarm_status;
    }

    public int getsleep_mode() {
        return sleep_mode;
    }

    public void setsleep_mode(int sleep_mode) {
        this.sleep_mode = sleep_mode;
    }
}
