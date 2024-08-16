package com.example.prodcons.models;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.List;

public class Diagram {
    private int no_machines;
    private int no_queues;
    private HashMap<String, List<Integer>> m2q;
    private HashMap<String, List<Integer>> q2m;
    private List<Integer> inputterQ;
    private List<Integer> outputterQ;

    public Diagram() {
    }

    public int getNo_machines() {
        return no_machines;
    }

    public int getNo_queues() {
        return no_queues;
    }

    public void setNo_machines(int no_machines) {
        this.no_machines = no_machines;
    }

    public void setNo_queues(int no_queues) {
        this.no_queues = no_queues;
    }

    public HashMap<String, List<Integer>> getM2q() {
        return m2q;
    }

    public HashMap<String, List<Integer>> getQ2m() {
        return q2m;
    }

    public void setM2q(HashMap<String, List<Integer>> m2q) {
        this.m2q = m2q;
    }

    public void setQ2m(HashMap<String, List<Integer>> q2m) {
        this.q2m = q2m;
    }

    public List<Integer> getInputterQ() {
        return inputterQ;
    }

    public List<Integer> getOutputterQ() {
        return outputterQ;
    }

    public void setInputterQ(List<Integer> inputterQ) {
        this.inputterQ = inputterQ;
    }

    public void setOutputterQ(List<Integer> outputterQ) {
        this.outputterQ = outputterQ;
    }
}
