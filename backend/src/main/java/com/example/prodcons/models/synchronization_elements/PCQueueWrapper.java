package com.example.prodcons.models.synchronization_elements;

import java.util.ArrayList;

import static java.lang.Math.min;

public class PCQueueWrapper implements Runnable{

    public PCQueue queue;
    private ArrayList<PCMachine> machineList = new ArrayList<>();


    public PCQueueWrapper(PCQueue queue) {
        this.queue = queue;
    }


    public void addMachine(PCMachine machine){
        machineList.add(machine);
    }

    @Override
    public void run() {
        while(true) {
            if(queue.isLastQueue()){
                queue.dequeue();
            }else {
                for (PCMachine machine : machineList) {
                    if (machine.state == State.IDLE && queue.size() > 0) {
                        Product product = queue.dequeue();
                        System.out.println("Product " + product.getId() + "served by machine " + machine.id);
                        boolean returnBool = machine.setProductToServe(product);
                        System.out.println(returnBool);
                    }
                }
            }
        }
    }
}
