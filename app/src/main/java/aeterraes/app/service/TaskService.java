package aeterraes.app.service;

import aeterraes.app.aspect.annotation.ExceptionHandling;
import aeterraes.app.aspect.annotation.LogTimeTracking;
import aeterraes.app.aspect.annotation.Loggable;
import aeterraes.app.aspect.annotation.ResultHandling;
import aeterraes.app.dataaccess.entity.Task;
import aeterraes.app.dataaccess.repository.TaskRepository;
import aeterraes.app.kafka.TaskEventProducer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskEventProducer taskEventProducer;

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public Task getTaskById(int id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Loggable
    @ResultHandling
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    public void deleteTaskById(int id) {
        taskRepository.deleteById(id);
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    @LogTimeTracking
    public Task updateTask(int id, Task task) {
        return taskRepository.findById(id)
                .map(currentTask -> {
                    currentTask.setTitle(task.getTitle());
                    currentTask.setDescription(task.getDescription());
                    currentTask.setUserId(task.getUserId());
                    if (!currentTask.getStatus().equals(task.getStatus())) {
                        taskEventProducer.sendTaskStatusUpdate(id, task.getStatus());
                    }
                    currentTask.setStatus(task.getStatus());
                    return taskRepository.save(currentTask);
                }).orElseThrow();
    }
}
