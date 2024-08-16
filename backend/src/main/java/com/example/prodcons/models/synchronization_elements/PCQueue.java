package com.example.prodcons.models.synchronization_elements;

import com.example.prodcons.ws.Updater;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCQueue {
    private ArrayList<Product> q = new ArrayList<>();
    private Lock lock = new ReentrantLock();
    private Condition not_empty = lock.newCondition();
    private Condition not_full = lock.newCondition();
    private int capacity;
    private String id;
    private Updater updater;
    private boolean isLastQueue = false;

    public PCQueue(int capacity, String id) {
        this.capacity = capacity;
        this.id = id;
    }

    public void enqueue(Product product){
        lock.lock();
        try{
            while(q.size() == capacity){
                not_full.await();
            }
            System.out.println("Queue " + id + "enqueuing product " + product.getId()
            + "queue.size " + size());
            q.add(product);
            updater.update("queue", this.id, "enqueue", product, size());
            not_empty.signal();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public Product dequeue(){
        lock.lock();
        try{
            while(q.size() == 0){
                not_empty.await();
            }
            Product product = q.get(0);
            product.setLastPlace("q" + String.valueOf(id));
            System.out.println("Queue " + id + "dequeuing product " + product.getId()
                    + "queue.size " + size());
            q.remove(0);
            if(isLastQueue){
                updater.update("queue", this.id, "throw", product, size());
            }else{
                updater.update("queue", this.id, "dequeue", product, size());
            }
            not_full.signal();
            return product;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int size(){
        lock.lock();
        try {
            return q.size();
        } finally {
            lock.unlock();
        }
    }


    public Updater getUpdater() {
        return updater;
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public ArrayList<Product> getQ() {
        return q;
    }

    public Lock getLock() {
        return lock;
    }

    public Condition getNot_empty() {
        return not_empty;
    }

    public Condition getNot_full() {
        return not_full;
    }

    public int getCapacity() {
        return capacity;
    }

    public String getId() {
        return id;
    }

    public void setQ(ArrayList<Product> q) {
        this.q = q;
    }

    public void setLock(Lock lock) {
        this.lock = lock;
    }

    public void setNot_empty(Condition not_empty) {
        this.not_empty = not_empty;
    }

    public void setNot_full(Condition not_full) {
        this.not_full = not_full;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLastQueue() {
        return isLastQueue;
    }

    public void setLastQueue(boolean lastQueue) {
        isLastQueue = lastQueue;
    }
}
