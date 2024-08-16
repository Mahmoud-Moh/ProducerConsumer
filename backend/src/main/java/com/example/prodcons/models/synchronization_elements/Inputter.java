package com.example.prodcons.models.synchronization_elements;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class Inputter implements Runnable{

    private List<PCQueueWrapper> InitialQueues;
    String[] colors = {
            "#FF0000", // Red
            "#00FF00", // Lime
            "#0000FF", // Blue
            "#FF00FF", // Fuchsia
            "#800000", // Maroon
            "#808000", // Olive
            "#008000", // Green
            "#800080", // Purple
            "#008080", // Teal
    };
    int colorCounter = -1;
    private SimpMessagingTemplate messagingTemplate;

    public Inputter() {
        this.InitialQueues = new ArrayList<>();
    }

    @Override
    public void run() {
        String color, id;
        while(true){
            color = getColor();
            id = String.valueOf(colorCounter);
            Product product = new Product(color, id);
            product.setLastPlace("i");
            System.out.println("Inputter push Product" + id);
            int minSize = Integer.MAX_VALUE;
            int minSize_idx = 0;
            for(int i=0; i < InitialQueues.size(); i++){
                if(InitialQueues.get(i).queue.size() < minSize){
                    minSize = InitialQueues.get(i).queue.size();
                    minSize_idx = i;
                }
            }
            InitialQueues.get(minSize_idx).queue.enqueue(product);
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public SimpMessagingTemplate getMessagingTemplate() {
        return messagingTemplate;
    }

    public void setMessagingTemplate(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public String getColor(){
        return colors[(++colorCounter)%colors.length];
    }

    public List<PCQueueWrapper> getInitialQueues() {
        return InitialQueues;
    }

    public void setInitialQueues(List<PCQueueWrapper> initialQueues) {
        InitialQueues = initialQueues;
    }

    public void addInitialQueue(PCQueueWrapper initialQueue){
        InitialQueues.add(initialQueue);
    }
}
