package com.qppd.carmonitoring.Classes;

public class History {

    private String snapshot;
    private String date;
    private String activated_on;
    private String deactivated_on;
    private int duration;

    public History(String snapshot, String date, String time, String activated_on,
                   String deactivated_on, int duration) {
        this.snapshot = snapshot;
        this.date = date;
        this.activated_on = activated_on;
        this.deactivated_on = deactivated_on;
        this.duration = duration;
    }

    public History(){

    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivated_on() {
        return activated_on;
    }

    public void setActivated_on(String activated_on) {
        this.activated_on = activated_on;
    }

    public String getDeactivated_on() {
        return deactivated_on;
    }

    public void setDeactivated_on(String deactivated_on) {
        this.deactivated_on = deactivated_on;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
