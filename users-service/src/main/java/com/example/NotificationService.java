package com.example;

import com.example.events.GamePurchasedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class NotificationService {

    @Incoming("game-purchases-in")
    public void sendNotification(String eventJson) {
        // Тут ви отримаєте JSON рядок. Можете просто вивести його або розпарсити.
        System.out.println("NEW MESSAGE FROM KAFKA: " + eventJson);
    }
}