package com.example.parking.models;

public class Reservation {
    private String date;
    private String time;
    private String car;
    private String carNum;

    public Reservation(String date, String time, String car, String carNum) {
        this.date = date;
        this.time = time;
        this.car = car;
        this.carNum = carNum;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getCar() {
        return car;
    }

    public String getCarNum() {
        return carNum;
    }
}
