package br.inatel.gameproject.listener;

import br.inatel.gameproject.model.dto.NotificationDTO;
import br.inatel.gameproject.service.PublisherManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {
    private final PublisherManagerService publisherManagerService;

    @Autowired
    public NotificationListener(PublisherManagerService publisherManagerService) {
        this.publisherManagerService = publisherManagerService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void publisherNotificationManager() {
        NotificationDTO[] notifications = publisherManagerService.sendPublisherManagerNotification();
        log.info("Notified through: {}", (Object) notifications);
    }
}
