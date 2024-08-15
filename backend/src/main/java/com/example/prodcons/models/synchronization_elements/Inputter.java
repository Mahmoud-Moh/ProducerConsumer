package com.example.prodcons.models.synchronization_elements;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Inputter implements Runnable{

    private PCQueueWrapper InitialQueue;
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
    }
    public void setInitialQueue(PCQueueWrapper initialQueue) {
        InitialQueue = initialQueue;
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
            InitialQueue.queue.enqueue(product);
            try {
                Thread.sleep(1000);
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
        return colors[(colorCounter++)%colors.length];
    }
}
