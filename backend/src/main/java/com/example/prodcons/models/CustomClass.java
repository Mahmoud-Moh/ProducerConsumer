package com.example.prodcons.models;

import java.util.List;

public class CustomClass {
    public CustomClass(List<Integer> name, String job) {
        this.name = name;
        this.job = job;
    }

    public List<Integer> getName() {
        return name;
    }

    public void setName(List<Integer> name) {
        this.name = name;
    }

    private List<Integer> name;


    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }


    private String job;

    public CustomClass() {
    }

}
