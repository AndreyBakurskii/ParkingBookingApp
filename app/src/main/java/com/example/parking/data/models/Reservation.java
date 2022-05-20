package com.example.parking.data.models;

public class Reservation {
    private String date;
    private String time;
    private String car;
    private String carNum;
    private String email;

    public Reservation(String date, String time, String car, String carNum, String email) {
        this.date = date;
        this.time = time;
        this.car = car;
        this.carNum = carNum;
        this.email = email;
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

    public String getEmail() {
        return email;
    }
}
