package com.example.parking.data.models;

public class Car {
    private String id;
    private String model;
    private String num;

    public Car(String id, String model, String num) {
        this.id = id;
        this.model = model;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getNum() {
        return num;
    }

}
