package aeterraes.app.service;

import aeterraes.app.dataaccess.entity.Task;
import aeterraes.app.dataaccess.repository.TaskRepository;
import aeterraes.app.kafka.TaskEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskEventProducer taskEventProducer;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {}

    private void getExampleTasks() {
        task1 = new Task();
        task1.setId(1);
        task1.setTitle("title");
        task1.setDescription("description");
        task1.setStatus("status");
        task1.setUserId(1);

        task2 = new Task();
        task2.setId(2);
        task2.setTitle("title2");
        task2.setDescription("description2");
        task2.setStatus("status2");
        task2.setUserId(1);
    }

    @Test
    void createTask() {
        getExampleTasks();
        taskService.createTask(task1);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void getTaskByIdReturnsTask() {
        getExampleTasks();
        when(taskRepository.findById(1)).thenReturn(Optional.of(task1));
        Optional<Task> result = taskService.getTaskById(1);
        assertTrue(result.isPresent());
        assertEquals(task1, result.get());
    }

    @Test
    void getTaskByIdReturnsEmptyOptional() {
        when(taskRepository.findById(1337)).thenReturn(Optional.empty());
        Optional<Task> result = taskService.getTaskById(1337);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllTasks() {
        getExampleTasks();
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
        List<Task> result = taskService.getAllTasks();
        assertEquals(2, result.size());
        assertTrue(result.contains(task1));
        assertTrue(result.contains(task2));
    }

    @Test
    void deleteTaskById() {
        taskService.deleteTaskById(1);
        verify(taskRepository, times(1)).deleteById(1);
    }

    @Test
    void updateTaskNotFound() {
        getExampleTasks();
        when(taskRepository.findById(1337)).thenReturn(Optional.empty());
        Optional<Task> result = taskService.updateTask(1337, task1);
        assertTrue(result.isEmpty());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateTaskChangeStatusAndSendMessage() {
        getExampleTasks();
        Task updatedTask = new Task();
        updatedTask.setTitle("title");
        updatedTask.setDescription("description");
        updatedTask.setUserId(1);
        updatedTask.setStatus("new");
        when(taskRepository.findById(1)).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Optional<Task> result = taskService.updateTask(1, updatedTask);
        assertTrue(result.isPresent());
        Task saved = result.get();
        assertEquals("new", saved.getStatus());
        assertEquals("title", saved.getTitle());
        assertEquals("description", saved.getDescription());
        assertEquals(1, saved.getUserId());
        verify(taskEventProducer, times(1))
                .sendTaskStatusUpdate(1, "new");
        verify(taskRepository).save(saved);
    }
}