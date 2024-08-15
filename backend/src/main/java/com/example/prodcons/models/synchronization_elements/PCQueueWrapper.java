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
            //ArrayList<PCMachine> readyMachines = new ArrayList<>();
            /*for(PCMachine machine : machineList){
                System.out.println("--> machine" + machine.id);
                if(machine.state == State.IDLE) {
                    System.out.println("$$ machine" + machine.id);
                    readyMachines.add(machine);
                }
            }*/

            /*int len = min(readyMachines.size(), queue.size());
            System.out.println("Queue" + queue.getId() + "readyMachines" + len);
            for(int i = 0; i < len; i++) {
                Product product = queue.dequeue();
                System.out.println("Machine " + readyMachines.get(i).id + "chosen to serve");
                //readyMachines.get(i).serve(product);
                readyMachines.get(i).setProductToServe(product);
            }*/

            for(PCMachine machine : machineList){
                if(machine.state == State.IDLE && queue.size() > 0){
                    Product product = queue.dequeue();
                    System.out.println("Product " + product.getId() + "served by machine " + machine.id);
                    boolean returnBool = machine.setProductToServe(product);
                    System.out.println(returnBool);
                }
            }
        }
    }
}
