package com.example.parking;

public class Reservation {
    private String date;
    private String time;
    private String car;

    public Reservation(String date, String time, String car) {
        this.date = date;
        this.time = time;
        this.car = car;
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
}
