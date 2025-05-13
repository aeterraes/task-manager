package aeterraes.app.kafka;

import aeterraes.app.controller.dto.TaskStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskEventProducer {

    private final KafkaTemplate<String, TaskStatus> kafkaTemplate;

    public void sendTaskStatusUpdate(int taskId, String newStatus) {
        TaskStatus update = new TaskStatus();
        update.setTaskId(taskId);
        update.setNewStatus(newStatus);
        kafkaTemplate.send("task-updates", String.valueOf(taskId), update)
                .whenComplete((_, ex) -> {
                    if (ex != null) {
                        log.error("Error {}", taskId, ex);
                    } else {
                        log.info("Message {} sent", taskId);
                    }
                });
    }
}