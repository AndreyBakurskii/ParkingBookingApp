package com.example.parking.models;

public class ParkingSpot {
    private String id;
    private String num;

    public ParkingSpot(String id, String num) {
        this.id = id;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public String getNum() {
        return num;
    }
}
