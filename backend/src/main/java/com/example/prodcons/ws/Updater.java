package com.example.prodcons.ws;

import com.example.prodcons.models.synchronization_elements.Product;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class Updater {
    private SimpMessagingTemplate messagingTemplate;
    public Updater(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void update(String type, String id, String operation,
    Product product, int sizeNow) {
        Update update = new Update(type, id, operation, product, sizeNow);
        //Message message = MessageBuilder.withPayload(update).build();
        messagingTemplate.convertAndSend("/topic/updates", update);
    }

    public void update(String type, String id, String operation,
    Product product) {
        Update update = new Update(type, id, operation, product);
        //Message message = MessageBuilder.withPayload(update).build();
        messagingTemplate.convertAndSend("/topic/updates", update);
    }
}
