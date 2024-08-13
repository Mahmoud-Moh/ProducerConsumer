package com.example.prodcons.models;

import com.example.prodcons.models.synchronization_elements.Inputter;
import com.example.prodcons.models.synchronization_elements.PCMachine;
import com.example.prodcons.models.synchronization_elements.PCQueue;
import com.example.prodcons.models.synchronization_elements.PCQueueWrapper;
import com.example.prodcons.ws.Updater;
import jdk.jshell.Diag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Diagram diagram;
    List<PCMachine> machines = new ArrayList<>();
    List<PCQueueWrapper> queueWrappers = new ArrayList<>();
    private final int capacity = 5;
    private Updater updater;
    SimpMessagingTemplate template;


    private void constructSimulation(){
        /*System.out.println("-----########-------");
        for(int i=0; i<diagram.getM2q().size(); i++)
            System.out.print(diagram.getM2q().get(i) + " ");
        System.out.println(" ");
        for(int i=0; i<diagram.getQ2m().size(); i++)
            System.out.print(diagram.getQ2m().get(i) + " ");
        System.out.println("-----########------");*/

        for(int i=0; i<diagram.getNo_machines(); i++){
            machines.add(new PCMachine(String.valueOf(i+1)));
            machines.get(i).setUpdater(updater);
        }
        for(int i=0; i<diagram.getNo_queues(); i++){
            queueWrappers.add(new PCQueueWrapper(new PCQueue(capacity, String.valueOf(i+1))));
            queueWrappers.get(i).queue.setUpdater(updater);
        }
        for(int i=0; i<diagram.getM2q().size(); i++){
            machines.get(i).setOutQueueW(queueWrappers.get(diagram.getM2q().get(i)));
        }
        for(int i=0; i<diagram.getQ2m().size(); i++){
            queueWrappers.get(i).addMachine(machines.get(diagram.getQ2m().get(i)));
        }
    }

    public Simulation(Diagram diagram, SimpMessagingTemplate template) {
        this.diagram = diagram;
        this.template = template;
        /*System.out.println("---@@@@@@---------");
        for(int i=0; i<diagram.getM2q().size(); i++)
            System.out.print(diagram.getM2q().get(i) + " ");
        System.out.println(" ");
        for(int i=0; i<diagram.getQ2m().size(); i++)
            System.out.print(diagram.getQ2m().get(i) + " ");
        System.out.println("---@@@@@---------");*/
        prepareUpdater();
        constructSimulation();
    }

    public void startSimulation(){
        for(PCMachine machine : machines){
            Thread t = new Thread(machine);
            t.start();
        }

        for(PCQueueWrapper queueWrapper : queueWrappers){
            Thread t = new Thread(queueWrapper);
            t.start();
        }
        Inputter inputter = new Inputter();
        inputter.setInitialQueue(queueWrappers.get(0));
        Thread t = new Thread(inputter);
        t.start();
    }

    private void prepareUpdater(){
        updater = new Updater(template);
    }
}

