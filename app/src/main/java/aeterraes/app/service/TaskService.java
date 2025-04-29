package aeterraes.app.service;

import aeterraes.app.aspect.annotation.ExceptionHandling;
import aeterraes.app.aspect.annotation.LogTimeTracking;
import aeterraes.app.aspect.annotation.Loggable;
import aeterraes.app.aspect.annotation.ResultHandling;
import aeterraes.app.dataaccess.entity.Task;
import aeterraes.app.dataaccess.repository.TaskRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@RequiredArgsConstructor
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public void createTask(Task task) {
        taskRepository.save(task);
    }

    @Loggable
    @ExceptionHandling
    @LogTimeTracking
    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }

    @Loggable
    @ResultHandling
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    public boolean deleteTaskById(int id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Loggable
    @ExceptionHandling
    @ResultHandling
    @LogTimeTracking
    public Optional<Task> updateTask(int id, Task task) {
        return taskRepository.findById(id)
                .map(currentTask -> {
                    currentTask.setTitle(task.getTitle());
                    currentTask.setDescription(task.getDescription());
                    currentTask.setUser(task.getUser());
                    return taskRepository.save(currentTask);
                });
    }
}
