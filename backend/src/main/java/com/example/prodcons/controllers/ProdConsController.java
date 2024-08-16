package com.example.prodcons.controllers;

import com.example.prodcons.models.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ProdConsController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/simulation")
    @SendTo("/topic/updates")
    public String greeting(String diagram) throws InterruptedException, JsonProcessingException {
        System.out.println(diagram);
        ObjectMapper objectMapper = new ObjectMapper();
        Diagram diagram1 = objectMapper.readValue(diagram, Diagram.class);

        System.out.println("------------------");
        System.out.println(diagram);
        /*for(int i=0; i<diagram1.getM2q().size(); i++)
            System.out.print(diagram1.getM2q().get(i) + " ");
        System.out.println(" ");
        for(int i=0; i<diagram1.getQ2m().size(); i++)
            System.out.print(diagram1.getQ2m().get(i) + " ");*/
        System.out.println("------------------");

        Simulation simulation = new Simulation(diagram1, simpMessagingTemplate);
        simulation.startSimulation();
        return "received your simulation";
    }
}
