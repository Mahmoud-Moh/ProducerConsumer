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
            updater.update("queue", this.id, "dequeue", product, size());
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

}
