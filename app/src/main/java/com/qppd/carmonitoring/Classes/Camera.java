package com.qppd.carmonitoring.Classes;

public class Camera {

    private int detection;

    private Camera(){

    }

    public Camera(int detection) {
        this.detection = detection;
    }

    public int getDetection() {
        return detection;
    }

    public void setDetection(int detection) {
        this.detection = detection;
    }
}
