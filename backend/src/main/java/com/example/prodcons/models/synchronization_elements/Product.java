package com.example.prodcons.models.synchronization_elements;

public class Product {
    private String color;
    private String Id;
    private String lastPlace;

    public void setColor(String color) {
        this.color = color;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setLastPlace(String lastPlace) {
        this.lastPlace = lastPlace;
    }

    public Product(String color, String id) {
        this.color = color;
        Id = id;
    }

    public String getColor() {
        return color;
    }

    public String getLastPlace() {
        return lastPlace;
    }

    public String getId() {
        return Id;
    }
}
