package com.example.prodcons.models.synchronization_elements;

import java.util.ArrayList;

import static java.lang.Math.min;

public class PCQueueWrapper implements Runnable{

    public PCQueue queue;
    private ArrayList<PCMachine> machineList = new ArrayList<>();

    private boolean IsLastQueue = false;

    public PCQueueWrapper(PCQueue queue) {
        this.queue = queue;
    }
    public void setLastQueue(boolean lastQueue) {
        IsLastQueue = lastQueue;
    }

    public void addMachine(PCMachine machine){
        machineList.add(machine);
    }

    @Override
    public void run() {
        while(true) {
            if(IsLastQueue){
                queue.dequeue();
            }
            ArrayList<PCMachine> readyMachines = new ArrayList<>();
            for(PCMachine machine : machineList){
                if(machine.state != State.RUNNING)
                    readyMachines.add(machine);
            }
            int len = min(machineList.size(), queue.size());
            for(int i = 0; i < len; i++) {
                Product product = queue.dequeue();
                try {
                    readyMachines.get(i).serve(product);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
