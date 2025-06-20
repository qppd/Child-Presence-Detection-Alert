package com.qppd.carmonitoring.Classes;

public class User {

    private String name;
    private String device_id;
    private String password;
    private int status;

    public User(String name, String device_id, String password, int status) {
        this.name = name;
        this.device_id = device_id;
        this.password = password;
        this.status = status;
    }

    public User(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
