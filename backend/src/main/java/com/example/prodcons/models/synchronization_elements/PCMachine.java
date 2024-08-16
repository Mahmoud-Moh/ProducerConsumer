package com.example.prodcons.models.synchronization_elements;

import com.example.prodcons.ws.Updater;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCMachine implements Runnable{
    String id;
    String color;
    State state;
    Lock lock = new ReentrantLock();
    Lock setProductLock = new ReentrantLock();
    PCQueueWrapper outQueueW;
    private Updater updater;
    private Product productToServe;

    public PCMachine(String id) {
        this.color = "black";
        this.state = State.IDLE;
        this.id = id;
    }


    public void setOutQueueW(PCQueueWrapper outQueueW) {
        this.outQueueW = outQueueW;
    }

    public void serve(Product p) throws InterruptedException {

        lock.lock();
        try {
            System.out.println("Machine " + id + "Started Processing Product" + p.getId());
            updateState(p.getColor(), State.RUNNING);
            updater.update("machine", id, "start", p);
            Thread.sleep(4000);
            finish(p);
            System.out.println("Machine " + id + "Finished Processing Product" + p.getId());
            updater.update("machine", id, "finish", p);
        }finally {
            lock.unlock();
        }
    }

    public void updateState(String color, State state) {
        this.color = color;
        this.state = state;
    }

    public void updateState(){
        this.state = State.RUNNING;
    }

    private void finish(Product p){
        p.setLastPlace("m" + String.valueOf(id));
        outQueueW.queue.enqueue(p);
        this.state = State.IDLE;
    }

    @Override
    public void run(){
        while(true){
            if(state == State.READY){
                System.out.println(productToServe.getId());
                try {
                    serve(productToServe);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public Updater getUpdater() {
        return updater;
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public boolean setProductToServe(Product p) {
        if(state != State.IDLE)
            return false;
        setProductLock.lock();
        this.productToServe = p;
        state = State.READY;
        setProductLock.unlock();
        return true;
    }
}
