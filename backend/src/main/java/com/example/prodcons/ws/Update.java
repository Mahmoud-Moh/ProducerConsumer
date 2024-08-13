package com.example.prodcons.ws;

import com.example.prodcons.models.synchronization_elements.Product;

public class Update {
    private String type;
    private String id;
    private String operation;
    private Product product;
    private int sizeNow;

    public Update(String type, String id, String operation, Product product) {
        this.type = type;
        this.id = id;
        this.operation = operation;
        this.product = product;
    }
    public Update(String type, String id, String operation, Product product, int sizeNow) {
        this.type = type;
        this.id = id;
        this.operation = operation;
        this.product = product;
        this.sizeNow = sizeNow;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getOperation() {
        return operation;
    }

    public Product getProduct() {
        return product;
    }

    public int getSizeNow() {
        return sizeNow;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSizeNow(int sizeNow) {
        this.sizeNow = sizeNow;
    }
}
