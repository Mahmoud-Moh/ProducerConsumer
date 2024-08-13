package com.example.prodcons.models;

public class HelloMessage {

    private String name;

    public HelloMessage(String name, int id) {
        this.name = name;
        this.id = id;
    }

    private int id;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}