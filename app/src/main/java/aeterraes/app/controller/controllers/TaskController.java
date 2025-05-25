package aeterraes.app.controller.controllers;

import aeterraes.app.controller.dto.TaskDTO;
import aeterraes.app.dataaccess.entity.Task;
import aeterraes.app.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable int id) {
        Task task = taskService.getTaskById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Task with id %d not found", id)
                ));
        return modelMapper.map(task, TaskDTO.class);
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(task -> modelMapper.map(task, TaskDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    public void createTask(@RequestBody TaskDTO taskDTO) {
        taskService.createTask(modelMapper.map(taskDTO, Task.class));
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable int id, @RequestBody TaskDTO taskDTO) {
        Task updatedTask = taskService.updateTask(id, modelMapper.map(taskDTO, Task.class))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Task with id %d not found", id)
                ));
        return modelMapper.map(updatedTask, TaskDTO.class);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable int id) {
        taskService.deleteTaskById(id);
    }
}
