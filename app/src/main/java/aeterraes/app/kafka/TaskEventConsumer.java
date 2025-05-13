package aeterraes.app.kafka;

import aeterraes.app.controller.dto.TaskStatus;
import aeterraes.app.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventConsumer {
    private final NotificationService notificationService;
    @KafkaListener(
            topics = "${task.kafka.topics.updates}",
            groupId = "task-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeTaskUpdate(@Payload TaskStatus update) {
        log.info("Updated {}: {}", update.getTaskId(), update.getNewStatus());
        notificationService.sendNotification(update.getTaskId(), update.getNewStatus());
    }
}