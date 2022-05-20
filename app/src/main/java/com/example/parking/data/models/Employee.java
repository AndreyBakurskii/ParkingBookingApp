package com.example.parking.data.models;

public class Employee {
    private String email;
    private String name;

    public Employee(String name, String email) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
